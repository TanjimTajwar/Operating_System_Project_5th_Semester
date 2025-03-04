import java.util.Scanner;

public class EncryptText {
    public static void main(String[] args) {
        encryptText();
    }

    static void encryptText() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("\nEnter text to encrypt: ");
            String str = sc.nextLine();

            StringBuilder sb = new StringBuilder(str);
            int[] aryl = new int[sb.length()];
            int[] ary2 = new int[sb.length()];
            char[] str2 = new char[sb.length()];

            for (int i = 0; i < sb.length(); i++) {
                aryl[i] = (int) sb.charAt(i);
                ary2[i] = aryl[i] + 4;
            }

            System.out.println("\nEncrypted text: ");
            for (int i = 0; i < sb.length(); i++) {
                str2[i] = (char) ary2[i];
                System.out.print(str2[i]);
            }
            System.out.println("\nEncryption complete.");
        } catch (Exception e) {
            System.out.println("Error occurred during encryption.");
        }
    }
}
