package bshutt.coplan;

import bshutt.coplan.models.Course;
import bshutt.coplan.models.User;
import org.bson.Document;

import static org.bson.Document.parse;

public class Request {

    private String route = null;
    private Document data = null;
    private User user = null;
    private Course course = null;

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

    public String getRoute() {
        return this.route;
    }

    public Document getData() {
        return data;
    }

    public <T> T getData(String key, Class<T> type) {
        return this.data.get(key, type);
    }

    public boolean contains(String key) {
        return (this.data.containsKey(key));
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public Course getCourse() {
        return this.course;
    }

    public Document pack() {
        Document p = new Document();
        p.append("route", this.route);
        p.append("data", data);
        return p;
    }

    public String toString() {
        String str = "<REQUEST ["+this.route+"]> \n"+
                "\tData: {" + this.data.toJson()+"}";
        return str;
    }

}
