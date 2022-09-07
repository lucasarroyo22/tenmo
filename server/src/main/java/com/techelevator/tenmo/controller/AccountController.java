package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {
    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @GetMapping(path = "/account")
    public BigDecimal getBalance(Principal principal) {
        int user_id = userDao.findIdByUsername(principal.getName());
        int id = accountDao.getAccountId(user_id);
        return accountDao.getBalance(id);
    }

    /*
    public boolean hasEnough(int id, BigDecimal amount) {
        return amount.compareTo(accountDao.getBalance(id)) < 0;
    }
    */

    @GetMapping(path = "/users")
    public List<String> getAllUsernames() {
        return userDao.getAllUsernames();
    }


}
