package com.example;

import dtu.ws.fastmoney.AccountInfo;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SimpleDTUPayService {
    // INTERNAL SERVICE VARIABLES
    private FastMoneyUser fastmoneyUser = new FastMoneyUser();
    ArrayList<FastMoneyUser> fastmoneyUsers = new ArrayList<>();
    HashMap<Integer, SimpleDTUPayLedger> transactions = new HashMap<>();

    // EXTERNAL VARIABLES
    private BankService bankService = new BankServiceService().getBankServicePort();

    public SimpleDTUPayService() {
        System.out.println("SimpleDTUPayService Created");
    }

    public boolean transaction(int amount, AccountInfo customer, AccountInfo merchant) {
        if(customer.getAccountId().startsWith("m") || merchant.getAccountId().startsWith("c")) {
            throw new IllegalArgumentException("customer with id "+merchant+" is unknown");
        }
        SimpleDTUPayLedger simpleDTUPayLedger = new SimpleDTUPayLedger(generateId(), UUID.fromString(customer.getAccountId()), UUID.fromString(merchant.getAccountId()), amount);
        try {
            bankService.transferMoneyFromTo(customer.getAccountId(), merchant.getAccountId(), BigDecimal.valueOf(amount), "DTUPay transaction");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        transactions.put(generateId(), simpleDTUPayLedger);
        return true;
    }

    public void initiateTransaction(int amount, SimpleDTUPayLedger simpleDTUPayLedger) {
        // check if customer exists in bank service
        try {
            var customerAccount = bankService.getAccount(simpleDTUPayLedger.getPayer().toString());
            var merchantAccount = bankService.getAccount(simpleDTUPayLedger.getPayee().toString());
            transaction(amount, customerAccount, merchantAccount);
        } catch (BankServiceException_Exception e) {
            throw new RuntimeException(e);
        }

    }

    public HashMap<Integer, SimpleDTUPayLedger> getTransactions() {
        return transactions;
    }

    public int generateId() {
        return transactions.size() + 1;
    }

    public List<AccountInfo> getFastmoneyUsers() {
        return bankService.getAccounts();
    }

    public void createCustomer(AccountInfo fastMoneyUser, BigDecimal balance) {
        try {
            bankService.createAccountWithBalance(fastMoneyUser.getUser(), balance);
        } catch (BankServiceException_Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean doesFastmoneyUserExist(String user_uuid) {
        try {
            bankService.getAccount(user_uuid);
            return true;
        } catch (BankServiceException_Exception e) {
            throw new NotFoundException(e);
        }
    }


//    public boolean getRegistrationCustomer(String cprNumber) {
//        for (SimpleDTUPay transaction : transactions.values()) {
//            if (transaction.getCid().equals(cprNumber)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean getRegistrationMerchant(String cprNumber) {
//        for (SimpleDTUPay transaction : transactions.values()) {
//            if (transaction.getMid().equals(cprNumber)) {
//                return true;
//            }
//        }
//        return false;
//    }
}
