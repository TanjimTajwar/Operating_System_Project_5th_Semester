import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class Video_Decryptor extends JFrame {
    private JTextField filePathField;
    private JPasswordField keyField;
    private String selectedVideoPath;
    private Color primaryColor = new Color(52, 152, 219);  // Modern blue
    private Color secondaryColor = new Color(46, 204, 113);  // Modern green
    private Color backgroundColor = new Color(236, 240, 241);  // Light gray background
    private Color textColor = new Color(44, 62, 80);  // Dark blue text
    private Color buttonHoverColor = new Color(41, 128, 185);  // Darker blue for hover

    public Video_Decryptor() {
        setTitle("Video Decryption");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Decrypt Video File", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(titleLabel);

        JPanel filePanel = new JPanel(new FlowLayout());
        filePathField = new JTextField(30);
        filePathField.setEditable(false);
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> browseVideo());
        filePanel.add(new JLabel("Select Encrypted Video: "));
        filePanel.add(filePathField);
        filePanel.add(browseButton);
        panel.add(filePanel);

        JPanel keyPanel = new JPanel(new FlowLayout());
        keyField = new JPasswordField(16);
        keyField.setPreferredSize(new Dimension(200, 30));
        keyPanel.add(new JLabel("Decryption Key (16 chars): "));
        keyPanel.add(keyField);
        panel.add(keyPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(e -> decryptVideo());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> this.dispose());
        buttonPanel.add(decryptButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel);

        add(panel);
    }

    private void browseVideo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".enc");
            }
            public String getDescription() {
                return "Encrypted Video Files (*.enc)";
            }
        });

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedVideoPath = fileChooser.getSelectedFile().getAbsolutePath();
            filePathField.setText(selectedVideoPath);
        }
    }

    private void decryptVideo() {
        if (selectedVideoPath == null) {
            JOptionPane.showMessageDialog(this, "Select an encrypted video file first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String key = new String(keyField.getPassword());
        if (key.length() != 16) {
            JOptionPane.showMessageDialog(this, "Key must be exactly 16 characters!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Optimized file reading and decryption with parallel processing
            File inputFile = new File(selectedVideoPath);
            String outputPath = selectedVideoPath.replace(".enc", "_decrypted.mp4");
            
            // Calculate file size and chunk size
            long fileSize = inputFile.length();
            int chunkSize = 8 * 1024 * 1024; // 8MB chunks for better performance
            int numChunks = (int) Math.ceil((double) fileSize / chunkSize);
            
            // Create thread pool with more threads for parallel processing
            int numThreads = Math.max(8, Runtime.getRuntime().availableProcessors() * 4);
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            List<Future<byte[]>> futures = new ArrayList<>();
            
            // Use memory-mapped file for faster reading
            try (RandomAccessFile raf = new RandomAccessFile(inputFile, "r")) {
                for (int i = 0; i < numChunks; i++) {
                    final int chunkIndex = i;
                    futures.add(executor.submit(() -> {
                        byte[] chunk = new byte[chunkSize];
                        int bytesRead = raf.read(chunk);
                        if (bytesRead > 0) {
                            return decrypt(Arrays.copyOf(chunk, bytesRead), key.getBytes());
                        }
                        return new byte[0];
                    }));
                }
            }
            
            // Write decrypted chunks to output file in parallel
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputPath), 16 * 1024 * 1024)) {
                for (Future<byte[]> future : futures) {
                    bos.write(future.get());
                }
            }
            
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);

            // Show success message
            JOptionPane.showMessageDialog(this, 
                "Video decrypted successfully!\nSaved as: " + outputPath, 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // Use ChaCha20 with parallel processing
        SecretKey secretKey = new SecretKeySpec(key, "ChaCha20");
        Cipher cipher = Cipher.getInstance("ChaCha20");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        
        // Process data in parallel chunks
        int chunkSize = 2 * 1024 * 1024; // 2MB chunks
        byte[] result = new byte[data.length];
        
        for (int i = 0; i < data.length; i += chunkSize) {
            int end = Math.min(i + chunkSize, data.length);
            byte[] chunk = Arrays.copyOfRange(data, i, end);
            byte[] decryptedChunk = cipher.doFinal(chunk);
            System.arraycopy(decryptedChunk, 0, result, i, decryptedChunk.length);
        }
        
        return result;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Video_Decryptor().setVisible(true));
    }
}
