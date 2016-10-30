package bshutt.coplan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Controller {

  private static Controller instance = new Controller();
  private static String inputType;

  protected Controller() { }

  public static Controller getInstance() {
    return Controller.instance;
  }

  public void listen(Router router) {
//    BufferedReader bufferReader;
//    try {
//      bufferReader = new BufferedReader(new InputStreamReader(System.in));
//      while (true) {
//        String req = bufferReader.readLine();
//        if (req != null) {
//          //System.out.println("Processing a new command!");
//          //System.out.println(command);
//          router.route(new Request(req));
//        }
//      }
//    } catch (IOException err) {
//      err.printStackTrace();
  }

  public void setInputType(String type) {
    Controller.inputType = type;
  }

}
