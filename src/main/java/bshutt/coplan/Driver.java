package bshutt.coplan;

import bshutt.coplan.handlers.Courses;
import bshutt.coplan.handlers.Users;
import org.bson.Document;

public class Driver {

    /**
     * Main Driver.
     *
     * @param args string arguments.
     */
    public static void main(String[] args) {
        Document msg = new Document("msg", "Hi, I'm the Java App! I'm here handle all your business logic!");
        System.out.println(msg.toJson());

        Router router = Router.getInstance();
        router.setupRoutes();
        //setupRoutes(router);

        Reader reader = new Reader(new JsonReaderStrategy());
        try {
            reader.listen(router::route);
        } catch (Exception exc) {
            Response.log(new Document("exceptionMessage", exc.getMessage())
                    .append("stackTrace", exc.getStackTrace())
                    .append("cause", exc.getCause()));
        }

    }

//    static void setupRoutes(Router router) {
//        Users users = new Users();
//        Courses courses = new Courses();
//
//        router.register("getUser", users.getUser, new String[] {"username"});
//
//        router.register("usernameIsAvailable", users.usernameIsAvailable, new String[] {"username"});
//
//        router.register("createUser", users.createUser, new String[] {"username", "firstName", "lastName"});
//
//        router.register("authenticate", users.authenticate, new String[] {"username", "password"});
//
//        router.register("registerForCourse", users.registerForCourse, new String[] {"username", "courseName"});
//
//        router.register("unregisterForCourse", users.unregisterForCourse, new String[] {"username", "courseName"});
//
//        router.register("getAllUsers", users.getAllUsers);
//
//        router.register("createCourse", courses.createCourse, new String[] {"courseName"});
//
//        router.register("getCourse", courses.getCourse, new String[] {"courseName"});
//
//        router.register("deleteCourse", courses.deleteCourse, new String[] {"courseName"});
//
//        router.register("getAllCourses", courses.getAllCourses);
//    }


}
