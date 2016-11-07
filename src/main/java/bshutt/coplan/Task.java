package bshutt.coplan;

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
