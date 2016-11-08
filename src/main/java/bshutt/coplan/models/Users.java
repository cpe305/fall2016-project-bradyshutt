package bshutt.coplan.models;

import bshutt.coplan.DBException;
import bshutt.coplan.Database;
import bshutt.coplan.Handler;
import bshutt.coplan.Response;
import org.bson.Document;

import java.util.ArrayList;

public class Users {

    private Database db;
    private ArrayList<Handler> handlers;

    public Users(Database db) {
        this.db = db;
    }

    public Handler getUser = (req, res) -> {
        String username = req.get("username");
        Document userDoc = null;
        try {
            userDoc = this.db
                    .col("users")
                    .find(db.filter("username", username))
                    .first();
        } catch (DBException exc) {
            res.err(exc, req);
        }
        if (userDoc == null) {
            res.err("User '" + username + "' not found!", req);
        } else {
            res.setResponse(userDoc);
        }
    };

    public Handler createUser = (req, res) -> {
        Document userDoc = req.data;
        try {
            this.db
                    .col("users")
                    .insertOne(userDoc);
        } catch (DBException exc) {
            res.err(exc);
        }
        res.append("createdUser", "success");
        res.end();
    };

    public Handler usernameIsAvailable = (req, res) -> {
        String username = req.get("username");
        Document userDoc = null;
        try {
            userDoc = this.db
                    .col("users")
                    .find(db.filter("username", username))
                    .first();
        } catch (DBException exc) {
            res.err(exc, req);
        }
        res.append("usernameIsAvailable", (userDoc == null));
        res.end();
    };

    public void register() {

    }

}

//    public Document getUser(String username) {
//        Document userDoc = null;
//        try {
//            userDoc = this.db
//                    .col("users")
//                    .find(db.filter("username", username))
//                    .first();
//        } catch (DBException exc) {
//            res.err(exc, res);
//        }
//        return userDoc;
//    }

//    public void createUser(Document user) {
//        try {
//            this.db.col("users").insertOne(user);
//        } catch (DBException exc) {
//            res.
//            e.printStackTrace();
//        }
//    }


