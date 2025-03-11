import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Scanner;

public class ImageDecryptor {
    public static void decryptImage(String encryptedImageName, String key, String newImageName) {
        try {
            // Define input and output paths
            String inputPath = "output_files/" + encryptedImageName;
            String outputPath = "input_files/" + newImageName;

            File encryptedFile = new File(inputPath);
            System.out.println("Looking for encrypted file at: " + encryptedFile.getAbsolutePath()); // Debugging

            if (!encryptedFile.exists()) {
                System.out.println("❌ Error: Encrypted file not found in output_files folder!");
                
                // Return to MainMenu after error
                System.out.println("\nPress Enter to return to Main Menu...");
                new Scanner(System.in).nextLine(); // Wait for user to press Enter
                MainMenu.main(null);
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
            
            // Display success message
            System.out.println("\n==========================================");
            System.out.println("       Image Successfully Decrypted!");
            System.out.println("==========================================");
            System.out.println("✅ Image decrypted and saved as: " + outputPath);
            System.out.println("\nPress Enter to return to Main Menu...");
            new Scanner(System.in).nextLine(); // Wait for user to press Enter
            
            // Return to MainMenu
            MainMenu.main(null);
            
        } catch (Exception e) {
            System.out.println("❌ Error decrypting image: " + e.getMessage());
            
            // Return to MainMenu after error
            System.out.println("\nPress Enter to return to Main Menu...");
            try {
                new Scanner(System.in).nextLine(); // Wait for user to press Enter
                MainMenu.main(null);
            } catch (Exception ex) {
                System.out.println("Error returning to main menu: " + ex.getMessage());
            }
        }
    }
}
