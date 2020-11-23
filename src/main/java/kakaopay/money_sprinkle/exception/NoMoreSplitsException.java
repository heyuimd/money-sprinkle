package kakaopay.money_sprinkle.exception;

public class NoMoreSplitsException extends RuntimeException {
    public NoMoreSplitsException() {
    }

    public NoMoreSplitsException(String message) {
        super(message);
    }

    public NoMoreSplitsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMoreSplitsException(Throwable cause) {
        super(cause);
    }

    public NoMoreSplitsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
