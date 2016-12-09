package bshutt.coplan;

import bshutt.coplan.exceptions.JwtException;
import bshutt.coplan.exceptions.UserDoesNotExistException;
import bshutt.coplan.models.Course;
import bshutt.coplan.models.User;
import org.bson.Document;

import static org.bson.Document.parse;

public class Request {

    public String route = null;
    public Document data = null;
    public User user = null;
    public Course course = null;

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

    public String getData(String key) {
        return this.data.getString(key);
    }

    public <T> T getData(String key, Class<T> type) {
        return this.data.get(key, type);
    }

//    public User getUser() throws UserDoesNotExistException, JwtException {
//        String username = this.getData("username");
//        String jwt = this.getData("jwt");
//        if (username != null) {
//            this.user = User.load(username);
//        } else if (jwt != null) {
//            this.user = this.getUserFromJwt(jwt);
//        } else {
//            this.user = null;
//        }
//        return this.user;
//    }

//    public User getUserFromJwt(String jwt) throws UserDoesNotExistException, JwtException {
//        if (jwt == null) {
//            return null;
//        } else {
//            this.user = User.loadFromJwt(jwt);
//            return this.user;
//        }
//
//    }

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
