package bshutt.coplan;

import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class CliReaderStrategy implements ReaderStrategy {


    @Override
    public Request interpret(String cmdString) {
        String[] cmdArray = cmdString.split(" ");
        Document data = null;

        if (cmdArray.length == 1) {
            data = null;
        } else if (cmdArray.length > 2) {
            String dataStr = "";
            for (int idx = 1; idx < cmdArray.length; idx++) {
                dataStr += cmdArray[idx];
            }
            data = Document.parse(dataStr);
        } else {
            System.err.println("Malformed input command: \n" + cmdString);
            return null;
        }

        Request req = new Request(
                cmdArray[0],
                data
        );

        return req;
    }
}
