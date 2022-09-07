package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.bizLogic.TransferValidation;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.InvalidTransferException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferClientDTO;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.TransferIdDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static com.techelevator.tenmo.model.Transfer.*;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private UserDao userDao;
    private TransferDao transferDao;
    private AccountDao accountDao;
    private TransferValidation validation;

    public TransferController(UserDao userDao, TransferDao transferDao, AccountDao accountDao, TransferValidation validation) {
        this.userDao = userDao;
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.validation = validation;
    }

    @GetMapping(path = "/transfers")
    public List<TransferClientDTO> listTransfersByStatus(@RequestParam(defaultValue = "%") String status, Principal principal) {
        return transferDao.getTransfersByStatusClient(userDao.findIdByUsername(principal.getName()), status);
    }

    @GetMapping(path = "/transfers/{id}")
    public Transfer listTransferById(@PathVariable int id) {
        return transferDao.getTransfer(id);
    }

    @PostMapping(value = "/send")
    public Transfer addTransfer(@Valid @RequestBody TransferDTO transferDTO,
                                Principal principal) throws InvalidTransferException {

        int accountIdSender = validation.usernameToAccountId(principal.getName());
        int accountIdReceiver = validation.usernameToAccountId(transferDTO.getUsername());

        boolean valid = validation.validTransfer(accountIdSender, accountIdReceiver, transferDTO.getAmount(), false);

        if (valid) {
            accountDao.subtractBalance(accountIdSender, transferDTO.getAmount());
            accountDao.addBalance(accountIdReceiver, transferDTO.getAmount());
            return transferDao.addTransfer(
                    accountIdSender,
                    accountIdReceiver,
                    transferDTO.getAmount(),
                    TRANSFER_STATUS_APPROVED);
        } else {
            throw new InvalidTransferException(
                    "Invalid transfer from " + principal.getName() + " to " + transferDTO.getUsername() + ": " +
                            (!validation.isDifferentAccounts(accountIdSender, accountIdReceiver) ? "Transfer between same user " : "") +
                            (!validation.isSufficientFunds(accountIdSender, transferDTO.getAmount()) ? "Insufficient funds in source account" : "") +
                            (!validation.isPositiveAmount(transferDTO.getAmount(), BigDecimal.ZERO) ? "Transfer must be a positive amount" : "")
            );
        }
    }

    // Request transfer: status pending
    @PostMapping(value = "/request")
    public Transfer requestTransfer(@Valid @RequestBody TransferDTO transferDTO, Principal principal) throws InvalidTransferException {
        int accountIdSender = validation.usernameToAccountId(transferDTO.getUsername());
        int accountIdReceiver = validation.usernameToAccountId(principal.getName());

        boolean valid = validation.validTransfer(accountIdSender, accountIdReceiver, transferDTO.getAmount(), true);

        if (valid) {
            return transferDao.addTransfer(
                    accountIdSender,
                    accountIdReceiver,
                    transferDTO.getAmount(), TRANSFER_STATUS_PENDING);
        } else {
            throw new InvalidTransferException(
                    "Invalid transfer from " + principal.getName() + " to " + transferDTO.getUsername() + ": " +
                            (!validation.isDifferentAccounts(accountIdSender, accountIdReceiver) ? "Transfer between same user " : "") +
                            (!validation.isPositiveAmount(transferDTO.getAmount(), BigDecimal.ZERO) ? "Transfer must be a positive amount" : "")
            );
        }
    }

    // Approve transfer:  status approved, tell account controller to update accounts
    //  can't approve if insufficient funds
    @PutMapping(path = "/transfers/approve")
    public void approveTransfer(@RequestBody TransferIdDTO transferId, Principal principal) throws InvalidTransferException {
        Transfer transfer = transferDao.getTransfer(transferId.getTransferId());
        int accountIdSender = transfer.getFromAccountId();
        int accountIdReceiver = transfer.getToAccountId();
        int accountIdApprover = validation.usernameToAccountId(principal.getName());

        boolean valid = validation.validTransfer(accountIdSender, accountIdReceiver, transfer.getAmount(), false);
        boolean differentReceiver = accountIdApprover != accountIdReceiver;

        if (valid && differentReceiver) {
            transferDao.updateTransfer(transferId.getTransferId(), TRANSFER_STATUS_APPROVED);
            accountDao.subtractBalance(transfer.getFromAccountId(), transfer.getAmount());
            accountDao.addBalance(transfer.getToAccountId(), transfer.getAmount());
        } else {
            String insufficientFunds = "Invalid transfer: Insufficient funds";
            String transferOtherUser = "Transfer must be approved by other user";
            throw new InvalidTransferException(
                    (!validation.isSufficientFunds(accountIdSender, transfer.getAmount()) ? insufficientFunds : "") +
                            (!differentReceiver ? transferOtherUser : ""));
        }
    }

    // Reject transfer: status rejected
    @PutMapping(path = "/transfers/reject")
    public void rejectTransfer(@RequestBody TransferIdDTO transferId) {
        transferDao.updateTransfer(transferId.getTransferId(), TRANSFER_STATUS_REJECTED);
    }
}
