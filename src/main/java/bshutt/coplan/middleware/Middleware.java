package bshutt.coplan.middleware;

import bshutt.coplan.Request;
import bshutt.coplan.Response;
import bshutt.coplan.exceptions.JwtException;
import bshutt.coplan.exceptions.MiddlewareException;
import bshutt.coplan.exceptions.UserDoesNotExistException;

public interface Middleware {
    void Do(Request req, Response res) throws MiddlewareException;
}
