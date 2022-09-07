package model;

import java.math.BigDecimal;

public class Transfer {
    public final static String TRANSFER_STATUS_PENDING = "Pending";
    public final static String TRANSFER_STATUS_APPROVED = "Approved";
    public final static String TRANSFER_STATUS_REJECTED = "Rejected";
    private int transferId;
    private int fromAccountId;
    private int toAccountId;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Transfer)) {
            return false;
        } else {
            return ((Transfer) obj).transferId == this.transferId &&
                    ((Transfer) obj).fromAccountId == this.fromAccountId &&
                    ((Transfer) obj).toAccountId == this.toAccountId &&
                    ((Transfer) obj).amount.equals(this.amount) &&
                    ((Transfer) obj).status.equals(this.status);
        }
    }

    private BigDecimal amount;
    private String status;

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
