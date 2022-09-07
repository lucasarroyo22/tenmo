import model.Transfer;
import model.TransferClientDTO;
import services.AuthenticationService;
import services.ConsoleService;
import services.TenmoService;

import java.math.BigDecimal;
import java.util.List;

import static model.Transfer.TRANSFER_STATUS_PENDING;

public class CLI {

    public static final char REQUEST_APPROVE = 'A';
    public static final char REQUEST_REJECT = 'R';
    private static final int EXIT_SELECTION = 0;
    private static final int REGISTER_SELECTION = 1;
    private static final int LOGIN_SELECTION = 2;
    private static final int LIST_USERS_SELECTION = 3;
    private static final int LIST_TRANSFERS_SELECTION = 4;
    private static final int SEND_SELECTION = 5;
    private static final int REQUEST_SELECTION = 6;
    private static final int APPROVE_REJECT_SELECTION = 7;
    private final ConsoleService consoleService = new ConsoleService();
    private final TenmoService tenmoService = new TenmoService();
    private final AuthenticationService authenticationService = new AuthenticationService();

    private final GUI gui = new GUI();

    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.run();
    }

    public void run() {
        int menuSelection = -1;
        while (menuSelection != EXIT_SELECTION) {
            consoleService.printMainMenu(tenmoService.isAuth(), tenmoService.getUsername());
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == REGISTER_SELECTION) {
                //Register New User
                String[] registerInfo = consoleService.promptToRegister();
                authenticationService.register(registerInfo[0], registerInfo[1]);
            } else if (menuSelection == LOGIN_SELECTION) {
                handleLogin();
            } else if (menuSelection == LIST_USERS_SELECTION && tenmoService.isAuth()) {
                consoleService.printUsernames(tenmoService.getUsernames());
            } else if (menuSelection == LIST_TRANSFERS_SELECTION && tenmoService.isAuth()) {
                consoleService.printTransfers(tenmoService.getTransfers());
            } else if ((menuSelection == SEND_SELECTION || menuSelection == REQUEST_SELECTION) && tenmoService.isAuth()) {
                handleMoney(menuSelection);
            } else if (menuSelection == APPROVE_REJECT_SELECTION && tenmoService.isAuth()) {
                handleApproveReject();
            } else if (menuSelection == EXIT_SELECTION) {
                continue;
            } else {
                // anything else is not valid
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

   public void handleLogin() {
        String[] loginInfo = consoleService.promptToLogin();
        String token = authenticationService.login(loginInfo[0], loginInfo[1]);
        if (token != null) {
            tenmoService.setAuthToken(token);
            tenmoService.setUsername(loginInfo[0]);
            consoleService.displayBalance(tenmoService.getBalance());
        } else {
            consoleService.printErrorMessage();
        }
    }

/*    public void handleLogin(String[] loginInfo) {

        if (loginInfo[1] != null) {
            tenmoService.setAuthToken(loginInfo[1]);
            tenmoService.setUsername(loginInfo[0]);
            consoleService.displayBalance(tenmoService.getBalance());
        } else {
            consoleService.printErrorMessage();
        }
    }*/

    public void handleMoney(int menuSelection) {
        String[] usernameList = tenmoService.getUsernames();
        consoleService.printUsernames(usernameList);
        String username = consoleService.getUsernameForTransfer(usernameList);
        BigDecimal amount = consoleService.getAmountToTransfer();
        if (!username.isBlank() && !amount.equals(BigDecimal.ZERO)) {
            if (menuSelection == SEND_SELECTION) {
                tenmoService.sendMoney(username, amount);
            } else {
                tenmoService.requestMoney(username, amount);
            }
        }
    }

    public void handleApproveReject() {
        List<TransferClientDTO> transfersList = tenmoService.getTransfersFiltered(TRANSFER_STATUS_PENDING);
        if (transfersList.size() > 0) {
            consoleService.printTransfers(transfersList);
            int transferId = consoleService.getTransfer(transfersList);
            if (transferId != -1) {
                char approveReject = consoleService.getApproveReject();
                if (approveReject == REQUEST_APPROVE) {
                    if (tenmoService.acceptTransfer(transferId)) {
                        consoleService.printTransferSuccess();
                    }
                } else if (approveReject == REQUEST_REJECT) {
                    if (tenmoService.rejectTransfer(transferId)) {
                        consoleService.printTransferRejected();
                    }
                }
            }
        } else {
            System.out.println("No pending transfers to approve/reject!");
        }
    }
}
