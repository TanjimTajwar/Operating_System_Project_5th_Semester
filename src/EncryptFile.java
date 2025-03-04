import java.io.*;

public class EncryptFile {
    public static void main(String[] CSECU) {
        try {
            String fileName = "original.txt";
            encryptFile(fileName);
        } catch (IOException myError) {
            System.out.println("Error occurred while encrypting file.");
        }
    }

    static void encryptFile(String fileName) throws IOException {
        FileReader file_reading = new FileReader(fileName);
        FileWriter file_writting = new FileWriter("encrypted.txt");
        int info;

        while ((info = file_reading.read()) != -1) {
            file_writting.write(info + 4); // Encrypting by adding 4 to each character
        }
        file_reading.close();
        file_writting.close();

        System.out.println("File encrypted successfully.");
    }
}
