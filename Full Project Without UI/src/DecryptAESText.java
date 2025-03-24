import java.util.Scanner;

public class DecryptAESText {
    public static void main(String[] CSECU) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the encrypted text: ");
        String myString = scanner.nextLine();
        decryptAESText(myString);
    }

    static void decryptAESText(String myString) {
        try {
            int[] ch2_array = new int[myString.length()];

            for (int a = 0; a < myString.length(); a++) {
                ch2_array[a] = (int) myString.charAt(a) - 4; // Decrypting by subtracting 4
            }

            System.out.print("Decrypted data: ");
            for (int a = 0; a < myString.length(); a++) {
                System.out.print((char) ch2_array[a]);
            }
            
            // Display success message
            System.out.println("\n\n==========================================");
            System.out.println("       Text Successfully Decrypted!");
            System.out.println("==========================================");
            System.out.println("\nPress Enter to return to Main Menu...");
            new Scanner(System.in).nextLine(); // Wait for user to press Enter
            
            // Return to MainMenu
            MainMenu.main(null);
            
        } catch (Exception myError) {
            System.out.println("Error occurred during decryption: " + myError.getMessage());
            
            // Return to MainMenu after error
            System.out.println("\nPress Enter to return to Main Menu...");
            try {
                new Scanner(System.in).nextLine(); // Wait for user to press Enter
                MainMenu.main(null);
            } catch (Exception e) {
                System.out.println("Error returning to main menu: " + e.getMessage());
            }
        }
    }
}
