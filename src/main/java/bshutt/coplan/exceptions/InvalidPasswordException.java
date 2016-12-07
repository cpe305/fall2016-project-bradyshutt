package bshutt.coplan.exceptions;

/**
 * Created by bshutt on 12/5/16.
 */
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String msg) {
        super(msg);
    }
    public InvalidPasswordException() {
        super();
    }
}
