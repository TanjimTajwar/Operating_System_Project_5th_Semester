import java.util.Scanner;

public class EncryptData {
    public static void main(String[] args) {
        EncryptText();
    }

    static void EncryptText() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("\nEnter text to encrypt: ");
            String str = sc.nextLine();
            StringBuilder sb = new StringBuilder(str);

            int[] aryl = new int[sb.length()];
            int[] ary2 = new int[sb.length()];
            char[] str2 = new char[sb.length()];

            System.out.print("\nOriginal text: ");
            System.out.println(str);

            for (int i = 0; i < sb.length(); i++) {
                aryl[i] = (int) str.charAt(i);
                ary2[i] = aryl[i] + 4;
            }

            System.out.println("\nEncrypting...");
            Thread.sleep(1000);
            System.out.print("Encrypted text: ");
            for (int i = 0; i < sb.length(); i++) {
                str2[i] = (char) ary2[i];
                System.out.print(str2[i]);
            }

            System.out.println("\nEncryption complete.");
        } catch (InterruptedException e) {
            System.out.println("An error occurred during encryption.");
        }
    }
}
