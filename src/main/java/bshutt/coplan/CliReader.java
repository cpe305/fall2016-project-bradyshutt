package bshutt.coplan;

import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class CliReader extends Reader {

    private String malformed = "Malformed input command."
            + "\nFormat:\n\t\"<subsystem> <action> [<data>...]\"";

    @Override
    public Request deserialize(String cmdString) throws Exception {
        String[] cmdArray = cmdString.split(" ");
        Document data;

        if (cmdArray.length == 1) {
            data = null;
        } else if (cmdArray.length > 2) {
            String dataStr = "";
            for (int idx = 1; idx < cmdArray.length; idx++) {
                dataStr += cmdArray[idx];
            }
            data = Document.parse(dataStr);
        } else {
            System.out.println(cmdString);
            throw new Exception(malformed);
        }

        Request req = new Request(
                cmdArray[0],
                data
        );

        return req;
    }
}
