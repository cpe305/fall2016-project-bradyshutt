package bshutt.coplan;

interface ReaderStrategy {
    Request interpret(String cmdString);
}
