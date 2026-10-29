package banking;

public interface Loanable {
    double getLoanLimit();
    void requestLoan(double amount) throws LoanRejectedException, InvalidTransactionException;
}