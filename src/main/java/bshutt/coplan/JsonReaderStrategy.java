package bshutt.coplan;

import org.bson.Document;

public class JsonReaderStrategy implements ReaderStrategy {

    @Override
    public Request interpret(String cmdString) {
        Document inputDoc = Document.parse(cmdString);
        String reqRoute = inputDoc.getString("route");
        Document reqData = inputDoc.get("data", Document.class);
        Request req = new Request(reqRoute, reqData);
        return req;
    }
}
