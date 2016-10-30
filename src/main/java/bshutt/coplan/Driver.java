package bshutt.coplan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {

  /**
   * Main Driver.
   *
   * @param args string arguments.
   */
  public static void main(String[] args) {
    System.out.println("Hi, I'm the Java App! I'm here handle all your business logic!");

    //Router router = Router.getInstance();
    //Controller controller = Controller.getInstance();

    //if (args[2] != null && args[2].equals("cli")) {
    //  controller.setInputType("cli");
    //}

    Interpreter interpreter = new InteractiveInterpreter();
    interpreter.listen((task) -> {
      System.out.println("Processing a new command!");
      System.out.println(task);
    });

    //      do {
    //         cmd = new UserIAction(nextInput());
    //         System.out.println("Got a new command!");
    //         cmd.printCommand();
    //         cmd = new UserIAction(nextInput());
    //      } while (cmd != null);
  }




}
