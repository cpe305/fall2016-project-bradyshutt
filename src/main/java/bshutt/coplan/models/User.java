package bshutt.coplan.models;

import bshutt.coplan.Database;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

public class User {

    private Document doc;
    private Users users = new Users(Database.getInstance());

    public User(Document userDoc) {
        this.doc = userDoc;
    }

    public String toString() {
        return "User: \n" + this.doc.toJson();
    }

}
