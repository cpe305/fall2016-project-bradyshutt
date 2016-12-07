package bshutt.coplan.middleware;

import bshutt.coplan.Request;
import bshutt.coplan.Response;
import bshutt.coplan.exceptions.JwtException;
import bshutt.coplan.exceptions.MiddlewareException;
import bshutt.coplan.exceptions.UserDoesNotExistException;
import bshutt.coplan.models.User;

public class Middlewares {

    public Middleware userFromJwt = (req, res) -> {
        String jwt = req.get("jwt");
        if (jwt == null) {
            res.err("Error getting user from JWT: Missing JWT in request.");
            return;
        } else {
            try {
                req.user = User.loadFromJwt(jwt);
            } catch (UserDoesNotExistException e) {
                throw new MiddlewareException("JWT err: The user for that JWT doesn't exist", e);
            } catch (JwtException e) {
                throw new MiddlewareException("JWT err", e);
            }
        }

        if (req.user == null) {
            throw new MiddlewareException("Couldn't get user from that JWT");
        }
    };




}
