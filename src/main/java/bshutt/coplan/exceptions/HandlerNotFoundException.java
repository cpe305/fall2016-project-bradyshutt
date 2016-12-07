package bshutt.coplan.exceptions;

public class HandlerNotFoundException extends RuntimeException {
    public HandlerNotFoundException(String msg) {
        super(msg);
    }
    public HandlerNotFoundException() {
        super();
    }
}
