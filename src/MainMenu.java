import java.util.Scanner;

public class MainMenu {
    public static void main(String[] CSECU) {
        Scanner CU_Scanner = new Scanner(System.in);
        int myChoice;

        do {
            System.out.println("\n==========================================");
            System.out.println("\t  CSECU Operating System Project");
            System.out.println("==========================================\n");
            System.out.println("Welcome to the CSECU Operating System Project!");
            System.out.println("In this system, you can securely encrypt or decrypt your data with ease.\n");
            System.out.println("Main Menu:");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Encrypt Data");
            System.out.println("2. Decrypt Data");
            System.out.println("3. Exit");
            System.out.println("-----------------------------------------------------");
            System.out.print("\nPlease select an option (1-3): ");
            myChoice = CU_Scanner.nextInt();


            switch (myChoice) {
                case 1:
                    System.out.println("Encrypting data...");
                    EncryptData.main(null); // Calls EncryptData.java
                    break;
                case 2:
                    System.out.println("Decrypting data...");
                    FileDecryption.main(null); // Calls FileDecryption.java
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (myChoice != 3);
    }
}
