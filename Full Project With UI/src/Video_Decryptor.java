import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import java.nio.file.Files;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Video_Decryptor extends JFrame {
    private JTextField filePathField;
    private JPasswordField keyField;
    private String selectedVideoPath;

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
            byte[] encryptedData = Files.readAllBytes(new File(selectedVideoPath).toPath());
            byte[] decryptedData = decrypt(encryptedData, key.getBytes());

            String outputPath = selectedVideoPath.replace(".enc", "_decrypted.mp4");
            Files.write(new File(outputPath).toPath(), decryptedData);

            JOptionPane.showMessageDialog(this, "Video decrypted successfully!\nSaved as: " + outputPath, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private byte[] decrypt(byte[] data, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Video_Decryptor().setVisible(true));
    }
}
