package coplan;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class JsonFormatter {
  private Map<String, Object> jsonMap;

  public JsonFormatter() {
    this.jsonMap = new HashMap<String, Object>();
  }

  public void set(String key, String value) {
    this.jsonMap.put(key, value);
  }

  /**
   * Format into a json object.
   *
   * @return the string.
   */
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
