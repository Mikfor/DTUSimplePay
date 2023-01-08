package com.example;

import dtu.ws.fastmoney.*;
import org.jboss.resteasy.spi.NotImplementedYetException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimpleDTUPayExternalBankServiceAdapter implements ExternalBankService {
    private BankService bankService = null;

    public SimpleDTUPayExternalBankServiceAdapter() {
        bankService = new BankServiceService().getBankServicePort();
    }

    @Override
    public SimpleDTUPayUser getBankUser(String bankId) {
        try {
            var bankUser = bankService.getAccount(bankId);
            return new SimpleDTUPayUser(UUID.fromString(bankUser.getId()), bankUser.getBalance(), bankUser.getUser().getFirstName(), bankUser.getUser().getLastName(), bankUser.getUser().getCprNumber());
        } catch (BankServiceException_Exception e) {
            // TODO: ASK ABOUT THIS
            // I'm not sure if this is a good idea for Java conventions
            return new SimpleDTUPayUser();
        }
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
        // TODO: I think the following will lead to issues if the user wants to deregister from DTU Pay,
        // but the docs are not clear on intent
        // remove the account from our system
        //users.remove(bankId);
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
}
