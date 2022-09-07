package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class Transfer {
    public final static String TRANSFER_STATUS_PENDING = "Pending";
    public final static String TRANSFER_STATUS_APPROVED = "Approved";
    public final static String TRANSFER_STATUS_REJECTED = "Rejected";
    private int transferId;
    private int fromAccountId;
    private int toAccountId;
    @Min(0)
    private BigDecimal amount;
    private String status;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Transfer)) {
            return false;
        } else {
            Transfer o = (Transfer) obj;
            return o.transferId == this.transferId &&
                    o.fromAccountId == this.fromAccountId &&
                    o.toAccountId == this.toAccountId &&
                    o.amount == null ? this.amount == null : o.amount.equals(this.amount) &&
                    o.status == null ? this.status == null : o.status.equals(this.status);
        }
    }

    public Transfer() {
    }

    public Transfer(int transferId, int fromAccountId, int toAccountId, BigDecimal amount, String status) {
        this.transferId = transferId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = status;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
