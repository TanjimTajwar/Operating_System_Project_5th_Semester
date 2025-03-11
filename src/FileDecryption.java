import java.io.*;
import java.util.Scanner;

public class FileDecryption {
    public static void main(String[] CSECU) {
        Scanner scanner = new Scanner(System.in);

        // Display menu options
        System.out.println("\nChoose an option:");
        System.out.println("1. Text Decryption");
        System.out.println("2. File Decryption");
        System.out.println("3. Return to Main Menu");
        System.out.print("Enter your choice (1/2/3): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1:
                // Text Decryption
                System.out.print("\nEnter the encrypted text: ");
                String encryptedText = scanner.nextLine();
                decryptAESText(encryptedText, scanner);
                break;

            case 2:
                // File Decryption
                System.out.print("\nEnter the file name (with .txt extension): ");
                String fileName = scanner.nextLine();
                decryptFile(fileName, scanner);
                break;

            case 3:
                // Return to Main Menu
                System.out.println("\nReturning to Main Menu...");
                MainMenu.main(null);
                break;

            default:
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                System.out.println("\nPress Enter to try again...");
                scanner.nextLine(); // Wait for user to press Enter
                main(null); // Restart this menu
        }
    }

    // Function to decrypt a text string
    static void decryptAESText(String encryptedText, Scanner scanner) {
        try {
            StringBuilder decryptedData = new StringBuilder();

            for (int a = 0; a < encryptedText.length(); a++) {
                decryptedData.append((char) (encryptedText.charAt(a) - 4)); // Decrypting
            }

            System.out.println("\nDecrypted Text: " + decryptedData);
            
            // Display success message
            System.out.println("\n==========================================");
            System.out.println("       Text Successfully Decrypted!");
            System.out.println("==========================================");
            System.out.println("\nPress Enter to return to Main Menu...");
            scanner.nextLine(); // Wait for user to press Enter
            
            // Return to MainMenu
            MainMenu.main(null);
            
        } catch (Exception e) {
            System.out.println("Error occurred during text decryption: " + e.getMessage());
            
            // Return to MainMenu after error
            System.out.println("\nPress Enter to return to Main Menu...");
            scanner.nextLine(); // Wait for user to press Enter
            MainMenu.main(null);
        }
    }

    // Function to decrypt a file
    static void decryptFile(String fileName, Scanner scanner) {
        try (FileReader fileReader = new FileReader(fileName)) {
            StringBuilder content = new StringBuilder();
            int info;

            while ((info = fileReader.read()) != -1) {
                content.append((char) (info - 4)); // Decrypting
            }

            System.out.println("\nDecrypted File Content:");
            System.out.println(content);
            
            // Display success message
            System.out.println("\n==========================================");
            System.out.println("       File Successfully Decrypted!");
            System.out.println("==========================================");
            System.out.println("\nPress Enter to return to Main Menu...");
            scanner.nextLine(); // Wait for user to press Enter
            
            // Return to MainMenu
            MainMenu.main(null);
            
        } catch (IOException e) {
            System.out.println("Error occurred while reading the file: " + e.getMessage());
            
            // Return to MainMenu after error
            System.out.println("\nPress Enter to return to Main Menu...");
            scanner.nextLine(); // Wait for user to press Enter
            MainMenu.main(null);
        }
    }
}
