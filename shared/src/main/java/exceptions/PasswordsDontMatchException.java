package exceptions;

public class PasswordsDontMatchException extends RuntimeException {
    public PasswordsDontMatchException(String message) {
        super(message);
    }
    public PasswordsDontMatchException(String message, Throwable ex){
        super(message, ex);
    }
}
