package bshutt.coplan;

import java.io.BufferedReader;
import java.io.InputStreamReader;

interface InputAction {
  void act(Task task);
}

public abstract class Interpreter {

  private InputStreamReader isr;
  private BufferedReader bufferReader;

  public Interpreter() {
    isr = new InputStreamReader(System.in);
    bufferReader = new BufferedReader(isr);
  }

  void listen(InputAction action) {
    String nextInput;
    while (true) {
      try {
        if ((nextInput = bufferReader.readLine()) != null) {
          Task cmd = this.interpret(nextInput);
          action.act(cmd);
        }
      } catch (Exception exception) {
        System.out.println(exception);
        //exception.printStackTrace();
      }
    }
  }

  public abstract Task interpret(String format) throws Exception;

}
