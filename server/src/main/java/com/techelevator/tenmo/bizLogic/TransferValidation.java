package com.techelevator.tenmo.bizLogic;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransferValidation {
    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;

//    public TransferValidation(UserDao userDao, AccountDao accountDao) {
//        this.userDao = userDao;
//        this.accountDao = accountDao;
//    }

    public boolean validTransfer(int fromAcct, int toAcct, BigDecimal amount, boolean isRequest) {
        //not same sender and receiver account
        //positive transfer amount
        //sufficient funds in sender account

        boolean differentAccounts = isDifferentAccounts(fromAcct, toAcct);
        boolean positiveAmount = isPositiveAmount(amount, BigDecimal.ZERO);
        boolean sufficientFunds = isSufficientFunds(fromAcct, amount) || isRequest;

        return differentAccounts && positiveAmount && sufficientFunds;
    }

    public boolean validTransfer(String fromUsername, String toUsername, BigDecimal amount, boolean isRequest) {
        return validTransfer(usernameToAccountId(fromUsername), usernameToAccountId(toUsername), amount, isRequest);
    }

    public boolean isSufficientFunds(int fromAcct, BigDecimal amount) {
        BigDecimal senderFunds = accountDao.getBalance(fromAcct);
        return isPositiveAmount(senderFunds, amount);
    }

    public boolean isPositiveAmount(BigDecimal amount, BigDecimal zero) {
        return amount.compareTo(zero) > 0;
    }

    public boolean isDifferentAccounts(int fromAcct, int toAcct) {
        return fromAcct != toAcct;
    }

    public int usernameToAccountId(String username) {
        return accountDao.getAccountId(userDao.findIdByUsername(username));
    }
}
