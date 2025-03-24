import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ImageDecryptor extends JFrame {
    private JLabel imagePreviewLabel;
    private JPasswordField keyField;
    private String selectedImagePath;
    
    public ImageDecryptor() {
        setTitle("Image Decryption");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Image Decryption", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        imagePreviewLabel = new JLabel("No image selected", SwingConstants.CENTER);
        imagePreviewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Image Preview"));
        centerPanel.add(imagePreviewLabel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout());
        keyField = new JPasswordField(16);
        JButton browseButton = new JButton("Browse Image");
        JButton decryptButton = new JButton("Decrypt");
        JButton clearButton = new JButton("Clear");
        
        browseButton.addActionListener(e -> browseImage());
        decryptButton.addActionListener(e -> decryptImage());
        clearButton.addActionListener(e -> {
            imagePreviewLabel.setText("No image selected");
            imagePreviewLabel.setIcon(null);
            keyField.setText("");
            selectedImagePath = null;
        });
        
        bottomPanel.add(new JLabel("Decryption Key (16 chars):"));
        bottomPanel.add(keyField);
        bottomPanel.add(browseButton);
        bottomPanel.add(decryptButton);
        bottomPanel.add(clearButton);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".enc");
            }
            public String getDescription() {
                return "Encrypted image files (*.enc)";
            }
        });
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedImagePath = fileChooser.getSelectedFile().getAbsolutePath();
            imagePreviewLabel.setText("Encrypted image selected");
        }
    }

    private void decryptImage() {
        if (selectedImagePath == null) {
            JOptionPane.showMessageDialog(this, "Please select an encrypted image!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String key = new String(keyField.getPassword());
        if (key.length() != 16) {
            JOptionPane.showMessageDialog(this, "Key must be 16 characters!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            File inputFile = new File(selectedImagePath);
            byte[] encryptedData = readFile(inputFile);
            byte[] decryptedData = decrypt(encryptedData, key.getBytes());
            String outputPath = selectedImagePath.replace(".enc", "_decrypted.jpg");
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                fos.write(decryptedData);
            }
            imagePreviewLabel.setIcon(new ImageIcon(outputPath));
            imagePreviewLabel.setText("");
            JOptionPane.showMessageDialog(this, "Decryption successful! Saved as " + outputPath, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Decryption failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private byte[] readFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return data;
        }
    }

    private byte[] decrypt(byte[] data, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageDecryptor().setVisible(true));
    }
}
