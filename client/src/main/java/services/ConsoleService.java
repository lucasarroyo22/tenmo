package services;

import model.Transfer;
import model.TransferClientDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public void printErrorMessage() {
        System.out.println("An error has occurred.");
    }

    public void printInvalidSelection() {
        System.out.println("Invalid selection. Please try again.");
    }

    public void printTransferSuccess() {
        System.out.println("Transfer successful!");
    }

    public void printTransferFailure() {
        System.out.println("Transfer was unsuccessful.");
    }

    public void printTransferRejected() {
        System.out.println("Transfer was rejected.");
    }


    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printMainMenu(boolean loggedIn, String username) {
        System.out.println();
        System.out.println("----Tenmo Main Menu----");
        System.out.println("1: Register New User");
        System.out.println("2: Login" + (loggedIn ? " (" + username + ")" : ""));
        if (loggedIn) {
            System.out.println("3: List Usernames");
            System.out.println("4: View Transfers");
            System.out.println("5: Send Money");
            System.out.println("6: Request Money");
            System.out.println("7: Accept/Reject Transfer");
        }
        System.out.println("0: Exit");
        System.out.println();
    }

    public String[] promptToRegister() {
        String[] userInfo = new String[2];
        System.out.println("Enter A Username: ");
        userInfo[0] = scanner.nextLine();
        System.out.println("Enter A Password: ");
        userInfo[1] = scanner.nextLine();
        return userInfo;
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public String[] promptToLogin() {
        String[] userInfo = new String[2];
        System.out.println("Welcome Back!");
        System.out.println("Enter Your Username: ");
        userInfo[0] = scanner.nextLine();
        System.out.println("Enter Your Password: ");
        userInfo[1] = scanner.nextLine();
        return userInfo;
    }

    public void displayBalance(BigDecimal balance) {
        System.out.println("Your current account balance is: $" + balance.setScale(2, RoundingMode.HALF_UP));
    }

    public void printUsernames(String[] users) {
        System.out.println("\n----     Users      ----");
        int i = 1;
        for (String user : users) {
            System.out.println(i + ": " + user);
            i++;
        }
    }

    public void printTransfers(List<TransferClientDTO> transfers) {
        System.out.println("\n----   Transfers    ----");
        int i = 1;
        for (TransferClientDTO transfer : transfers) {
            System.out.println(i + ". From: " + transfer.getUsernameFrom() + " To: " + transfer.getUsernameTo() + " Amount: " + transfer.getAmount() + " Status: " + transfer.getStatus());
            i++;
        }
    }

    public String getUsernameForTransfer(String[] users) {
        System.out.println("Please select a user: ");
        String userNumber = scanner.nextLine();
        String username = "";
        try {
            username = users[Integer.parseInt(userNumber.strip()) - 1];
        } catch (NumberFormatException e) {
            printInvalidSelection();
        }
        return username;
    }

    public BigDecimal getAmountToTransfer() {
        System.out.println("Enter amount to transfer: ");
        BigDecimal amount = BigDecimal.ZERO;
        try {
            amount = BigDecimal.valueOf(Double.parseDouble(scanner.nextLine().strip()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please try again.");
        }
        return amount;
    }

    public int getTransfer(List<TransferClientDTO> transfers) {
        System.out.println("Please select a transfer: ");
        String transferNumber = scanner.nextLine();
        int transferId = -1;
        try {
            transferId = transfers.get(Integer.parseInt(transferNumber.strip()) - 1).getTransferId();
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            printInvalidSelection();
        }
        return transferId;
    }

    public char getApproveReject() {
        System.out.println("(A)pprove or (R)eject? (any other value to cancel): ");
        char choice = '0';
        try {
            choice = scanner.nextLine().strip().toUpperCase().charAt(0);
        } catch (StringIndexOutOfBoundsException e) {
            printInvalidSelection();
        }
        return choice;
    }
}
