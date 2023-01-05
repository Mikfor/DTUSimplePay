import dtu.ws.fastmoney.BankServiceException_Exception;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SoapBankStepDef {

    @Given("a customer with CPR Number {string}")
    public void a_customer_with_cpr_number(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("the customer is created in the SOAP Bank Service")
    public void the_customer_is_created_in_the_soap_bank_service() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the customer should exist")
    public void the_customer_should_exist() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

//    @Given("a customer with a bank account with balance {int}")
//    public void aCustomerWithABankAccountWithBalance(int arg0) {
//        try {
//            bank.createAccountWithBalance(customerAccount, new BigDecimal(arg0));
//            customerBankAccount.setBalance(arg0);
//        } catch (BankServiceException_Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @And("that the customer is registered with DTU Pay")
//    public void thatTheCustomerIsRegisteredWithDTUPay() {
//        assertTrue(dtupayService.getRegistrationCustomer(customerAccount.getCprNumber()));
//    }
//
//    @Given("a merchant with a bank account with balance {int}")
//    public void aMerchantWithABankAccountWithBalance(int arg0) {
//        try {
//            bank.createAccountWithBalance(merchantAccount, BigDecimal.valueOf(arg0));
//            merchantBankAccount.setBalance(arg0);
//        } catch (BankServiceException_Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @And("that the merchant is registered with DTU Pay")
//    public void thatTheMerchantIsRegisteredWithDTUPay() {
//        assertTrue(dtupayService.getRegistrationMerchant(merchantAccount.getCprNumber()));
//    }
//
//    @And("the balance of the customer at the bank is {int} kr")
//    public void theBalanceOfTheCustomerAtTheBankIsKr(int arg0) {
//        assertEquals(arg0, customerBankAccount.getBalance());
//    }
//
//    @And("the balance of the merchant at the bank is {int} kr")
//    public void theBalanceOfTheMerchantAtTheBankIsKr(int arg0) {
//        assertEquals(arg0, merchantBankAccount.getBalance());
//    }

}
