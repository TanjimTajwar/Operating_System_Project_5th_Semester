import java.io.*;
import java.util.Scanner;

public class FileDecryption {
    public static void main(String[] CSECU) {
        // Call the method for file decryption
        DecryptFile();
    }

    // Function to decrypt a file
    static void DecryptFile() {
        try {
            String myString;
            System.out.print("\n\nEnter file name with (.txt) extension: ");
            Scanner CU_Scanner = new Scanner(System.in);
            myString = CU_Scanner.nextLine();

            FileReader file_reading = new FileReader(myString); // opening encrypted file
            int length1 = 0, info;
            
            // Read the file to determine its length
            while ((info = file_reading.read()) != -1) {
                length1++;
            }
            file_reading.close();

            file_reading = new FileReader(myString);
            int[] ch2_array = new int[length1];
            int a = 0;
            
            // Read the file's original data
            while ((info = file_reading.read()) != -1) {
                ch2_array[a++] = info;
            }

            // Decrypt the data
            for (a = 0; a < length1; a++) {
                ch2_array[a] = ch2_array[a] - 4;
            }

            // Print decrypted data
            System.out.print("\nDecrypted data: ");
            for (a = 0; a < length1; a++) {
                System.out.print((char) ch2_array[a]);
            }

            System.out.println("\nDecryption complete.");
            file_reading.close();
        } catch (IOException myError) {
            System.out.println("An error occurred while reading the file.");
        }
    }
}
