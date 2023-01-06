package com.example;

import java.math.BigDecimal;
import java.util.List;

public interface ExternalBankService {
    public SimpleDTUPayUser getBankUser(String bankId);
    public List<SimpleDTUPayUser> getBankUsers();
    public String createAccountWithBalance(SimpleDTUPayUser user, BigDecimal balance);
    public void retireAccount(String bankId);
    public boolean transferMoney(String fromBankId, String toBankId, int amount);
}
