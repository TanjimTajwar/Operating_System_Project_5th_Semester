import java.io.*;

public class DecryptFile {
    public static void main(String[] args) {
        try {
            String fileName = "encrypted.txt";
            decryptFile(fileName);
        } catch (Exception e) {
            System.out.println("Error occurred while decrypting file.");
        }
    }

    static void decryptFile(String fileName) throws IOException {
        FileReader fr = new FileReader(fileName);
        int data;
        StringBuilder content = new StringBuilder();

        while ((data = fr.read()) != -1) {
            content.append((char) (data - 4)); // Decrypting the content
        }
        fr.close();

        System.out.println("Decrypted File Content:");
        System.out.println(content.toString());
    }
}
