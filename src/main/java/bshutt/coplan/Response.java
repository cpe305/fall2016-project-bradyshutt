package bshutt.coplan;

import org.bson.Document;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Response {

    private Document doc;
    public boolean isDone = false;

    private static final String GOOD = "good";
    private static final String LOG = "log";
    private static final String ERR = "error";

    public Response() {
        this.doc = new Document();
        this.doc.append("type", Response.GOOD);
        this.doc.append("body", new Document());
    }

    public Response append(String key, Object obj) {
        this.doc.append(key, obj);
        return this;
    }

    public static void log(String message) {
        Response r = new Response();
        r.append("type", Response.LOG);
        r.append("msg", message);
        r.end();
    }

    public static void log(Document doc) {
        Response r = new Response();
        r.append("type", Response.LOG);
        r.append("msg", doc);
        r.end();
    }

    public Response setResponse(Document doc) {
        this.doc = new Document();
        this.doc.append("type", Response.GOOD);
        this.doc.append("body", doc);
        this.end();
        return this;
    }

    public Response setResponse(String msg) {
        this.doc = new Document();
        this.doc.append("type", Response.GOOD);
        this.doc.append("message", msg);
        this.end();
        return this;
    }

    public Response err(Exception exc) {
        this.doc = new Document();
        this.doc.append("type", Response.ERR);
        this.doc.append("errMessage", exc.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exc.printStackTrace(pw);
        String stackTrace = sw.toString();
        this.doc.append("stackTrace", stackTrace);
        this.end();
        return this;
    }

    public Response err(String errMsg, Request req) {
        this.doc = new Document();
        this.doc.append("type", Response.ERR);
        this.doc.append("errMessage", errMsg);
        this.doc.append("req", req.pack());
        this.end();
        return this;
    }

    public Response err(Exception exc, Request req) {
        this.doc = new Document();
        this.doc.append("type", Response.ERR);
        this.doc.append("msg", exc.getMessage());
        this.doc.append("req", req.pack());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exc.printStackTrace(pw);
        String stackTrace = sw.toString();
        this.doc.append("stackTrace", stackTrace);
        this.end();
        return this;
    }

    public Response end() {
        if (this.isDone) {
            throw new Error("This response has already finished. You can't end it again.");
        } else {
            System.out.println(doc.toJson());
            this.isDone = true;
            return this;
        }
    }

    public Response end(String msg) {
        this.doc.append("message", msg);
        if (this.isDone) {
            throw new Error("This response has already finished. You can't end it again.");
        } else {
            System.out.println(doc.toJson());
            this.isDone = true;
            return this;
        }
    }

    public Response end(Boolean status) {
        this.doc.append("status", status? "good" : "bad");
        if (this.isDone) {
            throw new Error("This response has already finished. You can't end it again.");
        } else {
            System.out.println(doc.toJson());
            this.isDone = true;
            return this;
        }
    }

    public Response end(Boolean status, String msg) {
        this.doc.append("status", status? "good" : "bad");
        this.doc.append("message", msg);
        if (this.isDone) {
            throw new Error("This response has already finished. You can't end it again.");
        } else {
            System.out.println(doc.toJson());
            this.isDone = true;
            return this;
        }
    }
    public Document getDoc() {
        return this.doc;
    }

}
