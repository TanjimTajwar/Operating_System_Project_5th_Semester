import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;

public class DecryptFile extends JFrame {
    private JTextArea contentArea;
    private JTextField fileNameField;
    private JPasswordField keyField;
    private Color primaryColor = new Color(52, 152, 219);  // Modern blue
    private Color secondaryColor = new Color(46, 204, 113);  // Modern green
    private Color backgroundColor = new Color(236, 240, 241);  // Light gray background
    private Color textColor = new Color(44, 62, 80);  // Dark blue text
    private Color buttonHoverColor = new Color(41, 128, 185);  // Darker blue for hover

    public DecryptFile() {
        setTitle("Decrypt File");
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
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create title
        JLabel titleLabel = createStyledLabel("File Decryption", 24);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create center panel for content area
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            createStyledBorder(), "Decrypted Content", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), textColor));

        contentArea = new JTextArea();
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setBorder(null);
        centerPanel.add(contentScroll, BorderLayout.CENTER);

        // Create bottom panel for file name, key input and buttons
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel fileNameLabel = createStyledLabel("Encrypted File Name:", 14);
        fileNameField = createStyledTextField();
        fileNameField.setPreferredSize(new Dimension(200, 30));

        JLabel keyLabel = createStyledLabel("Decryption Key (16 chars):", 14);
        keyField = createStyledPasswordField();
        keyField.setPreferredSize(new Dimension(200, 30));

        JButton browseButton = createStyledButton("Browse");
        browseButton.addActionListener(e -> browseFile());

        JButton decryptButton = createStyledButton("Decrypt");
        decryptButton.addActionListener(e -> decryptFile());

        JButton clearButton = createStyledButton("Clear");
        clearButton.addActionListener(e -> {
            contentArea.setText("");
            fileNameField.setText("");
            keyField.setText("");
        });

        JButton backButton = createStyledButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            this.dispose();
            new MainMenu().setVisible(true);
        });

        gbc.gridx = 0; gbc.gridy = 0;
        bottomPanel.add(fileNameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        bottomPanel.add(fileNameField, gbc);
        gbc.gridx = 2; gbc.gridy = 0;
        bottomPanel.add(browseButton, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        bottomPanel.add(keyLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        bottomPanel.add(keyField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        bottomPanel.add(decryptButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 3;
        bottomPanel.add(clearButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 3;
        bottomPanel.add(backButton, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createStyledLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        label.setForeground(textColor);
        return label;
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

    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Encrypted File to Decrypt");
        
        // Add file filter for encrypted files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".enc");
            }
            public String getDescription() {
                return "Encrypted files (*.enc)";
            }
        });
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileNameField.setText(selectedFile.getName());
        }
    }

    private void decryptFile() {
        String fileName = fileNameField.getText();
        if (fileName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select an encrypted file to decrypt!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String key = new String(keyField.getPassword());
        if (key.length() != 16) {
            JOptionPane.showMessageDialog(this,
                "Key must be exactly 16 characters!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Read encrypted file
            File inputFile = new File("output_files/" + fileName);
            if (!inputFile.exists()) {
                JOptionPane.showMessageDialog(this,
                    "Encrypted file not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] encryptedData = readFile(inputFile);
            byte[] keyBytes = key.getBytes();
            byte[] decryptedBytes = new byte[encryptedData.length];
            
            // Decrypt the data
            for (int i = 0; i < encryptedData.length; i++) {
                decryptedBytes[i] = (byte) (encryptedData[i] ^ keyBytes[i % keyBytes.length]);
            }
            
            // Convert decrypted bytes to string
            String decryptedText = new String(decryptedBytes);
            contentArea.setText(decryptedText);
            
            JOptionPane.showMessageDialog(this,
                "File successfully decrypted!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error during decryption: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private byte[] readFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return data;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DecryptFile().setVisible(true);
        });
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
