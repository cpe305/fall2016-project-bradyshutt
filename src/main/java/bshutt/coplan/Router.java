package bshutt.coplan;

public class Router {

  private static Router instance = new Router();

  private Router() { }

  public static Router getInstance() {
    return Router.instance;
  }

  public void route(Request req) {
    String subsystem = req.getSubsystem();

    switch (subsystem) {
      case "user":
        System.out.println("Doing user action");
        break;

      case "course":
        System.out.println("Doing course action");
        break;

      case "admin":
        System.out.println("Doing administration action");
        break;

      case "other":
        System.out.println("Doing something else action");
        break;

      default:
        System.out.println("Unrecognized command...");
    }
  }
}
