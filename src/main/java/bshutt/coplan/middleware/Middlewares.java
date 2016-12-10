package bshutt.coplan.middleware;

import bshutt.coplan.exceptions.*;
import bshutt.coplan.models.User;

public class Middlewares {

    public Middleware loadUserJwt = (req, res) -> {
        String jwt = req.getData("jwt");
        if (jwt == null) {
            res.err("Error getting user from JWT: Missing JWT in request.");
            return;
        } else {
            try {
                req.setUser( User.loadFromJwt(jwt));
            } catch (UserDoesNotExistException | JwtException e) {
                throw new MiddlewareException("Middleware error loading user", e);
            }
        }

        if (req.getUser() == null) {
            throw new MiddlewareException("Couldn't getData user from that JWT", null);
        }
    };
}
