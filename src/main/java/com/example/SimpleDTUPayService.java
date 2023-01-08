package com.example;

import java.math.BigDecimal;
import java.util.HashMap;

public class SimpleDTUPayService {
    // INTERNAL SERVICE VARIABLES
    private final SimpleDTUPayExternalBankServiceAdapter simpleDTUPayExternalBankServiceAdapter = new SimpleDTUPayExternalBankServiceAdapter();
    HashMap<Integer, SimpleDTUPayLedger> transactions = new HashMap<>();
    HashMap<String, SimpleDTUPayUser> users = new HashMap<>();

    // EXTERNAL VARIABLES
    // private BankService bankService = new BankServiceService().getBankServicePort();

    /* TODO
    * User Registration API
    * 1. Check if user exists with external bank service
    * 2. If user does not exist, throw an error that they are not registered
    * 3. If user exists, create a new user in our system with the returned information for the class
     */

    public SimpleDTUPayService() {
        System.out.println("SimpleDTUPayService Created");
    }

    public void registerUser(String bankId) {
        // Check if user exists with external bank service
        var bankUser = simpleDTUPayExternalBankServiceAdapter.getBankUser(bankId);
        if (bankUser.getBankId() == null) {
            // I'm still not sure about this part here; something about this code feels wrong
            throw new RuntimeException("User does not exist in external bank service");
        }
        // If user exists, create a new user in our system with the returned information for the class
        users.put(bankId, bankUser);
        // log that the user was registered
        System.out.println("User " + bankId + " was registered with SimpleDTUPay");
    }

    public void initiateTransaction(int amount, String payerId, String payeeId) {
        // check if customer exists in bank service
        // check that payer and payee exists in our system
        if (!users.containsKey(payerId) ) {
            // if not, query the bank service for the customer
            var payer = simpleDTUPayExternalBankServiceAdapter.getBankUser(payerId);
            // add the customer to our system
            users.put(payer.getBankId().toString(), payer);
        }
        if (!users.containsKey(payeeId)) {
            // if not, query the bank service for the customer
            var payee = simpleDTUPayExternalBankServiceAdapter.getBankUser(payeeId);
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
            throw new RuntimeException("Payer and Payee is the same");
        }
        // conduct the transaction
        if(simpleDTUPayExternalBankServiceAdapter.transferMoney(payer.getBankId().toString(), payee.getBankId().toString(), amount)) {
            // create a new transaction object
            var transaction = new SimpleDTUPayLedger(generateId(), payer, payee, amount);
            // add the transaction to our system
            transactions.put(transaction.getTransactionId(), transaction);
            // update the balance of the payer and payee
            payer.setBalance(payer.getBalance().subtract(BigDecimal.valueOf(amount)));
            payee.setBalance(payee.getBalance().add(BigDecimal.valueOf(amount)));
        }
    }


    public SimpleDTUPayUser getUser(String accountId) {
        return users.get(accountId);
    }

    public HashMap<Integer, SimpleDTUPayLedger> getTransactions() {
        return transactions;
    }

    // Helper function to generate a "unique" id for the list of transactions
    public int generateId() {
        return transactions.size() + 1;
    }
}
