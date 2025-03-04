import java.io.*;
import java.util.Scanner;

public class FileDecryption {
    public static void main(String[] args) {
        // Call the method for file decryption
        DecryptFile();
    }

    // Function to decrypt a file
    static void DecryptFile() {
        try {
            String str;
            System.out.print("\n\nEnter file name with (.txt) extension: ");
            Scanner sc = new Scanner(System.in);
            str = sc.nextLine();

            FileReader fw3 = new FileReader(str); // opening encrypted file
            int length1 = 0, c;
            
            // Read the file to determine its length
            while ((c = fw3.read()) != -1) {
                length1++;
            }
            fw3.close();

            fw3 = new FileReader(str);
            int[] ch2 = new int[length1];
            int i = 0;
            
            // Read the file's original data
            while ((c = fw3.read()) != -1) {
                ch2[i++] = c;
            }

            // Decrypt the data
            for (i = 0; i < length1; i++) {
                ch2[i] = ch2[i] - 4;
            }

            // Print decrypted data
            System.out.print("\nDecrypted data: ");
            for (i = 0; i < length1; i++) {
                System.out.print((char) ch2[i]);
            }

            System.out.println("\nDecryption complete.");
            fw3.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
        }
    }
}
