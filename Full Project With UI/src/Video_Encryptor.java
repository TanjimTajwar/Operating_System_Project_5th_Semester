import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;

public class Video_Encryptor extends JFrame {
    private JTextField filePathField;
    private JPasswordField keyField;
    private String selectedVideoPath;

    public Video_Encryptor() {
        setTitle("Video Encryption");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Encrypt Video File", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(titleLabel);

        JPanel filePanel = new JPanel(new FlowLayout());
        filePathField = new JTextField(30);
        filePathField.setEditable(false);
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> browseVideo());
        filePanel.add(new JLabel("Select Video: "));
        filePanel.add(filePathField);
        filePanel.add(browseButton);
        panel.add(filePanel);

        JPanel keyPanel = new JPanel(new FlowLayout());
        keyField = new JPasswordField(16);
        keyField.setPreferredSize(new Dimension(200, 30));
        keyPanel.add(new JLabel("Encryption Key (16 chars): "));
        keyPanel.add(keyField);
        panel.add(keyPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(e -> encryptVideo());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> this.dispose());
        buttonPanel.add(encryptButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel);

        add(panel);
    }

    private void browseVideo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".mp4");
            }
            public String getDescription() {
                return "Video Files (*.mp4)";
            }
        });

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedVideoPath = fileChooser.getSelectedFile().getAbsolutePath();
            filePathField.setText(selectedVideoPath);
        }
    }

    private void encryptVideo() {
        if (selectedVideoPath == null) {
            JOptionPane.showMessageDialog(this, "Select a video file first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String key = new String(keyField.getPassword());
        if (key.length() != 16) {
            JOptionPane.showMessageDialog(this, "Key must be exactly 16 characters!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            byte[] fileData = Files.readAllBytes(new File(selectedVideoPath).toPath());
            byte[] encryptedData = encrypt(fileData, key.getBytes());

            String outputPath = selectedVideoPath + ".enc";
            Files.write(new File(outputPath).toPath(), encryptedData);

            JOptionPane.showMessageDialog(this, "Video encrypted successfully!\nSaved as: " + outputPath, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private byte[] encrypt(byte[] data, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Video_Encryptor().setVisible(true));
    }
}
