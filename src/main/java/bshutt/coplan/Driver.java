package bshutt.coplan;

import bshutt.coplan.models.User;
import bshutt.coplan.models.UserModel;
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
        System.out.println("Hi, I'm the Java App! I'm here handle all your business logic!");



        Router router = Router.getInstance();
        setupRoutes(router);

//        router.register("Users.createUser", (req, res) -> {
//            req.user = new User(req.data.getString("username"));
//            req.users.loadAttributes(req.user);
//            System.out.println(req.user);
//
//        });

        Reader reader = new CliReader();
        reader.listen((req) -> {
            System.out.println("Processing a new command!");
            System.out.println(req);
            router.route(req);
        });

        //      do {
        //         cmd = new UserIAction(nextInput());
        //         System.out.println("Got a new command!");
        //         cmd.printCommand();
        //         cmd = new UserIAction(nextInput());
        //      } while (cmd != null);
    }

    static void setupRoutes(Router router) {
        UserModel userModel = UserModel.getInstance();
        router.register("Users.readUser", (req, res) -> {
            Document user = userModel.getUser(req.data.getString("username"));
            res.include("username", user.getString("username"))
                    .include("age", user.get("age"))
                    .include("school", user.getString("school"))
                    .include("firstName", user.getString("firstName"))
                    .include("lastName", user.getString("lastName"));
            res.end();
        });
    }


}
