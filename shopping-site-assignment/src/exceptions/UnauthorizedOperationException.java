package exceptions;

public class UnauthorizedOperationException extends Exception {
    public UnauthorizedOperationException(String message) {
        super(message);
    }
}
