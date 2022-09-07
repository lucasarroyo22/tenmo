package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferClientDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getTransfersByUser(int userId) {
        List<Transfer> outputTransfers = new ArrayList<>();
        String sql = "";

        sql += "SELECT transfer_id, fromaccount_id, toaccount_id, amount, status ";
        sql += "FROM transfer t ";
        sql += "LEFT JOIN account af ON af.account_id = t.fromaccount_id ";
        sql += "LEFT JOIN account at ON at.account_id = t.toaccount_id ";
        sql += "WHERE af.user_id = ? OR at.user_id = ?; ";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
            while (results.next()) {
                outputTransfers.add(mapToTransfer(results));
            }
        } catch (DataAccessException e) {
            System.out.println("Unable to get transfers: " + e.getMessage());
        }

        return outputTransfers;
    }

    @Override
    public List<Transfer> getTransfersByStatus(int userId, String status) {
        List<Transfer> outputTransfers = new ArrayList<>();
        String sql = "";

        sql += "SELECT transfer_id, fromaccount_id, toaccount_id, amount, status ";
        sql += "FROM transfer t ";
        sql += "LEFT JOIN account af ON af.account_id = t.fromaccount_id ";
        sql += "LEFT JOIN account at ON at.account_id = t.toaccount_id ";
        sql += "WHERE (af.user_id = ? OR at.user_id = ?) AND status = ?; ";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId, status);
            while (results.next()) {
                outputTransfers.add(mapToTransfer(results));
            }
        } catch (DataAccessException e) {
            System.out.println("Unable to get transfers: " + e.getMessage());
        }

        return outputTransfers;
    }

    @Override
    public List<TransferClientDTO> getTransfersByStatusClient(int userId, String status) {
        List<TransferClientDTO> outputTransfers = new ArrayList<>();
        String sql = "";

        sql += "SELECT transfer_id, tuf.username AS username_from, tut.username AS username_to , amount, status ";
        sql += "FROM transfer t ";
        sql += "LEFT JOIN account af ON af.account_id = t.fromaccount_id ";
        sql += "LEFT JOIN tenmo_user tuf ON tuf.user_id = af.user_id ";
        sql += "LEFT JOIN account at ON at.account_id = t.toaccount_id ";
        sql += "LEFT JOIN tenmo_user tut ON tut.user_id = at.user_id ";
        sql += "WHERE (af.user_id = ? OR at.user_id = ?) AND status LIKE ?; ";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId, status);
            while (results.next()) {
                outputTransfers.add(mapToTransferClientDTO(results));
            }
        } catch (DataAccessException e) {
            System.out.println("Unable to get transfers: " + e.getMessage());
        }

        return outputTransfers;
    }

    @Override
    public Transfer getTransfer(int transferId) {
        Transfer outputTransfer = new Transfer();
        String sql = "";

        sql += "SELECT transfer_id, fromaccount_id, toaccount_id, amount, status ";
        sql += "FROM transfer ";
        sql += "WHERE transfer_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                outputTransfer = mapToTransfer(results);
            }
        } catch (DataAccessException e) {
            System.out.println("Unable to get transfer: " + e.getMessage());
        }

        return outputTransfer;
    }

    @Override
    public Transfer addTransfer(int accountIdSender, int accountIdReceiver, BigDecimal amount, String status) {
        Transfer outputTransfer = new Transfer();
        String sql = "";

        sql += "INSERT INTO transfer ";
        sql += "(fromaccount_id, toaccount_id, amount, status) ";
        sql += "VALUES ";
        sql += "(?, ?, ?, ?) ";
        sql += "RETURNING transfer_id;";

        try {
            Integer newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, accountIdSender, accountIdReceiver, amount, status);
            outputTransfer = getTransfer(newTransferId);
        } catch (DataAccessException | NullPointerException e) {
            System.out.println("Unable to add transfer: " + e.getMessage());
        }

        return outputTransfer;
    }

    @Override
    public void updateTransfer(int transferId, String newStatus) {
        String sql = "";

        sql += "UPDATE transfer ";
        sql += "SET status = ? ";
        sql += "WHERE transfer_id = ?;";

        try {
            jdbcTemplate.update(sql, newStatus, transferId);
        } catch (DataAccessException e) {
            System.out.println("Unable to update transfer: " + e.getMessage());
        }
    }

    private Transfer mapToTransfer(SqlRowSet rowSet) {
        Transfer outputTransfer = new Transfer();
        outputTransfer.setTransferId(rowSet.getInt("transfer_id"));
        outputTransfer.setFromAccountId(rowSet.getInt("fromaccount_id"));
        outputTransfer.setToAccountId(rowSet.getInt("toaccount_id"));
        outputTransfer.setAmount(rowSet.getBigDecimal("amount"));
        outputTransfer.setStatus(rowSet.getString("status"));
        return outputTransfer;
    }

    private TransferClientDTO mapToTransferClientDTO(SqlRowSet rowSet) {
        TransferClientDTO outputTransfer = new TransferClientDTO();
        outputTransfer.setTransferId(rowSet.getInt("transfer_id"));
        outputTransfer.setUsernameFrom(rowSet.getString("username_from"));
        outputTransfer.setUsernameTo(rowSet.getString("username_to"));
        outputTransfer.setAmount(rowSet.getBigDecimal("amount"));
        outputTransfer.setStatus(rowSet.getString("status"));
        return outputTransfer;
    }
}
