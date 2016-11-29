package bshutt.coplan.utils;

import bshutt.coplan.Request;
import org.bson.Document;

public class RequestBuilder {
    public String route;
    public Document data;

    public RequestBuilder() {
        this.data = new Document();
    }

    public RequestBuilder setRoute(String route) {
        this.route = route;
        return this;
    }

    public RequestBuilder addData(String key, Object value) {
        this.data.append(key, value);
        return this;
    }

    public Request done() {
        return new Request(this.route, this.data);
    }

    public Request end() {
        return this.done();
    }

}
