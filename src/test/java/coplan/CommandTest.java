package coplan;

import junit.framework.TestCase;

public class CommandTest extends TestCase {

   public void testCommand() {
      Command cmd = new Command("User","read");
      assertEquals("Subsystem: [User]; Action: [read]", cmd.getCommand());
   }

   public void testFromJSON() {
      Command cmd = Command.fromJSON("<not-implemented-yet>");
      assertEquals("Subsystem: [test-subsystem]; Action: [test-action]", cmd.getCommand());
   }

}