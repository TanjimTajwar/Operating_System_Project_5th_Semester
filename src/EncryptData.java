import java.util.Scanner;

public class EncryptData {
    public static void main(String[] CSECU) {
        EncryptText();
    }

    static void EncryptText() {
        try {
            Scanner CU_Scanner = new Scanner(System.in);
            System.out.print("\nEnter text to encrypt: ");
            String myString = CU_Scanner.nextLine();
            StringBuilder string_builder = new StringBuilder(myString);

            int[] arr1 = new int[string_builder.length()];
            int[] arr2 = new int[string_builder.length()];
            char[] mystr2 = new char[string_builder.length()];

            System.out.print("\nOriginal text: ");
            System.out.println(myString);

            for (int a = 0; a < string_builder.length(); a++) {
                arr1[a] = (int) myString.charAt(a);
                arr2[a] = arr1[a] + 4;
            }

            System.out.println("\nEncrypting...");
            Thread.sleep(1000);
            System.out.print("Encrypted text: ");
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
            System.out.println("An error occurred during encryption: " + myError.getMessage());
            
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
