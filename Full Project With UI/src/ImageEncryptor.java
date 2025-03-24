import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Arrays;

public class ImageEncryptor extends JFrame {
    private JLabel imagePreviewLabel;
    private JPasswordField keyField;
    private String selectedImagePath;
    private BufferedImage originalImage;
    
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create title
        JLabel titleLabel = new JLabel("Image Encryption", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create center panel for image preview
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Image Preview"));

        imagePreviewLabel = new JLabel("No image selected", SwingConstants.CENTER);
        imagePreviewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        centerPanel.add(imagePreviewLabel, BorderLayout.CENTER);

        // Create bottom panel for key input and buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel keyLabel = new JLabel("Encryption Key (16 chars):");
        keyField = new JPasswordField(16);
        keyField.setPreferredSize(new Dimension(200, 30));

        JButton browseButton = new JButton("Browse Image");
        browseButton.addActionListener(e -> browseImage());

        JButton encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(e -> encryptImage());

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            imagePreviewLabel.setText("No image selected");
            imagePreviewLabel.setIcon(null);
            keyField.setText("");
            selectedImagePath = null;
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
                ImageIcon imageIcon = new ImageIcon(scaleImage(originalImage, 400, 300));
                imagePreviewLabel.setIcon(imageIcon);
                imagePreviewLabel.setText("");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading image!", "Error", JOptionPane.ERROR_MESSAGE);
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
            // Encrypt image data (XOR with key)
            byte[] keyBytes = key.getBytes();
            byte[] imageData = convertImageToBytes(originalImage);
            byte[] encryptedData = new byte[imageData.length];

            for (int i = 0; i < imageData.length; i++) {
                encryptedData[i] = (byte) (imageData[i] ^ keyBytes[i % keyBytes.length]);
            }

            // Save encrypted image
            BufferedImage encryptedImage = convertBytesToImage(encryptedData, originalImage.getWidth(), originalImage.getHeight());
            BufferedImage blurredImage = applyBlurEffect(encryptedImage, 90);

            String outputPath = selectedImagePath + "_encrypted.png";
            ImageIO.write(blurredImage, "png", new File(outputPath));

            imagePreviewLabel.setIcon(new ImageIcon(scaleImage(blurredImage, 400, 300)));

            JOptionPane.showMessageDialog(this, "Image encrypted and saved as: " + outputPath, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error during encryption: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private byte[] convertImageToBytes(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        byte[] data = new byte[width * height * 3];

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                data[index++] = (byte) ((rgb >> 16) & 0xFF);
                data[index++] = (byte) ((rgb >> 8) & 0xFF);
                data[index++] = (byte) (rgb & 0xFF);
            }
        }
        return data;
    }

    private BufferedImage convertBytesToImage(byte[] data, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = data[index++] & 0xFF;
                int g = data[index++] & 0xFF;
                int b = data[index++] & 0xFF;
                int rgb = (r << 16) | (g << 8) | b;
                image.setRGB(x, y, rgb);
            }
        }
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
        return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageEncryptor().setVisible(true));
    }
}
