package bshutt.coplan;

import bshutt.coplan.exceptions.HandlerMissingArgumentsException;
import bshutt.coplan.exceptions.JwtException;
import bshutt.coplan.exceptions.MiddlewareException;
import bshutt.coplan.exceptions.UserDoesNotExistException;
import bshutt.coplan.handlers.Courses;
import bshutt.coplan.handlers.Users;
import bshutt.coplan.middleware.Middleware;
import bshutt.coplan.middleware.Middlewares;

import java.util.*;


public class Router {

    private static Router instance = new Router();
    private Map<String, Transaction> routes;

    private Router() {
        this.routes = new HashMap<String, Transaction>();
    }

    public static Router getInstance() {
        return Router.instance;
    }

    public Router register(String key, Handler handler) {
        Transaction transaction = new Transaction(handler, null, null);
        routes.put(key, transaction);
        return this;
    }

    public Router register(String key, Handler handler, String[] args) {
        ArrayList<String> reqArgs = new ArrayList<String>(Arrays.asList(args));
        Transaction transaction = new Transaction(handler, reqArgs, null);
        routes.put(key, transaction);
        return this;
    }

    public Router register(String key, Handler handler, String[] args, Middleware[] middlewares) {
        ArrayList<String> reqArgs = new ArrayList<String>(Arrays.asList(args));
        Transaction transaction = new Transaction(handler, reqArgs, middlewares);
        routes.put(key, transaction);
        return this;
    }

    public Response route(Request req) {
        Transaction transaction = this.routes.get(req.route);

        if (transaction == null) {
            Response res = new Response(req)
                .append("errorMessage", "No handler found for route")
                .end(false);
            return res;
        }
        try {
            transaction.init(req);
        } catch (HandlerMissingArgumentsException hmae) {
            Response res = new Response(req)
                    .append("errorMessage", "Missing required arguments for this handler. Required arguments for '" +
                            req.route + "' are: " + transaction.args.toString())
                    .append("request", req.toString())
                    .end(false);
            return res;
        }

        try {
            transaction.complete();
        } catch (Exception exc) {
            Response res = new Response(req)
                    .err("Uncaught error fell through.", exc);
            return res;
        }

        if (!transaction.response.isDone) {
            transaction.response.end();
        }
        return transaction.response;
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
        Middlewares middlewares = new Middlewares();

        this.register("getUser",
                users.getUser,
                new String[]{"username"});

        this.register("getUserDetails",
                users.getUserDetails,
                new String[]{"jwt"},
                new Middleware[]{middlewares.userFromJwt});

        this.register("usernameIsAvailable",
                users.usernameIsAvailable,
                new String[]{"username"});

        this.register("createUser",
                users.createUser,
                new String[]{"username", "firstName", "lastName", "password"});

        this.register("removeUser",
                users.removeUser,
                new String[]{"username"});

        this.register("login",
                users.login,
                new String[]{"username", "password"});

        this.register("checkJwt",
                users.checkJwt,
                new String[]{"jwt"});

        this.register("registerForCourse",
                users.registerForCourse,
                new String[]{"courseName", "jwt"},
                new Middleware[]{middlewares.userFromJwt});

        this.register("unregisterForCourse",
                users.unregisterForCourse,
                new String[]{"courseName", "jwt"},
                new Middleware[]{middlewares.userFromJwt});

        this.register("getAllUsers",
                users.getAllUsers);

        this.register("createCourse",
                courses.createCourse,
                new String[]{"courseName"});

        this.register("getCourse",
                courses.getCourse,
                new String[]{"courseName"});

        this.register("deleteCourse",
                courses.deleteCourse,
                new String[]{"courseName"});

        this.register("getAllCourses",
                courses.getAllCourses);

        this.register("refreshJwt",
                users.refreshJwt,
                new String[]{"jwt"},
                new Middleware[]{middlewares.userFromJwt});

    }
}

class Transaction {
    public Handler handler;
    public ArrayList<String> args;
    public ArrayList<Middleware> middlewares;
    public Request request;
    public Response response;

    Transaction(Handler handler, ArrayList args, Middleware[] middlewares) {
        this.handler = handler;
        this.args = args;
        if (middlewares == null) {
            this.middlewares = null;
        } else {
            this.middlewares = new ArrayList<Middleware>(Arrays.asList(middlewares));
        }

    }

    public Response complete() {
        if (this.request == null) return null;

        final boolean[] middlewareInducedFatalError = {false};
        if (this.middlewares != null) {
            this.middlewares.forEach(middleware -> {
                try {
                    middleware.Do(this.request, this.response);
                } catch (MiddlewareException e) {
                    this.response.err("Error occurred in the middleware.", e);
                    middlewareInducedFatalError[0] = true;
                }
            });
        }

        if (middlewareInducedFatalError[0])
            return null;

        try {
            this.handler.handle(this.request, this.response);
        } catch (Exception exc) {
            exc.printStackTrace();
            this.response.err(exc);
        }
        return this.response;
    }

    public boolean init(Request req) throws HandlerMissingArgumentsException {
        this.response = new Response(req);
        this.request = req;
        if (this.args == null || req.data.keySet().containsAll(this.args)) {
            return true;
        } else
            throw new HandlerMissingArgumentsException();
    }
}

