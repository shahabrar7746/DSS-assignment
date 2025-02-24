package exceptions;

public class NoAdminFoundException extends RuntimeException {
    public NoAdminFoundException(String message) {
        super(message);
    }
}
