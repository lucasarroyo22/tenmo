package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;

public interface AccountDao {
    BigDecimal getBalance(int accountId);

    void addBalance(int accountId, BigDecimal amount);

    void subtractBalance(int accountId, BigDecimal amount);

    int getAccountId(int userId);
}
