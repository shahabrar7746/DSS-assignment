package exceptions;

public class TrialLimitExceedException extends RuntimeException {

    public TrialLimitExceedException(String message) {
        super(message);
    }
}
