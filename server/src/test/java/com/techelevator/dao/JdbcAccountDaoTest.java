package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTest extends BaseDaoTests {

    private JdbcAccountDao sut;
    private User testUser;
    private int usersInDb;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        UserDao userDao = new JdbcUserDao(jdbcTemplate);
        userDao.create("TEST_USER", "TEST");
        testUser = userDao.findByUsername("TEST_USER");
        sut = new JdbcAccountDao(jdbcTemplate);
        usersInDb = userDao.getAllUsernames().size();
    }

    @Test
    public void getAccountId() {
        //This test won't pass if other tests are run first because id's are used in the sequence
        int expectedId = 2000 + usersInDb; //2 users created in test-data, so next should be 2003
        int accountId = sut.getAccountId(testUser.getId());
        Assert.assertEquals(expectedId, accountId);
    }

    @Test
    public void getAccountIdNotFound() {
        int expectedId = 0;
        int accountId = sut.getAccountId(-1);
        Assert.assertEquals(expectedId, accountId);
    }

    @Test
    public void getBalance() {
        BigDecimal expectedBalance = new BigDecimal("1000.00");

        int accountId = sut.getAccountId(testUser.getId());
        BigDecimal actualBalance = sut.getBalance(accountId);

        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void getBalanceNotFound() {
        BigDecimal expectedBalance = BigDecimal.ZERO;

        int accountId = sut.getAccountId(-1);
        BigDecimal actualBalance = sut.getBalance(accountId);

        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void addBalance() {
        BigDecimal expectedBalance = new BigDecimal("1000.01");

        int accountId = sut.getAccountId(testUser.getId());
        sut.addBalance(accountId, new BigDecimal("0.01"));
        BigDecimal actualBalance = sut.getBalance(accountId);

        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void addBalanceNotFound() {
        BigDecimal expectedBalance = BigDecimal.ZERO;

        int accountId = sut.getAccountId(-1);
        sut.addBalance(accountId, new BigDecimal("0.01"));
        BigDecimal actualBalance = sut.getBalance(accountId);

        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void subtractBalance() {
        BigDecimal expectedBalance = new BigDecimal("0.00");

        int accountId = sut.getAccountId(testUser.getId());
        sut.subtractBalance(accountId, new BigDecimal("1000.00"));
        BigDecimal actualBalance = sut.getBalance(accountId);

        Assert.assertEquals(expectedBalance, actualBalance);

        expectedBalance = new BigDecimal("-0.01");

        sut.subtractBalance(accountId, new BigDecimal("0.01"));
        actualBalance = sut.getBalance(accountId);

        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void subtractBalanceNotFound() {
        BigDecimal expectedBalance = BigDecimal.ZERO;

        int accountId = sut.getAccountId(-1);
        sut.subtractBalance(accountId, new BigDecimal("1000.00"));
        BigDecimal actualBalance = sut.getBalance(accountId);

        Assert.assertEquals(expectedBalance, actualBalance);
    }

}