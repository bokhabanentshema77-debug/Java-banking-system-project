package banking;

public class CurrentAccount extends BankAccount implements Loanable {
    private static final double Overdraft_limit = 2000.00;
    private static final double Loan_limit = 5000.00;

    public CurrentAccount(String pin, double balance, String accountHolderName, String accountNumber)
            throws InvalidTransactionException {
        super(pin, balance, accountHolderName, accountNumber);
    }


    @Override
    public void deposit(double amount) throws InvalidTransactionException {
        if (amount <= 0) {
            TransactionLogger.log(getAccountNumber(), "Cash in", amount, getBalance(),
                    " Cash in must be greater than zero");
            throw new InvalidTransactionException("Cash in must be greater than zero");
        }
        setBalance(getBalance() + amount);
        TransactionLogger.log(getAccountNumber(), "Cash in", amount, getBalance(), "Successful cash in");
    }

    @Override
    public void withdraw(double amount)
            throws InvalidTransactionException, InsufficientFundsException {
        if (amount <= 0) {
            TransactionLogger.log(getAccountNumber(), "Cash out", amount, getBalance(),
                    "cash out must be greater than zero");
            throw new InvalidTransactionException("cash out must be greater than zero");
        }
        if (getBalance() - amount < -Overdraft_limit) { // cannot pass the overdraft limit
            TransactionLogger.log(getAccountNumber(), "Cash out", amount, getBalance(),
                    "Overdraft limit of M2000.00 exceeded");
            throw new InsufficientFundsException("cash out exceeds the overdraft limit of M2000.00");
        }
        setBalance(getBalance() - amount);
        TransactionLogger.log(getAccountNumber(), "Cash out", amount, getBalance(), "Successful cash out");
    }


    @Override
    public double calculateInterest() {
        TransactionLogger.log(getAccountNumber(), "Interest", 0, getBalance(),
                "current accounts earn none");
        return 0.0;
    }

    @Override
    public String getAccountType() {
        return "Current";
    }

    public double getLoanLimit() {
        return Loan_limit;
    }


    public void requestLoan(double amount) throws LoanRejectedException {
        if (amount <= 0) {
            TransactionLogger.log(getAccountNumber(), "Loan", amount, getBalance(),
                    "loan must be greater than zero");
            throw new LoanRejectedException("Loan amount must be greater than zero");
        }
        if (amount > getLoanLimit()) {
            TransactionLogger.log(getAccountNumber(), "Loan", amount, getBalance(),
                    String.format("Above loan limit of M%.2f", getLoanLimit()));
            throw new LoanRejectedException(
                    String.format("Loan exceeds your limit of M%.2f", getLoanLimit()));
        }
        setBalance(getBalance() + amount);
        TransactionLogger.log(getAccountNumber(), "Loan", amount, getBalance(), "Success: loan approved");
    }
}



