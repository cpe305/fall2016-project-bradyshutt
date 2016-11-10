package bshutt.coplan;

import org.bson.Document;

import java.util.*;


class HandlerArgs {
    public Handler handler;
    public ArrayList<String> args;

    public HandlerArgs(Handler handler, ArrayList args) {
        this.handler = handler;
        this.args = args;
    }
}


public class Router {

    private static Router instance = new Router();
    private Map<String, HandlerArgs> routes;

    private Router() {
        this.routes = new HashMap<String, HandlerArgs>();
    }

    public static Router getInstance() {
        return Router.instance;
    }

    public Router register(String key, Handler handler) {
        HandlerArgs handlerArgs = new HandlerArgs(handler, null);
        routes.put(key, handlerArgs);
        return this;
    }

    public Router register(String key, Handler handler, String[] args) {
        ArrayList<String> reqArgs = new ArrayList<String>(Arrays.asList(args));
        HandlerArgs handlerArgs = new HandlerArgs(handler, reqArgs);
        routes.put(key, handlerArgs);
        return this;
    }

    public void route(Request req) {
        Response res = new Response();
        HandlerArgs handlerArgs = this.routes.get(req.route);
        if (handlerArgs == null) {
            res.err("No handler found for route '" + req.route, req);
        } else {
            if (handlerArgs.args == null || req.data.keySet().containsAll(handlerArgs.args)) {
                try {
                    handlerArgs.handler.handle(req, res);
                } catch (Exception exc) {
                    res.err(exc);
                }
            } else {
                res.err("Missing required arguments for this handler. Required arguments for '" + req.route + "' are: " + handlerArgs.args.toString(), req);
            }

            if (!res.isDone)
                res.end();
        }

    }


}


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
