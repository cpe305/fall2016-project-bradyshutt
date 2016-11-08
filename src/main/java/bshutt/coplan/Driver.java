package bshutt.coplan;

import bshutt.coplan.models.User;
import bshutt.coplan.models.UserModel;
import bshutt.coplan.models.Users;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        UserModel userModel = UserModel.getInstance();
        Database db = Database.getInstance();
        Users users = new Users(db);

//        Handler getUser = (req, res) -> {
//            Document user = users.getUser(req.get("username"));
//            res.setResponse(user);
//            res.end();
//        };

        router.register("getUser", users.getUser);
        //.register("readUser", getUser)
        router.register("usernameIsAvailable", users.usernameIsAvailable);
        router.register("createUser", users.createUser);
    }


}
