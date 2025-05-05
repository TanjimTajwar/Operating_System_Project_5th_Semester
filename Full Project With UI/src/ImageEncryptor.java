import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageEncryptor extends JFrame {
    private JLabel imagePreviewLabel;
    private JPasswordField keyField;
    private String selectedImagePath;
    private BufferedImage originalImage;
    private Color primaryColor = new Color(52, 152, 219);  // Modern blue
    private Color secondaryColor = new Color(46, 204, 113);  // Modern green
    private Color backgroundColor = new Color(236, 240, 241);  // Light gray background
    private Color textColor = new Color(44, 62, 80);  // Dark blue text
    private Color buttonHoverColor = new Color(41, 128, 185);  // Darker blue for hover
    
    public ImageEncryptor() {
        setTitle("Image Encryption");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create title
        JLabel titleLabel = createStyledLabel("Image Encryption", 24);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create center panel for image preview
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            createStyledBorder(), "Image Preview", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), textColor));

        imagePreviewLabel = new JLabel("No image selected", SwingConstants.CENTER);
        imagePreviewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        centerPanel.add(imagePreviewLabel, BorderLayout.CENTER);

        // Create bottom panel for key input and buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(backgroundColor);

        JLabel keyLabel = new JLabel("Encryption Key (16 chars):");
        keyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        keyField = createStyledPasswordField();
        keyField.setPreferredSize(new Dimension(200, 30));

        JButton browseButton = createStyledButton("Browse Image");
        browseButton.addActionListener(e -> browseImage());

        JButton encryptButton = createStyledButton("Encrypt");
        encryptButton.addActionListener(e -> encryptImage());

        JButton clearButton = createStyledButton("Clear");
        clearButton.addActionListener(e -> {
            imagePreviewLabel.setText("No image selected");
            imagePreviewLabel.setIcon(null);
            keyField.setText("");
            selectedImagePath = null;
            originalImage = null;
        });

        bottomPanel.add(keyLabel);
        bottomPanel.add(keyField);
        bottomPanel.add(browseButton);
        bottomPanel.add(encryptButton);
        bottomPanel.add(clearButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image to Encrypt");
        
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg") 
                    || f.getName().toLowerCase().endsWith(".png") 
                    || f.getName().toLowerCase().endsWith(".jpeg");
            }
            public String getDescription() {
                return "Image files (*.jpg, *.png, *.jpeg)";
            }
        });
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedImagePath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                originalImage = ImageIO.read(new File(selectedImagePath));
                if (originalImage != null) {
                    ImageIcon imageIcon = new ImageIcon(scaleImage(originalImage, 400, 300));
                    imagePreviewLabel.setIcon(imageIcon);
                    imagePreviewLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Error loading image!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void encryptImage() {
        if (selectedImagePath == null || originalImage == null) {
            JOptionPane.showMessageDialog(this, "Please select an image!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String key = new String(keyField.getPassword());
        if (key.length() != 16) {
            JOptionPane.showMessageDialog(this, "Key must be exactly 16 characters!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Convert image to bytes
            byte[] imageData = convertImageToBytes(originalImage);
            
            // Calculate chunk size for multi-threading
            int chunkSize = 1024 * 1024; // 1MB chunks
            int numChunks = (int) Math.ceil((double) imageData.length / chunkSize);
            
            // Create thread pool for parallel processing
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            List<Future<byte[]>> futures = new ArrayList<>();
            
            // Process chunks in parallel
            for (int i = 0; i < numChunks; i++) {
                final int start = i * chunkSize;
                final int end = Math.min(start + chunkSize, imageData.length);
                futures.add(executor.submit(() -> {
                    byte[] chunk = Arrays.copyOfRange(imageData, start, end);
                    byte[] keyBytes = key.getBytes();
                    byte[] encryptedChunk = new byte[chunk.length];
                    
                    for (int j = 0; j < chunk.length; j++) {
                        encryptedChunk[j] = (byte) (chunk[j] ^ keyBytes[j % keyBytes.length]);
                    }
                    
                    return encryptedChunk;
                }));
            }
            
            // Combine encrypted chunks
            byte[] encryptedData = new byte[imageData.length];
            int currentPos = 0;
            for (Future<byte[]> future : futures) {
                byte[] chunk = future.get();
                System.arraycopy(chunk, 0, encryptedData, currentPos, chunk.length);
                currentPos += chunk.length;
            }
            
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
            
            // Create and save encrypted image
            BufferedImage encryptedImage = convertBytesToImage(encryptedData, originalImage.getWidth(), originalImage.getHeight());
            BufferedImage blurredImage = applyBlurEffect(encryptedImage, 50);

            String outputPath = selectedImagePath.substring(0, selectedImagePath.lastIndexOf('.')) + "_encrypted.png";
            ImageIO.write(blurredImage, "png", new File(outputPath));

            // Update preview
            imagePreviewLabel.setIcon(new ImageIcon(scaleImage(blurredImage, 400, 300)));
            JOptionPane.showMessageDialog(this, 
                "Image encrypted and saved as: " + outputPath, 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error during encryption: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private byte[] convertImageToBytes(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        
        byte[] data = new byte[width * height * 3];
        int index = 0;
        for (int pixel : pixels) {
            data[index++] = (byte) ((pixel >> 16) & 0xFF); // Red
            data[index++] = (byte) ((pixel >> 8) & 0xFF);  // Green
            data[index++] = (byte) (pixel & 0xFF);         // Blue
        }
        return data;
    }

    private BufferedImage convertBytesToImage(byte[] data, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] pixels = new int[width * height];
        int index = 0;
        for (int i = 0; i < pixels.length; i++) {
            int r = data[index++] & 0xFF;
            int g = data[index++] & 0xFF;
            int b = data[index++] & 0xFF;
            pixels[i] = (r << 16) | (g << 8) | b;
        }
        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }

    private BufferedImage applyBlurEffect(BufferedImage image, int radius) {
        float[] kernelData = new float[radius * radius];
        Arrays.fill(kernelData, 1.0f / (radius * radius));
        Kernel kernel = new Kernel(radius, radius, kernelData);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    private Image scaleImage(BufferedImage img, int width, int height) {
        if (img == null) return null;
        return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    g.setColor(buttonHoverColor);
                } else if (getModel().isRollover()) {
                    g.setColor(secondaryColor);
                } else {
                    g.setColor(primaryColor);
                }
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JLabel createStyledLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        label.setForeground(textColor);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(new RoundedBorder(10));
        textField.setBackground(Color.WHITE);
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(new RoundedBorder(10));
        passwordField.setBackground(Color.WHITE);
        return passwordField;
    }

    private Border createStyledBorder() {
        return BorderFactory.createCompoundBorder(
            new RoundedBorder(10),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
    }

    public static void main(String[] CSECU) {
        SwingUtilities.invokeLater(() -> new ImageEncryptor().setVisible(true));
    }
}

class RoundedBorder extends AbstractBorder {
    private int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(c.getForeground());
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2d.dispose();
    }
}
