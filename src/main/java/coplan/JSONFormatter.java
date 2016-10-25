package coplan;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;

import java.util.Set;
import java.util.Iterator;

public class JSONFormatter {
   private Map<String, Object> jsonMap;

   public JSONFormatter() {
      this.jsonMap = new HashMap<String,Object>();
   }

   public void set(String key, String value) {
      this.jsonMap.put(key, value);
   }

   public String format() {
      String json = new JSONObject(jsonMap).toString();
      return json;
//      StringBuilder jsonString = new StringBuilder();
//      jsonString.append("{");
//      for (Map.Entry<String, String> entry: map.entrySet()) {
//         jsonString.append("\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"");
//      }
//      returnStr.append("}");
//
//      return returnStr.toString();
   }

}
