import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;

public class EncryptFile extends JFrame {
    private JTextArea contentArea;
    private JTextField fileNameField;
    private JPasswordField keyField;
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 152, 219);
    private Color backgroundColor = new Color(0, 0, 255);
    private Color textColor = new Color(0, 0, 0);

    public EncryptFile() {
        setTitle("Encrypt File");
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
        JLabel titleLabel = createStyledLabel("File Encryption", 24);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create center panel for content area
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            createStyledBorder(), "File Content", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), textColor));

        contentArea = new JTextArea();
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setBorder(null);
        centerPanel.add(contentScroll, BorderLayout.CENTER);

        // Create bottom panel for file name, key input and buttons
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel fileNameLabel = createStyledLabel("File Name:", 14);
        fileNameField = createStyledTextField();
        fileNameField.setPreferredSize(new Dimension(200, 30));

        JLabel keyLabel = createStyledLabel("Encryption Key (16 chars):", 14);
        keyField = createStyledPasswordField();
        keyField.setPreferredSize(new Dimension(200, 30));

        JButton browseButton = createStyledButton("Browse");
        browseButton.addActionListener(e -> browseFile());

        JButton encryptButton = createStyledButton("Encrypt");
        encryptButton.addActionListener(e -> encryptFile());

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
        bottomPanel.add(encryptButton, gbc);

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

    private JLabel createStyledLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, size));
        label.setForeground(textColor);
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 35));
        button.setBackground(primaryColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(15));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(secondaryColor);
                button.repaint();
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(primaryColor);
                button.repaint();
            }
        });
        
        return button;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(new RoundedBorder(10));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(new RoundedBorder(10));
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
        fileChooser.setDialogTitle("Select File to Encrypt");
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileNameField.setText(selectedFile.getName());
            
            try {
                String content = readFile(selectedFile);
                contentArea.setText(content);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error reading file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String readFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    private void encryptFile() {
        String fileName = fileNameField.getText();
        if (fileName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a file name!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String content = contentArea.getText();
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter some content to encrypt!",
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
            // Encrypt the content
            byte[] contentBytes = content.getBytes();
            byte[] keyBytes = key.getBytes();
            byte[] encryptedBytes = new byte[contentBytes.length];
            
            for (int i = 0; i < contentBytes.length; i++) {
                encryptedBytes[i] = (byte) (contentBytes[i] ^ keyBytes[i % keyBytes.length]);
            }
            
            // Save encrypted file
            String outputPath = "output_files/" + fileName + ".enc";
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                fos.write(encryptedBytes);
            }
            
            JOptionPane.showMessageDialog(this,
                "File encrypted and saved as '" + outputPath + "'",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error during encryption: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EncryptFile().setVisible(true);
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
