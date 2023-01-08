package com.example;

import java.math.BigDecimal;
import java.util.List;

public interface ExternalBankService {
    SimpleDTUPayUser getBankUser(String bankId);
    List<SimpleDTUPayUser> getBankUsers();
    String createAccountWithBalance(SimpleDTUPayUser user, BigDecimal balance);
    void retireAccount(String bankId);
    boolean transferMoney(String fromBankId, String toBankId, int amount);
}
