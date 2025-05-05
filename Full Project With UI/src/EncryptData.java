import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;

public class EncryptData extends JFrame {
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JPasswordField keyField;
    private Color primaryColor = new Color(52, 152, 219);  // Modern blue
    private Color secondaryColor = new Color(46, 204, 113);  // Modern green
    private Color backgroundColor = new Color(236, 240, 241);  // Light gray background
    private Color textColor = new Color(44, 62, 80);  // Dark blue text
    private Color buttonHoverColor = new Color(41, 128, 185);  // Darker blue for hover

    public EncryptData() {
        setTitle("Encrypt Data");
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
        JLabel titleLabel = createStyledLabel("Data Encryption", 24);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create center panel for text areas
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBackground(backgroundColor);

        // Input area
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(backgroundColor);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            createStyledBorder(), "Input Text", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), textColor));

        inputArea = createStyledTextArea();
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(null);
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        // Output area
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBackground(backgroundColor);
        outputPanel.setBorder(BorderFactory.createTitledBorder(
            createStyledBorder(), "Encrypted Text", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), textColor));

        outputArea = createStyledTextArea();
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(null);
        outputPanel.add(outputScroll, BorderLayout.CENTER);

        centerPanel.add(inputPanel);
        centerPanel.add(outputPanel);

        // Create bottom panel for key input and buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(backgroundColor);

        JLabel keyLabel = createStyledLabel("Encryption Key (16 chars):", 14);
        keyField = createStyledPasswordField();
        keyField.setPreferredSize(new Dimension(200, 30));

        JButton encryptButton = createStyledButton("Encrypt");
        encryptButton.addActionListener(e -> encryptData());

        JButton clearButton = createStyledButton("Clear");
        clearButton.addActionListener(e -> {
            inputArea.setText("");
            outputArea.setText("");
            keyField.setText("");
        });

        bottomPanel.add(keyLabel);
        bottomPanel.add(keyField);
        bottomPanel.add(encryptButton);
        bottomPanel.add(clearButton);

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

    private JTextArea createStyledTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(new RoundedBorder(10));
        return textArea;
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

    private void encryptData() {
        String input = inputArea.getText();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter some text to encrypt!",
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
            // Encrypt the input text
            byte[] inputBytes = input.getBytes();
            byte[] keyBytes = key.getBytes();
            byte[] encryptedBytes = new byte[inputBytes.length];
            
            for (int i = 0; i < inputBytes.length; i++) {
                encryptedBytes[i] = (byte) (inputBytes[i] ^ keyBytes[i % keyBytes.length]);
            }
            
            // Convert encrypted bytes to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encryptedBytes) {
                hexString.append(String.format("%02X", b));
            }
            
            // Display encrypted text
            outputArea.setText(hexString.toString());
            
            // Save encrypted data to file
            try (FileOutputStream fos = new FileOutputStream("encrypted_data.txt")) {
                fos.write(encryptedBytes);
            }
            
            JOptionPane.showMessageDialog(this,
                "Data encrypted and saved to 'encrypted_data.txt'",
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
            new EncryptData().setVisible(true);
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
