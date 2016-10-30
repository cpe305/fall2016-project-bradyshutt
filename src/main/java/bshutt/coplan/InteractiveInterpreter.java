package bshutt.coplan;

public class InteractiveInterpreter extends Interpreter {

  private String malformed = "Malformed input command."
      + "\nFormat:\n\t\"<subsystem> <action> [<data>...]\"";

  @Override
  public Task interpret(String cmdString) throws Exception {
    String[] cmdArray = cmdString.split(" ");
    String data;
    if (cmdArray.length == 2) {
      data = null;
    } else if (cmdArray.length > 2) {
      data = "[";
      for (int idx = 2; idx < cmdArray.length; idx++) {
        data += cmdArray[idx];
        if (idx < cmdArray.length - 1) {
          data += ",";
        }
      }
      data += "]";
    } else {
      System.out.println(cmdString);
      throw new Exception(malformed);
    }

    Task cmd = new Task(
        cmdArray[0],
        cmdArray[1],
        data
    );

    return cmd;
  }
}
