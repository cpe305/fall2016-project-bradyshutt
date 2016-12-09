package bshutt.coplan.exceptions;

public abstract class Exc extends Exception {

    public Exc() {
        super("An error occurred while running middleware.");
    }

    public Exc(Exception exc) {
        super("An error occurred while running middleware: " + exc.getMessage(), exc);
    }

    public Exc(String message, Exception exc) {
        super(message + ": " + exc.getMessage(), exc);
    }

    public Exc(String message) {
        super(message);
    }

}
