package banking;

public class LoanAccount extends BankAccount implements Loanable {

    private double interestRate;
    private double loanLimit = 100000;

    public LoanAccount(String accountNumber, double initialLoan, double interestRate) {
        super(accountNumber, initialLoan);
        this.interestRate = interestRate;
    }

    @Override
    public void deposit(double amount) throws InvalidTransactionException {
        if (amount <= 0) throw new InvalidTransactionException("Repayment must be positive");
        balance-=amount;
    }

    @Override
    public void withdraw(double amount) throws InvalidTransactionException, InsufficientFundsException {
        if (amount <= 0) throw new InvalidTransactionException("Loan must be positive");
        if (getBalance() + amount > loanLimit) {
            throw new InsufficientFundsException("Loan limit exceeded");
        }

        setBalance(getBalance() + amount);
    }

    @Override
    public void requestLoan(double amount) throws LoanRejectedException, InvalidTransactionException {
        if (amount <= 0) throw new InvalidTransactionException("Amount must be positive");
        if (!isFitForLoan(amount)) {
            throw new LoanRejectedException("Loan Rejected! Limit: " + loanLimit + " Current debt: " + getBalance());
        }
        try {
            withdraw(amount);
        } catch (InsufficientFundsException e) {
            throw new LoanRejectedException(e.getMessage());
        }
    }

    public double calculateEMI(int months) {
        double monthlyRate = interestRate / 12 / 100;
        double principal = getBalance();
        if (monthlyRate == 0) return principal / months;
        return (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) / (Math.pow(1 + monthlyRate, months) - 1);
    }

    public void applyInterest() {
        setBalance(getBalance() + getBalance() * interestRate / 100);
    }

    public boolean isFitForLoan(double amount) {
        return (getBalance() + amount) <= loanLimit;
    }

    @Override
    public double getLoanLimit() {
        return loanLimit;
    }

    @Override
    public double calculateInterest() {
        return getBalance() * interestRate / 100;
    }

    @Override
    public String getAccountType() {
        return "Loan Account";
    }
}