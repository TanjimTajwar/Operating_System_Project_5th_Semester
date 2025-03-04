public class DecryptAESText {
    public static void main(String[] CSECU) {
        String myString = "Encrypted text here";
        decryptAESText(myString);
    }

    static void decryptAESText(String myString) {
        try {
            int[] ch2_array = new int[myString.length()];

            for (int a = 0; a < myString.length(); a++) {
                ch2_array[a] = (int) myString.charAt(a) - 4; // Decrypting by subtracting 4
            }

            System.out.print("Decrypted data: ");
            for (int a = 0; a < myString.length(); a++) {
                System.out.print((char) ch2_array[a]);
            }
            System.out.println("\nDecryption complete.");
        } catch (Exception myError) {
            System.out.println("Error occurred during decryption.");
        }
    }
}
