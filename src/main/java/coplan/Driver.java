package coplan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {

  public enum Subsystem {
    USER, COURSE, OTHER
  }

  /**
   * Main Driver.
   *
   * @param args string arguments.
   */
  public static void main(String[] args) {
    System.out.println("Hi, I'm the Java App! I'm here handle all your business logic!");

    Router router = Router.getInstance();

    listen(router);
    //      do {
    //         cmd = new UserIAction(nextInput());
    //         System.out.println("Got a new command!");
    //         cmd.printCommand();
    //         cmd = new UserIAction(nextInput());
    //      } while (cmd != null);
  }

  private static void nextInput() {
    BufferedReader bufferReader;
    String command;
    System.out.println("Listening!");
    try {
      bufferReader = new BufferedReader(new InputStreamReader(System.in));
      command = bufferReader.readLine();
    } catch (IOException err) {
      err.printStackTrace();
      command = null;
    }
  }

  static void listen(Router router) {
    BufferedReader bufferReader;
    try {
      bufferReader = new BufferedReader(new InputStreamReader(System.in));
      while (true) {
        String command = bufferReader.readLine();
        if (command != null) {
          System.out.println("Processing a new command!");
          System.out.println(command);
          //router.route(command);
        }
      }
    } catch (IOException err) {
      err.printStackTrace();
    }
  }



}
