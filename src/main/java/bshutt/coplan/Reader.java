package bshutt.coplan;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class Reader {

  private InputStreamReader isr;
  private BufferedReader bufferReader;

  public Reader() {
    isr = new InputStreamReader(System.in);
    bufferReader = new BufferedReader(isr);
  }

  void listen(Callback cb) {
    String nextInput;
    while (true) {
      try {
        if ((nextInput = bufferReader.readLine()) != null) {
          Request req = this.deserialize(nextInput);
          cb.cb(req);
        }
      } catch (Exception exception) {
        System.out.println(exception);
        //exception.printStackTrace();
      }
    }
  }

  public abstract Request deserialize(String format) throws Exception;

}
