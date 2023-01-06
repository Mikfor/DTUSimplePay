package com.example;

import dtu.ws.fastmoney.*;

import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SimpleDTUPayService implements ExternalBankService {
    // INTERNAL SERVICE VARIABLES
    HashMap<Integer, SimpleDTUPayLedger> transactions = new HashMap<>();
    HashMap<String, SimpleDTUPayUser> users = new HashMap<>();

    // EXTERNAL VARIABLES
    private BankService bankService = new BankServiceService().getBankServicePort();

    public SimpleDTUPayService() {
        System.out.println("SimpleDTUPayService Created");
    }

//    public boolean transaction(int amount, SimpleDTUPayUser customer, SimpleDTUPayUser merchant) {
//        return true;
//    }

    public void initiateTransaction(int amount, String payerId, String payeeId) {
        // check if customer exists in bank service
        try {
            // check that payer and payee exists in our system
            if (!users.containsKey(payerId) ) {
                // if not, query the bank service for the customer
                var payerAccount = bankService.getAccount(payerId);
                // create a new customer object
                var payer = new SimpleDTUPayUser(UUID.fromString(payerAccount.getId()), payerAccount.getBalance(), payerAccount.getUser().getFirstName(), payerAccount.getUser().getLastName(), payerAccount.getUser().getCprNumber());
                // add the customer to our system
                users.put(payer.getBankId().toString(), payer);
            }
            if (!users.containsKey(payeeId)) {
                // if not, query the bank service for the customer
                var payeeAccount = bankService.getAccount(payeeId);
                // create a new customer object
                var payee = new SimpleDTUPayUser(UUID.fromString(payeeAccount.getId()), payeeAccount.getBalance(), payeeAccount.getUser().getFirstName(), payeeAccount.getUser().getLastName(), payeeAccount.getUser().getCprNumber());
                // add the customer to our system
                users.put(payee.getBankId().toString(), payee);
            }
            // get the customer from our system
            var payer = users.get(payerId);
            var payee = users.get(payeeId);

            // validate that payer has enough money
            if (payer.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
                throw new RuntimeException("Payer does not have enough money");
            }
            // check that payer and payee is not the same
            if (payer.getBankId().toString().equals(payeeId)) {
                throw new RuntimeException("Payer and payee is the same");
            }
            // conduct the transaction
            if(transferMoney(payer.getBankId().toString(), payee.getBankId().toString(), amount)) {
                // create a new transaction object
                var transaction = new SimpleDTUPayLedger(generateId(), payer, payee, amount);
                // add the transaction to our system
                transactions.put(transaction.getTransactionId(), transaction);
            }
        } catch (BankServiceException_Exception e) {
            throw new RuntimeException(e);
        }

    }

    public HashMap<Integer, SimpleDTUPayLedger> getTransactions() {
        return transactions;
    }

    public HashMap<String, SimpleDTUPayUser> getUsers() {
        return users;
    }

    @Override
    public SimpleDTUPayUser getBankUser(String bankId) {
        return null;
    }

    @Override
    public List<SimpleDTUPayUser> getBankUsers() {
        // converts the bank users to our users
        List<AccountInfo> bankUsers = bankService.getAccounts();
        List<SimpleDTUPayUser> simpleDTUPayUsers = new ArrayList<>();
        // create new list of SimpleDTUPayUsers from bankUsers
        for(AccountInfo bankUser : bankUsers) {
            try {
                var detailedBankUser = bankService.getAccount(bankUser.getAccountId());
                var user = new SimpleDTUPayUser(UUID.fromString(detailedBankUser.getId()), detailedBankUser.getBalance(), detailedBankUser.getUser().getFirstName(), detailedBankUser.getUser().getLastName(), detailedBankUser.getUser().getCprNumber());
                simpleDTUPayUsers.add(user);
            } catch (BankServiceException_Exception e) {
                throw new RuntimeException(e);
            }
        }

        return simpleDTUPayUsers;
    }

    @Override
    public String createAccountWithBalance(SimpleDTUPayUser user, BigDecimal balance) {
        // Just because of the API, we need to use the service's user object
        var bankUser = new User();
        bankUser.setFirstName(user.getFirstName());
        bankUser.setLastName(user.getLastName());
        bankUser.setCprNumber(user.getCprNumber());

        try {
            return bankService.createAccountWithBalance(bankUser, balance);
        } catch (BankServiceException_Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void retireAccount(String bankId) {
        // retire the account from the external bank service
        try {
            bankService.retireAccount(bankId);
        } catch (BankServiceException_Exception e) {
            throw new RuntimeException(e);
        }
        // remove the account from our system
        users.remove(bankId);
    }

    @Override
    public boolean transferMoney(String fromBankId, String toBankId, int amount) throws RuntimeException {
        // Wraps the transferMoney method from the bank service
        try {
            bankService.transferMoneyFromTo(fromBankId, toBankId, BigDecimal.valueOf(amount), "DTUPay transaction");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public SimpleDTUPayUser getUser(String accountId) {
        return users.get(accountId);
    }

    public int generateId() {
        return transactions.size() + 1;
    }

//    public List<AccountInfo> getFastmoneyUsers() {
//        return bankService.getAccounts();
//    }

//    public void createCustomer(String firstName, String lastName, String cprNumber, BigDecimal balance) {
//    }

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
