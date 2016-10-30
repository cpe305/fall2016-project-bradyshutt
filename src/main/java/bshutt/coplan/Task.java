package bshutt.coplan;

import com.mongodb.util.JSON;
import com.mongodb.util.JSONSerializers;
import org.bson.json.JsonReader;

public class Task {

  public String subsystem;
  public String action;
  public String data;

  public Task(String subsystem, String action, String data) {
    this.subsystem = subsystem;
    this.action = action;
    this.data = data;
  }

  @Override
  public String toString() {
    String str = "<Task>:"
        + "\n\tSubsystem: " + this.subsystem
        + "\n\tAction: " + this.action
        + "\n\tData: " + this.data;
    return str;
  }
}
