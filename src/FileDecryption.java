import java.io.*;
import java.util.Scanner;

public class FileDecryption {
    public static void main(String[] CSECU) {
        Scanner scanner = new Scanner(System.in);

        while (true) { // Infinite loop to keep the program running until user exits
            // Display menu options
            System.out.println("\nChoose an option:");
            System.out.println("1. Text Decryption");
            System.out.println("2. File Decryption");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1/2/3): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    // Text Decryption
                    System.out.print("\nEnter the encrypted text: ");
                    String encryptedText = scanner.nextLine();
                    decryptAESText(encryptedText);
                    break;

                case 2:
                    // File Decryption
                    System.out.print("\nEnter the file name (with .txt extension): ");
                    String fileName = scanner.nextLine();
                    decryptFile(fileName);
                    break;

                case 3:
                    // Exit the program
                    System.out.println("\nExiting the program. Goodbye!");
                    scanner.close();
                    System.exit(0); // Terminate the program

                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    // Function to decrypt a text string
    static void decryptAESText(String encryptedText) {
        try {
            StringBuilder decryptedData = new StringBuilder();

            for (int a = 0; a < encryptedText.length(); a++) {
                decryptedData.append((char) (encryptedText.charAt(a) - 4)); // Decrypting
            }

            System.out.println("\nDecrypted Text: " + decryptedData);
            System.out.println("\nDecryption complete.");
        } catch (Exception e) {
            System.out.println("Error occurred during text decryption.");
        }
    }

    // Function to decrypt a file
    static void decryptFile(String fileName) {
        try (FileReader fileReader = new FileReader(fileName)) {
            StringBuilder content = new StringBuilder();
            int info;

            while ((info = fileReader.read()) != -1) {
                content.append((char) (info - 4)); // Decrypting
            }

            System.out.println("\nDecrypted File Content:");
            System.out.println(content);
            System.out.println("\nDecryption complete.");
        } catch (IOException e) {
            System.out.println("Error occurred while reading the file: " + e.getMessage());
        }
    }
}
