package bshutt.coplan;

import bshutt.coplan.exceptions.HandlerMissingArgumentsException;
import bshutt.coplan.exceptions.MiddlewareException;
import bshutt.coplan.middleware.Middleware;

import java.util.ArrayList;
import java.util.Arrays;

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
            this.middlewares = new ArrayList<>(Arrays.asList(middlewares));
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
        if ((this.args == null) || (req.data.keySet().containsAll(this.args))) {
            if (req.data.containsValue(null))
                throw new HandlerMissingArgumentsException("contains null arg");
            else
                return true;
        } else {
            throw new HandlerMissingArgumentsException();
        }
    }
}
