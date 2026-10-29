package banking;

public abstract class BankAccount {
    private String pin;
    protected double balance;
    private String accountHolderName;
    private String accountNumber;


    public BankAccount(String pin, double balance, String name,
                       String number) throws InvalidTransactionException {
        if (pin == null || !pin.matches("\\d{4}")) {
            TransactionLogger.log(number, "Open an account", balance, 0,
                    "Rejected because Pin must be 4 digits");
            throw new InvalidTransactionException(
                    "Pin must be exactly 4 digits");
        }

        if (balance < 0) {
            TransactionLogger.log(number, "Open an account", balance, 0,
                    "Rejected because the opening balance is negative");
            throw new InvalidTransactionException(
                    "Opening balance cannot be negative");
        }

        if (name == null || name.trim().isEmpty()
                || number == null || number.trim().isEmpty()) {
            throw new InvalidTransactionException(
                    "Name and account number are required.");
        }

        this.pin = pin;
        this.balance = balance;
        this.accountHolderName = name.trim();
        this.accountNumber = number.trim();

        TransactionLogger.log(accountNumber, "Open an account",
                balance, balance, "Successful");
    }


    public BankAccount(String accountNumber,double initialLoan) {
    }

    public String getAccountNumber() {

        return accountNumber;
    }

    public String getAccountHolderName() {

        return accountHolderName;
    }

    public double getBalance() {

        return balance;
    }

    public boolean checkPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    public void setAccountHolderName(String name)
            throws InvalidTransactionException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidTransactionException(
                    "Account holder name cannot be empty");
        }
        accountHolderName = name.trim();
    }

    protected void setBalance(double newBalance) {
        balance = newBalance;
    }


    public abstract void deposit(double amount)
            throws InvalidTransactionException;

    public abstract void withdraw(double amount)
            throws InvalidTransactionException,
            InsufficientFundsException;

    public abstract double calculateInterest();

    public abstract String getAccountType();


    public void transfer(BankAccount target, double amount)
            throws InvalidTransactionException,
            InsufficientFundsException {

        if (target == null || target == this || amount <= 0) {
            String message;

            if (target == null) {
                message = "Target account was not found";
            } else if (target == this) {
                message = "Cannot transfer to the same account";
            } else {
                message = "Transfer amount must be greater than zero";
            }

            TransactionLogger.log(accountNumber, "Transfer",
                    amount, balance, "Unaccepted " + message);

            throw new InvalidTransactionException(message);
        }

        try {
            withdraw(amount);
            target.deposit(amount);

            TransactionLogger.log(accountNumber, "Transfer",
                    amount, balance,
                    "Successfully sent to " + target.getAccountNumber());

        } catch (InvalidTransactionException
                 | InsufficientFundsException e) {
            TransactionLogger.log(accountNumber, "Transfer",
                    amount, balance, "Unaccepted " + e.getMessage());
            throw e;
        }
    }

    public void displayAccountDetails() {
        System.out.println("Account number : " + accountNumber);
        System.out.println("Holder         : " + accountHolderName);
        System.out.println("Account type   : " + getAccountType());
        System.out.printf("Balance        : M%.2f%n", balance);

    }

}
