package model;

import java.math.BigDecimal;

public class TransferDTO {

    private String username;
    private BigDecimal amount;

    public TransferDTO(String username, BigDecimal amount) {
        this.username = username;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
