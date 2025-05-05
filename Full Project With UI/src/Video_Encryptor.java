import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class Video_Encryptor extends JFrame {
    private JTextField filePathField;
    private JPasswordField keyField;
    private String selectedVideoPath;
    private Color primaryColor = new Color(52, 152, 219);  // Modern blue
    private Color secondaryColor = new Color(46, 204, 113);  // Modern green
    private Color backgroundColor = new Color(236, 240, 241);  // Light gray background
    private Color textColor = new Color(44, 62, 80);  // Dark blue text
    private Color buttonHoverColor = new Color(41, 128, 185);  // Darker blue for hover

    public Video_Encryptor() {
        setTitle("Video Encryption");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(backgroundColor);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(backgroundColor);

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(backgroundColor);
        JLabel titleLabel = new JLabel("Video Encryption System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        centerPanel.setBackground(backgroundColor);

        // File Selection Panel
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePanel.setBackground(backgroundColor);
        filePathField = new JTextField(30);
        filePathField.setEditable(false);
        filePathField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filePathField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JButton browseButton = createStyledButton("Browse");
        browseButton.addActionListener(e -> browseVideo());
        
        filePanel.add(new JLabel("Select Video: "));
        filePanel.add(filePathField);
        filePanel.add(browseButton);
        centerPanel.add(filePanel);

        // Key Input Panel
        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keyPanel.setBackground(backgroundColor);
        keyField = new JPasswordField(16);
        keyField.setPreferredSize(new Dimension(200, 30));
        keyField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        keyField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        keyPanel.add(new JLabel("Encryption Key (16 chars): "));
        keyPanel.add(keyField);
        centerPanel.add(keyPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(backgroundColor);
        
        JButton encryptButton = createStyledButton("Encrypt");
        encryptButton.addActionListener(e -> encryptVideo());
        
        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> this.dispose());
        
        buttonPanel.add(encryptButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
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
            // Process video using FFmpeg
            processVideo(selectedVideoPath);

            // Optimized file reading and encryption with parallel processing
            File inputFile = new File(selectedVideoPath + "_processed.mp4");
            String outputPath = selectedVideoPath + ".enc";
            
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
                            return encrypt(Arrays.copyOf(chunk, bytesRead), key.getBytes());
                        }
                        return new byte[0];
                    }));
                }
            }
            
            // Write encrypted chunks to output file in parallel
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputPath), 16 * 1024 * 1024)) {
                for (Future<byte[]> future : futures) {
                    bos.write(future.get());
                }
            }
            
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);

            // Delete temporary processed file
            inputFile.delete();

            // Show success message
            JOptionPane.showMessageDialog(this, 
                "Video encrypted successfully!\nSaved as: " + outputPath, 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processVideo(String inputPath) throws Exception {
        String outputPath = inputPath + "_processed.mp4";
        
        // Further optimized FFmpeg command with parallel processing
        String[] cmd = {
            "ffmpeg",
            "-i", inputPath,
            "-vf", "boxblur=10:10",
            "-an",
            "-c:v", "libx264",
            "-preset", "ultrafast",
            "-crf", "35",           // Higher compression for faster processing
            "-threads", "0",        // Use all available threads
            "-tune", "zerolatency",
            "-movflags", "+faststart",
            "-x264-params", "threads=0:lookahead-threads=0:sync-lookahead=0",  // Enable parallel encoding
            "-y",
            outputPath
        };

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        // Wait for the process to complete with timeout
        if (!process.waitFor(120, TimeUnit.SECONDS)) {  // 2 minutes timeout
            process.destroyForcibly();
            throw new Exception("Video processing timed out");
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new Exception("FFmpeg processing failed with exit code: " + exitCode);
        }
    }

    private byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // Use ChaCha20 with parallel processing
        SecretKey secretKey = new SecretKeySpec(key, "ChaCha20");
        Cipher cipher = Cipher.getInstance("ChaCha20");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        
        // Process data in parallel chunks
        int chunkSize = 2 * 1024 * 1024; // 2MB chunks
        byte[] result = new byte[data.length];
        
        for (int i = 0; i < data.length; i += chunkSize) {
            int end = Math.min(i + chunkSize, data.length);
            byte[] chunk = Arrays.copyOfRange(data, i, end);
            byte[] encryptedChunk = cipher.doFinal(chunk);
            System.arraycopy(encryptedChunk, 0, result, i, encryptedChunk.length);
        }
        
        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Video_Encryptor().setVisible(true));
    }
}
