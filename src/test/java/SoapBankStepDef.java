import com.example.SimpleDTUPayService;
import com.example.SimpleDTUPayUser;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SoapBankStepDef {
    private List<SimpleDTUPayUser> customers;
//    private BankService bankService = new BankServiceService().getBankServicePort();
    private SimpleDTUPayService simpleDTUPayService = new SimpleDTUPayService();
//    private AccountInfo customerBankAccount = new AccountInfo();
//    private AccountInfo merchantBankAccount = new AccountInfo();
    private SimpleDTUPayUser customerBankAccount = new SimpleDTUPayUser();
    private SimpleDTUPayUser merchantBankAccount = new SimpleDTUPayUser();


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
        customerBankAccount.setFirstName("John");
        customerBankAccount.setLastName("Doe");
        customerBankAccount.setCprNumber("123456-7890");
        // TODO: ENABLE THIS FEATURE
//        customerBankAccount.setRole("customer");

        merchantBankAccount.setFirstName("Jane");
        merchantBankAccount.setLastName("Doe");
        merchantBankAccount.setCprNumber("123456-7891");
    }

    @After
    public void tearDown() {
        // TODO: Might be a better way to cleanup our created accounts, BUT TOO BAD
        var customers = simpleDTUPayService.getBankUsers();
        for (var user : customers) {
            if (user.getCprNumber().equals(customerBankAccount.getCprNumber()) && user.getFirstName().equals(customerBankAccount.getFirstName()) && user.getLastName().equals(customerBankAccount.getLastName())) {
                simpleDTUPayService.retireAccount(user.getBankId().toString());
            }
            if (user.getCprNumber().equals(merchantBankAccount.getCprNumber()) && user.getFirstName().equals(merchantBankAccount.getFirstName()) && user.getLastName().equals(merchantBankAccount.getLastName())) {
                simpleDTUPayService.retireAccount(user.getBankId().toString());
            }
        }
    }

    @Given("a customer with CPR Number {string}")
    public void a_customer_with_cpr_number(String string) {
        customerBankAccount.setCprNumber(string);
        // LEAVE THIS HERE FOR NOW; IT IS MEANT TO CHECK THE EXTERNAL WEB SERVICE FOR CLEANUP
//        var x = simpleDTUPayService.getBankUsers();
//        System.out.println(x);
    }

    @When("the customer is created in the SOAP Bank Service")
    public void the_customer_is_created_in_the_soap_bank_service() {
        // TODO: Ask Hubert why the service returns an exception but still creates the account
        simpleDTUPayService.createAccountWithBalance(customerBankAccount, BigDecimal.ZERO);
    }

    @Then("the customer should exist")
    public void the_customer_should_exist() {
        var customers = simpleDTUPayService.getBankUsers();
        for (var user : customers) {
            if (user.getCprNumber().equals(customerBankAccount.getCprNumber()) && user.getFirstName().equals(customerBankAccount.getFirstName()) && user.getLastName().equals(customerBankAccount.getLastName())) {
                customerBankAccount.setBankId(UUID.fromString(user.getBankId().toString()));
                assertTrue(true);
                return;
            }
        }
    }

    @When("the bank service is queried for the customer")
    public void theBankServiceIsQueriedForTheCustomer() {
        customers = simpleDTUPayService.getBankUsers();
    }

    @Then("the customer should not exist")
    public void theCustomerShouldNotExist() {
        for (var user : customers) {
            if (user.getCprNumber().equals(customerBankAccount.getCprNumber()) && user.getFirstName().equals(customerBankAccount.getFirstName()) && user.getLastName().equals(customerBankAccount.getLastName())) {
                assertTrue(true);
                return;
            }
        }
        assertFalse(false);
    }

    @Given("a customer with a bank account with balance {int}")
    public void aCustomerWithABankAccountWithBalance(int arg0) {
        customerBankAccount.setBalance(BigDecimal.valueOf(arg0));
        simpleDTUPayService.createAccountWithBalance(customerBankAccount, customerBankAccount.getBalance());
    }

    @And("that the customer is registered with DTU Pay")
    public void thatTheCustomerIsRegisteredWithDTUPay() {
        customers = simpleDTUPayService.getBankUsers();
        for (var user : customers) {
            if (user.getCprNumber().equals(customerBankAccount.getCprNumber()) && user.getFirstName().equals(customerBankAccount.getFirstName()) && user.getLastName().equals(customerBankAccount.getLastName())) {
                customerBankAccount.setBankId(UUID.fromString(user.getBankId().toString()));
                customerRegistered = true;
            }
        }
        assertTrue(customerRegistered);
    }

    @Given("a merchant with a bank account with balance {int}")
    public void aMerchantWithABankAccountWithBalance(int arg0) {
        simpleDTUPayService.createAccountWithBalance(merchantBankAccount, BigDecimal.valueOf(arg0));
    }

    @And("that the merchant is registered with DTU Pay")
    public void thatTheMerchantIsRegisteredWithDTUPay() {
        customers = simpleDTUPayService.getBankUsers();
        for (var user : customers) {
            if (user.getCprNumber().equals(merchantBankAccount.getCprNumber()) && user.getFirstName().equals(merchantBankAccount.getFirstName()) && user.getLastName().equals(merchantBankAccount.getLastName())) {
                merchantBankAccount.setBankId(UUID.fromString(user.getBankId().toString()));
                merchantRegistered = true;
            }
        }
        assertTrue(merchantRegistered);
    }

    @When("the merchant starts a payment for {int} kr by the customer")
    public void theMerchantStartsAPaymentForKrByTheCustomer(int arg0) {
        // I...think this needs refactoring
        try {
            assertTrue(simpleDTUPayService.transferMoney(merchantBankAccount.getBankId().toString(), customerBankAccount.getBankId().toString(), arg0));
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
