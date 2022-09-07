package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferClientDTO;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    //Shows sent or received, status approved or rejected
    List<Transfer> getTransfersByUser(int userId);

    //Shows pending transfers
    List<Transfer> getTransfersByStatus(int userId, String status);

    List<TransferClientDTO> getTransfersByStatusClient(int userId, String status);

    Transfer getTransfer(int transferId);

    Transfer addTransfer(int userIdSender, int userIdReceiver, BigDecimal amount, String status);

    void updateTransfer(int transferId, String newStatus);

}
