package bshutt.coplan;

import bshutt.coplan.handlers.Courses;
import bshutt.coplan.handlers.Users;
import org.bson.Document;

public class Driver {

    /**
     * Main Driver.
     *
     * @param args string arguments.
     */
    public static void main(String[] args) {
        Document msg = new Document("msg", "Hi, I'm the Java App! I'm here handle all your business logic!");
        System.out.println(msg.toJson());

        Router router = Router.getInstance();
        router.setupRoutes();

        Reader reader = new Reader(new JsonReaderStrategy()); // <--- Strategy Pattern
                                                              // Listens for next input, builds a <Request> object
        try {
            reader.listen(router::route); // <--- Calls router.route(<Request>)
        } catch (Exception exc) {
            Response.log(new Document("exceptionMessage", exc.getMessage())
                    .append("stackTrace", exc.getStackTrace())
                    .append("cause", exc.getCause()));
        }

    }
}
