package kakaopay.money_sprinkle.exception;

public class TimePassedException extends RuntimeException {
    public TimePassedException() {
    }

    public TimePassedException(String message) {
        super(message);
    }

    public TimePassedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimePassedException(Throwable cause) {
        super(cause);
    }

    public TimePassedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
