import java.io.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncryptFile {
    public static void main(String[] CSECU) {
        try {
            String fileName = "original.txt";
            encryptFile(fileName);
        } catch (Exception myError) {
            System.out.println("Error occurred while encrypting file.");
        }
    }

    static void encryptFile(String fileName) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        FileReader file_reading = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(file_reading);
        StringBuilder content = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();

        byte[] encryptedBytes = cipher.doFinal(content.toString().getBytes());
        String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);

        FileWriter file_writting = new FileWriter("encrypted.txt");
        file_writting.write(encryptedText);
        file_writting.close();

        System.out.println("File encrypted successfully.");
    }
}
