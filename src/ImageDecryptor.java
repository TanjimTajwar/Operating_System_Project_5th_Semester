import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ImageDecryptor {
    public static void decryptImage(String encryptedImageName, String key, String newImageName) {
        try {
            // Define input and output paths
            String inputPath = encryptedImageName;
            String outputPath = newImageName;

            File encryptedFile = new File(inputPath);
            System.out.println("Looking for encrypted file at: " + encryptedFile.getAbsolutePath()); // Debugging

            if (!encryptedFile.exists()) {
                System.out.println("❌ Error: Encrypted file not found in output_files folder!");
                return;
            }

            // Read encrypted bytes
            byte[] encryptedBytes = Files.readAllBytes(encryptedFile.toPath());

            // Perform AES decryption
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Save decrypted file
            Files.write(Paths.get(outputPath), decryptedBytes);
            System.out.println("✅ Image decrypted and saved as: " + outputPath);
        } catch (Exception e) {
            System.out.println("❌ Error decrypting image: " + e.getMessage());
        }
    }
}
