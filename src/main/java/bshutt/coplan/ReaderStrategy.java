package bshutt.coplan;

public interface ReaderStrategy {

    Request interpret(String cmdString);
}
