import java.io.*;

public class EncryptFile {
    public static void main(String[] args) {
        try {
            String fileName = "original.txt";
            encryptFile(fileName);
        } catch (IOException e) {
            System.out.println("Error occurred while encrypting file.");
        }
    }

    static void encryptFile(String fileName) throws IOException {
        FileReader fr = new FileReader(fileName);
        FileWriter fw = new FileWriter("encrypted.txt");
        int data;

        while ((data = fr.read()) != -1) {
            fw.write(data + 4); // Encrypting by adding 4 to each character
        }
        fr.close();
        fw.close();

        System.out.println("File encrypted successfully.");
    }
}
