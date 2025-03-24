import java.util.Scanner;

public class EncryptText {
    public static void main(String[] CSECU) {
        encryptText();
    }

    static void encryptText() {
        try {
            Scanner CU_Scanner = new Scanner(System.in);
            System.out.print("\nEnter text to encrypt: ");
            String myString = CU_Scanner.nextLine();

            StringBuilder string_builder = new StringBuilder(myString);
            int[] arr1 = new int[string_builder.length()];
            int[] arr2 = new int[string_builder.length()];
            char[] mystr2 = new char[string_builder.length()];

            for (int a = 0; a < string_builder.length(); a++) {
                arr1[a] = (int) string_builder.charAt(a);
                arr2[a] = arr1[a] + 4;
            }

            System.out.println("\nEncrypted text: ");
            for (int a = 0; a < string_builder.length(); a++) {
                mystr2[a] = (char) arr2[a];
                System.out.print(mystr2[a]);
            }
            
            // Display success message
            System.out.println("\n\n==========================================");
            System.out.println("       Text Successfully Encrypted!");
            System.out.println("==========================================");
            System.out.println("\nPress Enter to return to Main Menu...");
            CU_Scanner.nextLine(); // Wait for user to press Enter
            
            // Return to MainMenu
            MainMenu.main(null);
            
        } catch (Exception myError) {
            System.out.println("Error occurred during encryption: " + myError.getMessage());
            
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
