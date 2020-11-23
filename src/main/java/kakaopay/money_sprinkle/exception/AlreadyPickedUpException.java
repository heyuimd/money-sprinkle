package kakaopay.money_sprinkle.exception;

public class AlreadyPickedUpException extends RuntimeException {
    public AlreadyPickedUpException() {
    }

    public AlreadyPickedUpException(String message) {
        super(message);
    }

    public AlreadyPickedUpException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyPickedUpException(Throwable cause) {
        super(cause);
    }

    public AlreadyPickedUpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
