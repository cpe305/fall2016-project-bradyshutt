package bshutt.coplan.exceptions;

public class JwtException extends Exception {
    public JwtException(String message) {
        super(message);
    }
    public JwtException() {
        super();
    }
    public JwtException(String message, Exception exc) {
        super(message, exc);
    }
}
