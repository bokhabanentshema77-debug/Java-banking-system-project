package banking;

public class SavingsAccount extends BankAccount implements Loanable {
    private static final double MinimumBalance = 100.00;
    private static final double InterestRate = 0.05;
    private static final double LoanMultiplier= 3.0;

    public SavingsAccount(String pin, double balance, String name,
                          String number)
            throws InvalidTransactionException {
        super(pin, balance, name, number);
    }

    @Override
    public void deposit(double amount)
            throws InvalidTransactionException {
        if (amount <= 0) {
            TransactionLogger.log(getAccountNumber(), "Cash in",
                    amount, getBalance(), "Amount must be positive");
            throw new InvalidTransactionException(
                    "Cash in must be greater than zero");
        }

        setBalance(getBalance() + amount);
        TransactionLogger.log(getAccountNumber(), "Cash in",
                amount, getBalance(), "Successful cash in");
    }

    @Override
    public void withdraw(double amount)
            throws InvalidTransactionException,
            InsufficientFundsException {
        if (amount <= 0) {
            TransactionLogger.log(getAccountNumber(), "Cash out",
                    amount, getBalance(), "The amount should be positive");
            throw new InvalidTransactionException(
                    "Cash withdrawal should always be greater than zero");
        }

        if (getBalance() - amount < MinimumBalance) {
            TransactionLogger.log(getAccountNumber(), "Cash out",
                    amount, getBalance(),
                    "Minimum balance is M100.00");
            throw new InsufficientFundsException(
                    "Savings account must keep at least M100.00");
        }

        setBalance(getBalance() - amount);
        TransactionLogger.log(getAccountNumber(), "Cash out",
                amount, getBalance(), "Success");
    }

    @Override
    public double calculateInterest() {
        double interest = getBalance() * InterestRate;
        setBalance(getBalance() + interest);

        TransactionLogger.log(getAccountNumber(), "Interest",
                interest, getBalance(), "Attainment: 5% interest added");

        return interest;
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }



    public double getLoanLimit() {
        return getBalance() * LoanMultiplier;
    }


    public void requestLoan(double amount)
            throws LoanRejectedException {
        if (amount <= 0 || amount > getLoanLimit()) {
            String message;

            if (amount <= 0) {
                message = "Loan amount should not be negative";
            } else {
                message = String.format(
                        "Loan exceeds your limit of M%.2f",
                        getLoanLimit());
            }

            TransactionLogger.log(getAccountNumber(), "Loan",
                    amount, getBalance(), "Declined: " + message);
            throw new LoanRejectedException(message);
        }

        setBalance(getBalance() + amount);
        TransactionLogger.log(getAccountNumber(), "Loan",
                amount, getBalance(), "Delivered: loan approved");
    }
}

