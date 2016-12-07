package bshutt.coplan.exceptions;

public class UserDoesNotExistException extends Exception {
    public UserDoesNotExistException(String msg) {
        super(msg);
    }
    public UserDoesNotExistException() {
        super();
    }
}
