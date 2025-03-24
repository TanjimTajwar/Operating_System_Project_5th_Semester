import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class MainMenu extends JFrame {
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 152, 219);
    private Color backgroundColor = new Color(0, 0, 139);
    private Color textColor = new Color(0, 0, 0);
    private JPanel mainPanel;
    private JPanel centerPanel;

    public MainMenu() {
        setTitle("File Encryption System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = createStyledLabel("File Encryption System", 32);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(backgroundColor);
        
        showMainOptions();

        add(mainPanel);
    }

    private void showMainOptions() {
        centerPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton encryptButton = createStyledButton("Encrypt");
        encryptButton.addActionListener(e -> showFileTypeOptions("encrypt"));

        JButton decryptButton = createStyledButton("Decrypt");
        decryptButton.addActionListener(e -> showFileTypeOptions("decrypt"));

        JButton exitButton = createStyledButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        centerPanel.add(encryptButton, gbc);
        centerPanel.add(decryptButton, gbc);
        centerPanel.add(exitButton, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showFileTypeOptions(String operation) {
        centerPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton textButton = createStyledButton("Text");
        textButton.addActionListener(e -> {
            if (operation.equals("encrypt")) {
                new EncryptText().setVisible(true);
            } else {
                new DecryptAESText().setVisible(true);
            }
        });

        JButton fileButton = createStyledButton("File");
        fileButton.addActionListener(e -> {
            if (operation.equals("encrypt")) {
                new EncryptFile().setVisible(true);
            } else {
                new DecryptFile().setVisible(true);
            }
        });

        JButton imageButton = createStyledButton("Image");
        imageButton.addActionListener(e -> {
            if (operation.equals("encrypt")) {
                new ImageEncryptor().setVisible(true);
            } else {
                new ImageDecryptor().setVisible(true);
            }
        });
        
        JButton videoButton = createStyledButton("Video");
        videoButton.addActionListener(e -> {
            if (operation.equals("encrypt")) {
                new Video_Encryptor().setVisible(true);
            } else {
                new Video_Decryptor().setVisible(true);
            }
        });

        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> showMainOptions());

        centerPanel.add(textButton, gbc);
        centerPanel.add(fileButton, gbc);
        centerPanel.add(imageButton, gbc);
        centerPanel.add(videoButton, gbc);
        centerPanel.add(backButton, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JLabel createStyledLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, size));
        label.setForeground(textColor);
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 45));
        button.setBackground(primaryColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(15));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
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
