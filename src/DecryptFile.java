import java.io.*;

public class DecryptFile {
    public static void main(String[] CSECU) {
        try {
            String fileName = "encrypted.txt";
            decryptFile(fileName);
        } catch (IOException myError) {
            System.out.println("Error occurred while decrypting file: " + myError.getMessage());
        }
    }
    

    static void decryptFile(String fileName) throws IOException {
        FileReader file_reading = new FileReader(fileName);
        int info;
        StringBuilder content = new StringBuilder();

        while ((info = file_reading.read()) != -1) {
            content.append((char) (info - 4)); // Decrypting the content
        }
        file_reading.close();

        System.out.println("Decrypted File Content:");
        System.out.println(content.toString());
    }
}
