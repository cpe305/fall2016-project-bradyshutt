package bshutt.coplan.exceptions;

public class HandlerMissingArgumentsException extends Exception {
    public HandlerMissingArgumentsException(String msg) {
        super(msg);
    }
    public HandlerMissingArgumentsException() {
        super();
    }

}
