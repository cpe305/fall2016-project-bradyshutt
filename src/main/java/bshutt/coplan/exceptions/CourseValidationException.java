package bshutt.coplan.exceptions;

public class CourseValidationException extends Exception {
    public CourseValidationException(String message) {
        super(message);
    }
    public CourseValidationException(String message, Exception exc) {
        super(message, exc);
    }
    public CourseValidationException() {
        super("There was an error validating this course");
    }
    public CourseValidationException(Exception exc) {
        super("There was an error validating this course", exc);
    }

}
