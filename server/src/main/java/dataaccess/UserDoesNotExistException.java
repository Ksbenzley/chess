package dataaccess;

/*
indicates whether a user is in the database or not
 */

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String message) {
        super(message);
    }
    public UserDoesNotExistException(String message, Throwable ex){
        super(message, ex);
    }
}
