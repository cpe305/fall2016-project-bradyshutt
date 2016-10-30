package bshutt.coplan;


import org.bson.Document;

public class JsonParser {

  private Document doc;

  public JsonParser(String jsonString) {
    String[] keyvalue = jsonString.split("\\{")[1].split("\\}")[0].split(",");
    this.doc = new Document();

    for (String kv : keyvalue) {
      String[] split = kv.split("\"");
      String key = split[1];
      String val = split[3];
      doc.append(key, val);
    }

  }

  public String get(String key) {
    return (String) this.doc.get(key);
  }

}
