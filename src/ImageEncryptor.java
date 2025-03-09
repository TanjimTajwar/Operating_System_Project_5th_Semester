import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ImageEncryptor {
    public static void encryptImage(String imageName, String key) {
        try {
            // Define input and output paths
            String inputPath = imageName;
            String outputPath = imageName + ".enc";

            File inputFile = new File(inputPath);
            System.out.println("Looking for file at: " + inputFile.getAbsolutePath()); // Debugging

            if (!inputFile.exists()) {
                System.out.println("❌ Error: Image file not found in input_files folder!");
                return;
            }

            // Read image bytes
            byte[] imageBytes = Files.readAllBytes(inputFile.toPath());

            // Perform AES encryption
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(imageBytes);

            // Save encrypted file
            Files.write(Paths.get(outputPath), encryptedBytes);
            System.out.println("✅ Image encrypted and saved as: " + outputPath);
        } catch (Exception e) {
            System.out.println("❌ Error encrypting image: " + e.getMessage());
        }
    }
}
