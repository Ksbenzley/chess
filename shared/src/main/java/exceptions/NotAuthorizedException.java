package exceptions;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String message) {
        super(message);
    }
    public NotAuthorizedException(String message, Throwable ex){
        super(message, ex);
    }
}
