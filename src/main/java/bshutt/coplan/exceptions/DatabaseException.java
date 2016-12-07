package bshutt.coplan.exceptions;

public class DatabaseException extends Exception {
    public DatabaseException(String msg) {
        super(msg);
    }
    public DatabaseException() {
        super();
    }
    public DatabaseException(String s, Exception exc) {
        super(s, exc);
    }
}
