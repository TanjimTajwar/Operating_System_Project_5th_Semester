public class DecryptAESText {
    public static void main(String[] args) {
        String str = "Encrypted text here";
        decryptAESText(str);
    }

    static void decryptAESText(String str) {
        try {
            int[] ch2 = new int[str.length()];

            for (int i = 0; i < str.length(); i++) {
                ch2[i] = (int) str.charAt(i) - 4; // Decrypting by subtracting 4
            }

            System.out.print("Decrypted data: ");
            for (int i = 0; i < str.length(); i++) {
                System.out.print((char) ch2[i]);
            }
            System.out.println("\nDecryption complete.");
        } catch (Exception e) {
            System.out.println("Error occurred during decryption.");
        }
    }
}
