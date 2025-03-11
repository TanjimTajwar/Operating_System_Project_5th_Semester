import java.util.Scanner;

public class MainMenu {
    public static void main(String[] CSECU) {
        Scanner CU_Scanner = new Scanner(System.in);
        int mainChoice;
        int subChoice;

        do {
            System.out.println("\n==========================================");
            System.out.println("\t  CSECU Operating System Project");
            System.out.println("==========================================\n");
            System.out.println("Welcome to the CSECU Operating System Project!");
            System.out.println("In this system, you can securely encrypt or decrypt your data with ease.\n");
            System.out.println("Main Menu:");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("3. Exit");
            System.out.println("-----------------------------------------------------");
            System.out.print("\nPlease select an option (1-3): ");
            mainChoice = CU_Scanner.nextInt();
            CU_Scanner.nextLine(); // Consume newline

            switch (mainChoice) {
                case 1: // Encrypt options
                    do {
                        System.out.println("\n==========================================");
                        System.out.println("\t  Encryption Options");
                        System.out.println("==========================================\n");
                        System.out.println("What would you like to encrypt?");
                        System.out.println("-----------------------------------------------------");
                        System.out.println("1. Encrypt Data");
                        System.out.println("2. Encrypt Text File");
                        System.out.println("3. Encrypt Image");
                        System.out.println("4. Back to Main Menu");
                        System.out.println("-----------------------------------------------------");
                        System.out.print("\nPlease select an option (1-4): ");
                        subChoice = CU_Scanner.nextInt();
                        CU_Scanner.nextLine(); // Consume newline

                        switch (subChoice) {
                            case 1:
                                System.out.println("Encrypting data...");
                                EncryptData.main(null);
                                break;
                            case 2:
                                System.out.println("Encrypting text file...");
                                EncryptFile.main(null);
                                break;
                            case 3:
                                System.out.print("Enter the image file name (e.g., image.jpg): ");
                                String imageName = CU_Scanner.nextLine();
                                System.out.print("Enter the encryption key (16 characters): ");
                                String keyEncrypt = CU_Scanner.nextLine();
                                if (keyEncrypt.length() != 16) {
                                    System.out.println("❌ Error: Key must be exactly 16 characters!");
                                    break;
                                }
                                ImageEncryptor.encryptImage(imageName, keyEncrypt);
                                break;
                            case 4:
                                System.out.println("Returning to main menu...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    } while (subChoice != 4);
                    break;

                case 2: // Decrypt options
                    do {
                        System.out.println("\n==========================================");
                        System.out.println("\t  Decryption Options");
                        System.out.println("==========================================\n");
                        System.out.println("What would you like to decrypt?");
                        System.out.println("-----------------------------------------------------");
                        System.out.println("1. Decrypt Data");
                        System.out.println("2. Decrypt Text File");
                        System.out.println("3. Decrypt Image");
                        System.out.println("4. Back to Main Menu");
                        System.out.println("-----------------------------------------------------");
                        System.out.print("\nPlease select an option (1-4): ");
                        subChoice = CU_Scanner.nextInt();
                        CU_Scanner.nextLine(); // Consume newline

                        switch (subChoice) {
                            case 1:
                                System.out.println("Decrypting data...");
                                FileDecryption.main(null);
                                break;
                            case 2:
                                System.out.println("Decrypting text file...");
                                DecryptFile.main(null);
                                break;
                            case 3:
                                System.out.print("Enter the encrypted image file name (e.g., image.jpg.enc): ");
                                String encryptedImageName = CU_Scanner.nextLine();
                                System.out.print("Enter the decryption key (16 characters): ");
                                String keyDecrypt = CU_Scanner.nextLine();
                                if (keyDecrypt.length() != 16) {
                                    System.out.println("❌ Error: Key must be exactly 16 characters!");
                                    break;
                                }
                                System.out.print("Enter a name for the decrypted image (e.g., new_image.jpg): ");
                                String newImageName = CU_Scanner.nextLine();
                                ImageDecryptor.decryptImage(encryptedImageName, keyDecrypt, newImageName);
                                break;
                            case 4:
                                System.out.println("Returning to main menu...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    } while (subChoice != 4);
                    break;

                case 3:
                    System.out.println("Thank you for using CSECU Operating System Project. Goodbye!");
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (mainChoice != 3);

        CU_Scanner.close();
    }
}
