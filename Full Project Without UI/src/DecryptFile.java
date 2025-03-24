import java.io.*;
import java.util.Scanner;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DecryptFile {
    public static void main(String[] CSECU) {
        try {
            Scanner scanner = new Scanner(System.in);
            
            // Ask for input file name
            System.out.print("Enter the encrypted file name (from output_files folder): ");
            String inputFileName = scanner.nextLine();
            
            // Ask for key file name
            System.out.print("Enter the key file name (from output_files folder, e.g., key_for_mytext.txt): ");
            String keyFileName = scanner.nextLine();
            
            // Ask for output file name
            System.out.print("Enter the name to save the decrypted file (in input_files folder): ");
            String outputFileName = scanner.nextLine();
            
            decryptFile("output_files/" + inputFileName, "output_files/" + keyFileName, "input_files/" + outputFileName);
            
            // Display success message
            System.out.println("\n==========================================");
            System.out.println("       File Successfully Decrypted!");
            System.out.println("==========================================");
            System.out.println("Your file has been decrypted and saved as: " + outputFileName);
            System.out.println("Location: input_files/" + outputFileName);
            System.out.println("\nPress Enter to return to Main Menu...");
            scanner.nextLine(); // Wait for user to press Enter
            
            // Return to MainMenu
            MainMenu.main(null);
            
        } catch (Exception myError) {
            System.out.println("Error occurred while decrypting file: " + myError.getMessage());
            myError.printStackTrace();
            
            // Return to MainMenu after error
            System.out.println("\nPress Enter to return to Main Menu...");
            new Scanner(System.in).nextLine(); // Wait for user to press Enter
            MainMenu.main(null);
        }
    }
    
    static void decryptFile(String inputFilePath, String keyFilePath, String outputFilePath) throws Exception {
        // Read the encryption key
        BufferedReader keyReader = new BufferedReader(new FileReader(keyFilePath));
        String encodedKey = keyReader.readLine();
        keyReader.close();
        
        // Decode the key
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        
        // Read the encrypted file
        BufferedReader fileReader = new BufferedReader(new FileReader(inputFilePath));
        String encryptedContent = fileReader.readLine();
        fileReader.close();
        
        // Decrypt the content
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedContent);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedContent = new String(decryptedBytes);
        
        // Write the decrypted content to the output file
        File outputDir = new File("input_files");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        
        FileWriter fileWriter = new FileWriter(outputFilePath);
        fileWriter.write(decryptedContent);
        fileWriter.close();

        System.out.println("Decrypted File Content:");
        System.out.println(decryptedContent);
    }
}
