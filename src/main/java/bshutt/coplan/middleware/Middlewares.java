package bshutt.coplan.middleware;

import bshutt.coplan.exceptions.*;
import bshutt.coplan.models.Course;
import bshutt.coplan.models.User;

public class Middlewares {

    public Middleware loadUserJwt = (req, res) -> {
        String jwt = req.getData("jwt");
        if (jwt == null) {
            res.err("Error getting user from JWT: Missing JWT in request.");
            return;
        } else {
            try {
                req.user = User.loadFromJwt(jwt);
            } catch (UserDoesNotExistException | JwtException e) {
                throw new MiddlewareException("Middleware error loading user", e);
            }
        }

        if (req.user == null) {
            throw new MiddlewareException("Couldn't getData user from that JWT", null);
        }
    };

    public Middleware loadUsersCourses = (req, res) -> {

    };

//    public Middleware loadCourse = (req, res) -> {
//        String courseName = req.getData("courseName");
//        if (courseName == null) {
//            res.err("Error getting course from request: Missing courseName in request.");
//            return;
//        } else {
//            try {
//                req.course = Course.load(courseName);
//            } catch (CourseValidationException | DatabaseException e) {
//                throw new MiddlewareException("Middleware error loading course", e);
//            }
//        }
//
//        if (req.user == null) {
//            throw new MiddlewareException("Couldn't getData user from that JWT", null);
//        }
//    };

}
