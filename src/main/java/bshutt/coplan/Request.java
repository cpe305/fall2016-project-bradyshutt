package bshutt.coplan;

import bshutt.coplan.models.User;
import org.bson.Document;

import static org.bson.Document.parse;

public class Request {

    public String route;
    public Document data;
    private User user;

    public Request() { }

    public Request(String route, Document data) {
        this.route = route;
        this.data = data;
    }

    public Request(String req) {
        Document reqDoc = parse(req);
        this.route = reqDoc.getString("route");
        this.data = (Document) reqDoc.get("data");
    }

    public String get(String key) {
        return this.data.getString(key);
    }

    public <T> T get(String key, Class<T> type) {
        return this.data.get(key, type);
    }

    public User getUser() throws Exception {
        String username = this.get("username");
        if (username == null) {
            return null;
        } else {
            this.user = new User().load(username);
            return this.user;
        }
    }

    public boolean contains(String key) {
        return (this.data.containsKey(key));
    }

    public Document pack() {
        Document p = new Document();
        p.append("route", this.route);
        p.append("data", data);
        return p;
    }

    public String toString() {
        String str = "Request for '" + this.route
                + "\n\tData: " + this.data.toJson();
        return str;
    }

}