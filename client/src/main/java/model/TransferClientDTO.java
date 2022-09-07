package model;

import java.math.BigDecimal;

public class TransferClientDTO {
    private int transferId;
    private String usernameFrom;
    private String usernameTo;
    private BigDecimal amount;
    private String status;

    public TransferClientDTO() {
    }

    public TransferClientDTO(int transferId, String usernameFrom, String usernameTo, BigDecimal amount, String status) {
        this.transferId = transferId;
        this.usernameFrom = usernameFrom;
        this.usernameTo = usernameTo;
        this.amount = amount;
        this.status = status;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
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
}
