package com.techelevator.tenmo.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class Account {
    private final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    private int id;
    private int userId;
    @Min(0)
    private BigDecimal balance;

    public Account(){}
    public Account(int id, int userId, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.balance = STARTING_BALANCE;
    }

    public Account(int id, int userId) {
        this.id = id;
        this.userId = userId;
        this.balance = STARTING_BALANCE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
