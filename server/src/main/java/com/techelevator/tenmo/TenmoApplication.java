package com.techelevator.tenmo;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TenmoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenmoApplication.class, args);
//        JdbcTransferDao test = new JdbcTransferDao(ne);
//
//        System.out.println(test.getAccountId(1001));
    }

}
