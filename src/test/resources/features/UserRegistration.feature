Feature: User Registration
  This feature tests the registration process for new supporters.
  It covers both successful registration and various negative scenarios such as missing last name, password mismatch, and not accepting the terms and conditions.

  # Positive Scenario: Successful Registration
  Scenario: Successful registration creates a new account
    Given I navigate to the registration page
    When I enter valid registration details:
      | field           | value                    |
      | firstName       | John                     |
      | lastName        | Doe                      |
      | email           | john.doe@example.com     |
      | password        | P@ssw0rd                 |
      | confirmPassword | P@ssw0rd                 |
    And I accept the terms and conditions
    And I submit the registration form
    Then I should see a success message

  # Negative Scenarios: Using Scenario Outline for various error cases
  Scenario Outline: Unsuccessful registration due to invalid inputs
    Given I navigate to the registration page
    When I enter registration details:
      | field           | value                    |
      | firstName       | John                     |
      | lastName        | <lastName>               |
      | email           | john.doe@example.com     |
      | password        | <password>               |
      | confirmPassword | <confirmPassword>        |
    And I set the terms and conditions acceptance to <termsAccepted>
    And I submit the registration form
    Then I should see an error message "<expectedError>"

    Examples:
      | lastName | password  | confirmPassword | termsAccepted | expectedError                             |
      |          | P@ssw0rd  | P@ssw0rd        | true          | Last name is required                     |
      | Doe      | P@ssw0rd  | DifferentP@ss   | true          | Passwords do not match                    |
      | Doe      | P@ssw0rd  | P@ssw0rd        | false         | You must accept the terms and conditions  |
