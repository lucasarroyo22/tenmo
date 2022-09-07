package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTest extends BaseDaoTests {

    private JdbcAccountDao accountDao;
    private User testUser;
    private JdbcTransferDao sut;
    private JdbcTemplate jdbcTemplate;

    private Transfer transfer1 = new Transfer(
            3001,
            2001,
            2002,
            new BigDecimal("1.00"),
            "Approved");
    private Transfer transfer2 = new Transfer(
            3002,
            2001,
            2002,
            new BigDecimal("1000.00"),
            "Pending");
    private Transfer newTransfer = new Transfer(
            3003,
            2001,
            2002,
            new BigDecimal("100.00"),
            "Pending");


    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        UserDao userDao = new JdbcUserDao(jdbcTemplate);
        userDao.create("TEST_USER", "TEST");
        userDao.create("TEST_USER2", "TEST_TEST");
        testUser = userDao.findByUsername("TEST_USER");
        accountDao = new JdbcAccountDao(jdbcTemplate);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getTransfersByUser() {
        int expectedTransfers = 2;
        List<Transfer> newUserTransfers = sut.getTransfersByUser(1001);
        Assert.assertEquals(expectedTransfers, newUserTransfers.size());
    }

    @Test
    public void getTransfersByUserIdNotFound() {
        int expectedTransfers = 0;
        List<Transfer> newUserTransfers = sut.getTransfersByUser(-1);
        Assert.assertEquals(expectedTransfers, newUserTransfers.size());
    }

    @Test
    public void getTransfer() {
        Transfer expectedTransfer = transfer1;

        Transfer actualTransfer = sut.getTransfer(3001);

        Assert.assertEquals(expectedTransfer.getTransferId(), actualTransfer.getTransferId());
        Assert.assertEquals(expectedTransfer.getFromAccountId(), actualTransfer.getFromAccountId());
        Assert.assertEquals(expectedTransfer.getToAccountId(), actualTransfer.getToAccountId());
        Assert.assertEquals(expectedTransfer.getAmount(), actualTransfer.getAmount());
        Assert.assertEquals(expectedTransfer.getStatus(), actualTransfer.getStatus());

        Assert.assertEquals(expectedTransfer, actualTransfer);

        expectedTransfer = new Transfer();
        actualTransfer = sut.getTransfer(0);
        Assert.assertEquals(expectedTransfer.getTransferId(), actualTransfer.getTransferId());
        Assert.assertEquals(expectedTransfer.getFromAccountId(), actualTransfer.getFromAccountId());
        Assert.assertEquals(expectedTransfer.getToAccountId(), actualTransfer.getToAccountId());
        Assert.assertEquals(expectedTransfer.getAmount(), actualTransfer.getAmount());
        Assert.assertEquals(expectedTransfer.getStatus(), actualTransfer.getStatus());
        Assert.assertEquals(expectedTransfer, actualTransfer);
    }

    @Test
    public void getTransferNotFound() {
        Transfer expectedTransfer = new Transfer();

        Transfer actualTransfer = sut.getTransfer(-1);

        Assert.assertEquals(expectedTransfer.getTransferId(), actualTransfer.getTransferId());
        Assert.assertEquals(expectedTransfer.getFromAccountId(), actualTransfer.getFromAccountId());
        Assert.assertEquals(expectedTransfer.getToAccountId(), actualTransfer.getToAccountId());
        Assert.assertEquals(expectedTransfer.getAmount(), actualTransfer.getAmount());
        Assert.assertEquals(expectedTransfer.getStatus(), actualTransfer.getStatus());

        Assert.assertEquals(expectedTransfer, actualTransfer);
    }

    @Test
    public void getTransfersByStatus() {
        List<Transfer> expectedList = new ArrayList<>();
        expectedList.add(transfer1);
        List<Transfer> actualList = sut.getTransfersByStatus(1001, "Approved");

        Assert.assertEquals(expectedList, actualList);

        expectedList = new ArrayList<>();
        expectedList.add(transfer2);
        actualList = sut.getTransfersByStatus(1001, "Pending");

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void getTransfersByStatusClient() {
        List<Transfer> expectedList = new ArrayList<>();
        expectedList.add(transfer1);
        List<Transfer> actualList = sut.getTransfersByStatus(1001, "Approved");

        Assert.assertEquals(expectedList, actualList);

        expectedList = new ArrayList<>();
        expectedList.add(transfer2);
        actualList = sut.getTransfersByStatus(1001, "Pending");

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void getTransfersByStatusClientNotFound() {
        List<Transfer> expectedList = new ArrayList<>();
        List<Transfer> actualList = sut.getTransfersByStatus(-1, "Approved");

        Assert.assertEquals(expectedList, actualList);

        expectedList = new ArrayList<>();
        actualList = sut.getTransfersByStatus(-1, "Pending");

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void addTransfer() {
        Transfer expectedTransfer = newTransfer;
        Transfer actualTransfer = sut.addTransfer(newTransfer.getFromAccountId(), newTransfer.getToAccountId(), newTransfer.getAmount(), newTransfer.getStatus());

        Assert.assertEquals(expectedTransfer, actualTransfer);
    }

    @Test
    public void addTransferUserNotFound() {
        Transfer expectedTransfer = new Transfer();
        Transfer actualTransfer = sut.addTransfer(-1, -1, BigDecimal.ONE, "");

        Assert.assertEquals(expectedTransfer, actualTransfer);
    }

    @Test
    public void updateTransfer() {
        Transfer expectedTransfer = new Transfer(
                3002,
                2001,
                2002,
                new BigDecimal("1000.00"),
                "Approved");
        sut.updateTransfer(3002, "Approved");
        Transfer actualTransfer = sut.getTransfer(3002);

        Assert.assertEquals(expectedTransfer, actualTransfer);
    }

    @Test
    public void updateTransferOtherValue() {
        Transfer expectedTransfer = new Transfer(
                3002,
                2001,
                2002,
                new BigDecimal("1000.00"),
                "TEST VALUE");
        sut.updateTransfer(3002, "TEST VALUE");
        Transfer actualTransfer = sut.getTransfer(3002);

        Assert.assertEquals(expectedTransfer, actualTransfer);
    }

    @Test
    public void updateTransferNullValue() {
        Transfer expectedTransfer = transfer2;

        sut.updateTransfer(3002, null);

        //Need to rollback for testing due to JUnit transaction
        //No changes should happen to the transfer
        jdbcTemplate.execute("ROLLBACK;");
        Transfer actualTransfer = sut.getTransfer(3002);

        Assert.assertEquals(expectedTransfer, actualTransfer);
    }
}