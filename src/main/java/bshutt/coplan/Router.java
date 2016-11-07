package bshutt.coplan;

import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

interface Handler {
    void handle(Request req, Response res);
}

public class Router {

    private static Router instance = new Router();
    private Map<String, Handler> routes;

    private Router() {
        this.routes = new HashMap<String, Handler>();
    }

    public static Router getInstance() {
        return Router.instance;
    }


    public void register(String key, Handler handler) {
        routes.put(key, handler);
    }

    public void route(Request req) {
        Response res = new Response();
        this.routes
                .get(req.route)
                .handle(req, res);
        if (!res.isDone)
            res.end();



//    switch (subsystem) {
//      case "user":
//        System.out.println("Doing user action");
//        break;
//
//      case "course":
//        System.out.println("Doing course action");
//        break;
//
//      case "admin":
//        System.out.println("Doing administration action");
//        break;
//
//      case "other":
//        System.out.println("Doing something else action");
//        break;
//
//      default:
//        System.out.println("Unrecognized command...");
//    }
    }
}
