import java.io.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncryptFile {
    public static void main(String[] CSECU) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the file name (e.g., mytext.txt): ");
            String fileName = reader.readLine();
            encryptFile("input_files/" + fileName);
            
            // Display success message
            System.out.println("\n==========================================");
            System.out.println("       File Successfully Encrypted!");
            System.out.println("==========================================");
            System.out.println("Your file has been encrypted and saved in output_files folder");
            System.out.println("Don't forget to keep your key file safe for decryption!");
            System.out.println("\nPress Enter to return to Main Menu...");
            reader.readLine(); // Wait for user to press Enter
            
            // Return to MainMenu
            MainMenu.main(null);
            
        } catch (Exception myError) {
            System.out.println("❌ Error occurred while encrypting file: " + myError.getMessage());
            myError.printStackTrace();
            
            // Return to MainMenu after error
            System.out.println("\nPress Enter to return to Main Menu...");
            try {
                new BufferedReader(new InputStreamReader(System.in)).readLine(); // Wait for user to press Enter
                MainMenu.main(null);
            } catch (IOException e) {
                System.out.println("Error returning to main menu: " + e.getMessage());
            }
        }
    }

    static void encryptFile(String fileName) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Extract original file name without path
        String originalFileName = new File(fileName).getName();
        String encryptedFileName = "Encrypted(" + originalFileName + ")";

        FileReader file_reading = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(file_reading);
        StringBuilder content = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();

        byte[] encryptedBytes = cipher.doFinal(content.toString().getBytes());
        String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);

        // Ensure output directory exists
        File outputDir = new File("output_files");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        FileWriter file_writting = new FileWriter("output_files/" + encryptedFileName);
        file_writting.write(encryptedText);
        file_writting.close();

        // Save the secret key for later decryption
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        FileWriter keyWriter = new FileWriter("output_files/key_for_" + originalFileName);
        keyWriter.write(encodedKey);
        keyWriter.close();

        System.out.println("File encrypted successfully and saved in output_files/" + encryptedFileName);
        System.out.println("Encryption key saved in output_files/key_for_" + originalFileName);
    }
}
