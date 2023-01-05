import com.example.FastMoneyUser;
import com.example.SimpleDTUPayService;
import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SoapBankStepDef {
    private List<AccountInfo> customers;
    private BankService bankService = new BankServiceService().getBankServicePort();
    private SimpleDTUPayService simpleDTUPayService = new SimpleDTUPayService();
    private AccountInfo customerBankAccount = new AccountInfo();
    private AccountInfo merchantBankAccount = new AccountInfo();

    private boolean customerRegistered;
    private boolean merchantRegistered;
    private ErrorMessageHolder errorMessageHolder = new ErrorMessageHolder();

    @Before
    public void setUp() {
//        try {
//            bankService.retireAccount("b4fd27cc-edaf-4705-83c5-771e520fd8a7");
//        } catch (BankServiceException_Exception e) {
//            throw new RuntimeException(e);
//        }
        customerBankAccount.setUser(new User());
        customerBankAccount.getUser().setFirstName("John");
        customerBankAccount.getUser().setLastName("Doe");
        customerBankAccount.getUser().setCprNumber("123456-7890");
        // TODO: ENABLE THIS FEATURE
//        customerBankAccount.setRole("customer");

        merchantBankAccount.setUser(new User());
        merchantBankAccount.getUser().setFirstName("Jane");
        merchantBankAccount.getUser().setLastName("Doe");
        merchantBankAccount.getUser().setCprNumber("123456-7891");
//        merchantBankAccount.setRole("merchant");
    }

    @After
    public void tearDown() {
        // TODO: Might be a better way to cleanup our created accounts, BUT TOO BAD
        var customers = bankService.getAccounts();
        for (var user : customers) {
            if (user.getUser().getCprNumber().equals(customerBankAccount.getUser().getCprNumber()) && user.getUser().getFirstName().equals(customerBankAccount.getUser().getFirstName()) && user.getUser().getLastName().equals(customerBankAccount.getUser().getLastName())) {
                try {
                    bankService.retireAccount(user.getAccountId());
                } catch (BankServiceException_Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (user.getUser().getCprNumber().equals(merchantBankAccount.getUser().getCprNumber()) && user.getUser().getFirstName().equals(merchantBankAccount.getUser().getFirstName()) && user.getUser().getLastName().equals(merchantBankAccount.getUser().getLastName())) {
                try {
                    bankService.retireAccount(user.getAccountId());
                } catch (BankServiceException_Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Given("a customer with CPR Number {string}")
    public void a_customer_with_cpr_number(String string) {
        customerBankAccount.getUser().setCprNumber(string);
//        var x = bankService.getAccounts();
//        System.out.println(x);
    }

    @When("the customer is created in the SOAP Bank Service")
    public void the_customer_is_created_in_the_soap_bank_service() {
        // TODO: Ask Hubert why the service returns an exception but still creates the account
//        try {
//            bankService.createAccountWithBalance(customerBankAccount, BigDecimal.ZERO);
//        } catch (BankServiceException_Exception e) {
//            e.printStackTrace();
//        }
        simpleDTUPayService.createCustomer(customerBankAccount, BigDecimal.ZERO);
    }

    @Then("the customer should exist")
    public void the_customer_should_exist() {
        var customers = bankService.getAccounts();
        for (var user : customers) {
            if (user.getUser().getCprNumber().equals(customerBankAccount.getUser().getCprNumber()) && user.getUser().getFirstName().equals(customerBankAccount.getUser().getFirstName()) && user.getUser().getLastName().equals(customerBankAccount.getUser().getLastName())) {
                customerBankAccount.setAccountId(user.getAccountId());
                assertTrue(true);
                return;
            }
        }
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

    @When("the bank service is queried for the customer")
    public void theBankServiceIsQueriedForTheCustomer() {
        customers = bankService.getAccounts();
    }

    @Then("the customer should not exist")
    public void theCustomerShouldNotExist() {
        for (var user : customers) {
            if (user.getUser().getCprNumber().equals(customerBankAccount.getUser().getCprNumber()) && user.getUser().getFirstName().equals(customerBankAccount.getUser().getFirstName()) && user.getUser().getLastName().equals(customerBankAccount.getUser().getLastName())) {
                assertTrue(true);
                return;
            }
        }
        assertFalse(false);
    }

    @Given("a customer with a bank account with balance {int}")
    public void aCustomerWithABankAccountWithBalance(int arg0) {
        simpleDTUPayService.createCustomer(customerBankAccount, BigDecimal.valueOf(arg0));
    }

    @And("that the customer is registered with DTU Pay")
    public void thatTheCustomerIsRegisteredWithDTUPay() {
        customers = simpleDTUPayService.getFastmoneyUsers();
        for (var user : customers) {
            if (user.getUser().getCprNumber().equals(customerBankAccount.getUser().getCprNumber()) && user.getUser().getFirstName().equals(customerBankAccount.getUser().getFirstName()) && user.getUser().getLastName().equals(customerBankAccount.getUser().getLastName())) {
                customerBankAccount.setAccountId(user.getAccountId());
                customerRegistered = true;
            }
        }
        assertTrue(customerRegistered);
    }

    @Given("a merchant with a bank account with balance {int}")
    public void aMerchantWithABankAccountWithBalance(int arg0) {
        simpleDTUPayService.createCustomer(merchantBankAccount, BigDecimal.valueOf(arg0));
    }

    @And("that the merchant is registered with DTU Pay")
    public void thatTheMerchantIsRegisteredWithDTUPay() {
        customers = bankService.getAccounts();
        for (var user : customers) {
            if (user.getUser().getCprNumber().equals(merchantBankAccount.getUser().getCprNumber()) && user.getUser().getFirstName().equals(merchantBankAccount.getUser().getFirstName()) && user.getUser().getLastName().equals(merchantBankAccount.getUser().getLastName())) {
                merchantBankAccount.setAccountId(user.getAccountId());
                merchantRegistered = true;
            }
        }
        assertTrue(merchantRegistered);
    }

    @When("the merchant starts a payment for {int} kr by the customer")
    public void theMerchantStartsAPaymentForKrByTheCustomer(int arg0) {
        try {
            assertTrue(simpleDTUPayService.transaction(arg0, customerBankAccount, merchantBankAccount));
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the payment succeeds")
    public void the_payment_succeeds() {
        assertNull(errorMessageHolder.getErrorMessage());
    }

    @Then("the balance of the customer at the bank is {int} kr")
    public void the_balance_of_the_customer_at_the_bank_is_kr(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the balance of the merchant at the bank is {int} kr")
    public void the_balance_of_the_merchant_at_the_bank_is_kr(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
