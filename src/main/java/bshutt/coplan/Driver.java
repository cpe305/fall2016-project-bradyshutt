package bshutt.coplan;

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
        setupRoutes(router);

        Reader reader = new Reader(new JsonReaderStrategy());
        reader.listen((req) -> {
            router.route(req);
        });

    }

    static void setupRoutes(Router router) {
        Users users = new Users();

        router.register("getUser", users.getUser);
        //.register("readUser", getUser)
        router.register("usernameIsAvailable", users.usernameIsAvailable);
        router.register("createUser", users.createUser);
        router.register("authenticate", users.authenticate);
    }


}
