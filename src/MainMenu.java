import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nMain Menu");
            System.out.println("1. Encrypt Data");
            System.out.println("2. Decrypt Data");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
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
        } while (choice != 3);
    }
}
