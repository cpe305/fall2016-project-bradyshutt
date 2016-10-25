package coplan;

interface ICommand {
   void execute();
}

public class Command implements ICommand {
   private String subsystem;
   private String action;

   Command(String subsystem, String action) {
      this.subsystem = subsystem;
      this.action = action;
   }

   public static Command fromJSON(String json) {
      return new Command("test-subsystem", "test-action");
   }

   public String getCommand() {
      return "Subsystem: ["+this.subsystem+"]; Action: ["+this.action+"]";
   }

   public void execute() {

   }
}
