package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int accountId) {
        BigDecimal balance = new BigDecimal(0);
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            balance = mapRowToAccount(results).getBalance();
        }
        return balance;
    }

    @Override
    public void addBalance(int accountId, BigDecimal amount) {
        BigDecimal balance = new BigDecimal(0);
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, amount, accountId);
    }

    @Override
    public void subtractBalance(int accountId, BigDecimal amount) {
        BigDecimal balance = new BigDecimal(0);
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, amount, accountId);
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }

    @Override
    public int getAccountId(int userId) {
        int accountId = 0;
        String sql = "SELECT account_id FROM account WHERE user_id = ?;";
        try {
            accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        } catch (DataAccessException | NullPointerException e) {
            System.out.println("Unable to get account id for user id " + userId + ". " + e.getMessage());
        }
        return accountId;
    }
}
