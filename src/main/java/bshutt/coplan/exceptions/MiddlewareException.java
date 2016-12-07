package bshutt.coplan.exceptions;

public class MiddlewareException extends Exception {
    public MiddlewareException(String message) {
        super(message);
    }
    public MiddlewareException(String message, Exception exc) {
        super(message, exc);
    }
    public MiddlewareException() {
        super("Middleware Exception");
    }

}
