import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class JSONFormatter {
   private HashMap<String, String> formatter;

   public JSONFormatter() {
      this.formatter = new HashMap<String,String>();
   }

   public void set(String key, String value) {
      formatter.put(key, value);
   }

   public String format() {
      StringBuilder returnStr = new StringBuilder();

      Set jsonSet = this.formatter.entrySet();
      Iterator iterate = jsonSet.iterator();

      returnStr.append("{");
      while (iterate.hasNext()) {
         Map.Entry pair = (Map.Entry)iterate.next();
         returnStr.append(" \"" + pair.getKey() + "\":\"" + pair.getValue() + "\",");
         iterate.remove();
      }
      returnStr.append("}");

      return returnStr.toString();
   }

}
