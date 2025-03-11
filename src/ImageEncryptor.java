import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Scanner;

public class ImageEncryptor {
    public static void encryptImage(String imageName, String key) {
        try {
            // Define input and output paths
            String inputPath = "input_files/" + imageName;
            String outputPath = "output_files/" + imageName + ".enc";

            File inputFile = new File(inputPath);
            System.out.println("Looking for file at: " + inputFile.getAbsolutePath()); // Debugging

            if (!inputFile.exists()) {
                System.out.println("❌ Error: Image file not found in input_files folder!");
                
                // Return to MainMenu after error
                System.out.println("\nPress Enter to return to Main Menu...");
                new Scanner(System.in).nextLine(); // Wait for user to press Enter
                MainMenu.main(null);
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
            
            // Display success message
            System.out.println("\n==========================================");
            System.out.println("       Image Successfully Encrypted!");
            System.out.println("==========================================");
            System.out.println("✅ Image encrypted and saved as: " + outputPath);
            System.out.println("\nPress Enter to return to Main Menu...");
            new Scanner(System.in).nextLine(); // Wait for user to press Enter
            
            // Return to MainMenu
            MainMenu.main(null);
            
        } catch (Exception e) {
            System.out.println("❌ Error encrypting image: " + e.getMessage());
            
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
