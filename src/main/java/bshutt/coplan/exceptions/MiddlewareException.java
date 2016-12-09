package bshutt.coplan.exceptions;

public class MiddlewareException extends Exception {

    public MiddlewareException(Exception exc) {
        super("An error occurred while running middleware: " + exc.getMessage(), exc);
    }

    public MiddlewareException(String msg, Exception exc) {
        super(msg + "; " + ((exc != null) ? exc.getMessage() : ""), exc);
    }
}
