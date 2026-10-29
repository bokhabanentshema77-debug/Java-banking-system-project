package banking;

public class LoanRejectedException extends RuntimeException {
    public LoanRejectedException(String message) {
        super(message);
    }
}
