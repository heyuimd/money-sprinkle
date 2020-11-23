package kakaopay.money_sprinkle.exception;

public class NotEnoughToSplitException extends RuntimeException {
    public NotEnoughToSplitException() {
    }

    public NotEnoughToSplitException(String message) {
        super(message);
    }

    public NotEnoughToSplitException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughToSplitException(Throwable cause) {
        super(cause);
    }

    public NotEnoughToSplitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
