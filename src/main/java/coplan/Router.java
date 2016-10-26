package coplan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Router {

  /**
   * Route string command to proper handler.
   *
   * @param cmd is the string command.
   */
  public void route(String cmd) {
    String[] command = cmd.split(" ");
    // Subsystem the command is intended for
    String subsystem = command[0];
    System.out.println(command[0]);
    if (subsystem.equals("user")) {
      System.out.println("Do something with the user subsystem.");
      System.out.println("UserIAction to do: " + cmd);
    } else if (subsystem.equals("course")) {
      System.out.println("Do something with the course subsystem.");
      System.out.println("UserIAction to do: " + cmd);
    } else if (subsystem.equals("other")) {
      System.out.println("Do something with some other subsystem.");
      System.out.println("UserIAction to do: " + cmd);
    } else {
      System.out.println("Unrecognized command...");
    }
  }
}
