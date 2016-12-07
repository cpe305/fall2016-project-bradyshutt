package bshutt.coplan.exceptions;

/**
 * Created by bshutt on 12/6/16.
 */
public class CourseDoesNotExistException extends Exception {
    public CourseDoesNotExistException(String msg) {
        super(msg);
    }
    public CourseDoesNotExistException() {
        super();
    }
}
