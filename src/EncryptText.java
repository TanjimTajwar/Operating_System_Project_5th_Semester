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
            System.out.println("\nEncryption complete.");
        } catch (Exception myError) {
            System.out.println("Error occurred during encryption.");
        }
    }
}
