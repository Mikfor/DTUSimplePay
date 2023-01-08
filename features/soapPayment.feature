Feature: Soap Payment

  Scenario: User Exists in SOAP Service
    Given a customer with CPR Number "123456-7890"
    When the customer is created in the SOAP Bank Service
    Then the customer should exist

  Scenario: User Does Not Exist in SOAP Service
    Given a customer with CPR Number "123456-7891"
    When the bank service is queried for the customer
    Then the customer should not exist

  Scenario: Successful Account Payment
    Given a customer with a bank account with balance 1000
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000
    And that the merchant is registered with DTU Pay
    When the merchant starts a payment for 100 kr by the customer
    Then the payment succeeds
    And the balance of the customer at the bank is 900 kr
    And the balance of the merchant at the bank is 2100 kr