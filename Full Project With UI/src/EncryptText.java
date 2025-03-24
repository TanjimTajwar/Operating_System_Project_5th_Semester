import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class EncryptText extends JFrame {
    private JTextArea inputArea;
    private JTextArea outputArea;
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 152, 219);
    private Color backgroundColor = new Color(0, 0, 255);
    private Color textColor = new Color(0, 0, 0);

    public EncryptText() {
        setTitle("Text Encryption");
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
        JLabel titleLabel = createStyledLabel("Text Encryption", 24);
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

        inputArea = new JTextArea();
        inputArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(null);
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        // Output area
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBackground(backgroundColor);
        outputPanel.setBorder(BorderFactory.createTitledBorder(
            createStyledBorder(), "Encrypted Text", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), textColor));

        outputArea = new JTextArea();
        outputArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(null);
        outputPanel.add(outputScroll, BorderLayout.CENTER);

        centerPanel.add(inputPanel);
        centerPanel.add(outputPanel);

        // Create bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(backgroundColor);

        JButton encryptButton = createStyledButton("Encrypt");
        encryptButton.addActionListener(e -> encryptText());

        JButton clearButton = createStyledButton("Clear");
        clearButton.addActionListener(e -> {
            inputArea.setText("");
            outputArea.setText("");
        });

        JButton backButton = createStyledButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            this.dispose();
            new MainMenu().setVisible(true);
        });

        bottomPanel.add(encryptButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(backButton);

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

    private Border createStyledBorder() {
        return BorderFactory.createCompoundBorder(
            new RoundedBorder(10),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
    }

    private void encryptText() {
        String input = inputArea.getText();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter some text to encrypt!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            StringBuilder stringBuilder = new StringBuilder(input);
            int[] arr1 = new int[stringBuilder.length()];
            int[] arr2 = new int[stringBuilder.length()];
            char[] mystr2 = new char[stringBuilder.length()];

            for (int a = 0; a < stringBuilder.length(); a++) {
                arr1[a] = (int) stringBuilder.charAt(a);
                arr2[a] = arr1[a] + 4;
            }

            StringBuilder encryptedText = new StringBuilder();
            for (int a = 0; a < stringBuilder.length(); a++) {
                mystr2[a] = (char) arr2[a];
                encryptedText.append(mystr2[a]);
            }
            
            outputArea.setText(encryptedText.toString());
            
            JOptionPane.showMessageDialog(this,
                "Text successfully encrypted!",
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
            new EncryptText().setVisible(true);
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
