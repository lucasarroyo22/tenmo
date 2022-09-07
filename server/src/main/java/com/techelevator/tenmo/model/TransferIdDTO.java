package com.techelevator.tenmo.model;

import javax.validation.constraints.NotEmpty;

public class TransferIdDTO {
    @NotEmpty
    private int transferId;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }
}
