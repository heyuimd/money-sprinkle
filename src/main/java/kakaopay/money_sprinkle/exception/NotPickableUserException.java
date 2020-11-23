package kakaopay.money_sprinkle.exception;

public class NotPickableUserException extends RuntimeException {
    public NotPickableUserException() {
    }

    public NotPickableUserException(String message) {
        super(message);
    }

    public NotPickableUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotPickableUserException(Throwable cause) {
        super(cause);
    }

    public NotPickableUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
