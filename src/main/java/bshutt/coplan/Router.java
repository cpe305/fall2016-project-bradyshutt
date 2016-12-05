package bshutt.coplan;

import bshutt.coplan.handlers.Courses;
import bshutt.coplan.handlers.Users;
import org.bson.Document;

import java.util.*;


public class Router {

    private static Router instance = new Router();
    private Map<String, HandlerWrapper> routes;

    private Router() {
        this.routes = new HashMap<String, HandlerWrapper>();
    }

    public static Router getInstance() {
        return Router.instance;
    }

    public Router register(String key, Handler handler) {
        HandlerWrapper handlerArgs = new HandlerWrapper(handler, null);
        routes.put(key, handlerArgs);
        return this;
    }

    public Router register(String key, Handler handler, String[] args) {
        ArrayList<String> reqArgs = new ArrayList<String>(Arrays.asList(args));
        HandlerWrapper handlerArgs = new HandlerWrapper(handler, reqArgs);
        routes.put(key, handlerArgs);
        return this;
    }

    public Response route(Request req) {
        Response res = new Response();
        HandlerWrapper handlerArgs = this.routes.get(req.route);
        if (handlerArgs == null) {
            res.err("No handler found for route '" + req.route, req);
        } else {
            if (handlerArgs.args == null || req.data.keySet().containsAll(handlerArgs.args)) {
                try {
                    handlerArgs.handler.handle(req, res);
                    System.out.println(res);
                } catch (Exception exc) {
                    res.err(exc);
                }
            } else {
                res.err("Missing required arguments for this handler. Required arguments for '" + req.route + "' are: " + handlerArgs.args.toString(), req);
            }

            if (!res.isDone)
                res.end();
        }
        return res;
    }

    public Handler getHandler(String handler) {
        return this.routes.get(handler).handler;
    }

    public boolean routeExists(String route) {
        return (this.routes.get(route) != null);
    }

    public void setupRoutes() {
        Users users = new Users();
        Courses courses = new Courses();

        this.register("getUser", users.getUser, new String[]{"username"});
        this.register("usernameIsAvailable", users.usernameIsAvailable, new String[]{"username"});
        this.register("createUser", users.createUser, new String[]{"username", "firstName", "lastName", "password"});
        this.register("removeUser", users.removeUser, new String[]{"username"});
        this.register("login", users.login, new String[]{"username", "password"});
        this.register("checkJwt", users.checkJwt, new String[]{"jwt"});
        this.register("registerForCourse", users.registerForCourse, new String[]{"username", "courseName"});
        this.register("unregisterForCourse", users.unregisterForCourse, new String[]{"username", "courseName"});
        this.register("getAllUsers", users.getAllUsers);
        this.register("createCourse", courses.createCourse, new String[]{"courseName"});
        this.register("getCourse", courses.getCourse, new String[]{"courseName"});
        this.register("deleteCourse", courses.deleteCourse, new String[]{"courseName"});
        this.register("getAllCourses", courses.getAllCourses);

    }
}

class HandlerWrapper {
    public Handler handler;
    public ArrayList<String> args;

    HandlerWrapper(Handler handler, ArrayList args) {
        this.handler = handler;
        this.args = args;
    }
}

