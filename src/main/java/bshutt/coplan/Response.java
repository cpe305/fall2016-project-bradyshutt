package bshutt.coplan;

import org.bson.Document;

import java.io.PrintWriter;
import java.io.StringWriter;
import bshutt.coplan.Request;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;

public class Response {

    private Document doc;
    public boolean isDone = false;
    private Request request;

    public Response(Request req) {
        this.request = req;
        this.doc = new Document();
        this.doc.append("res", 1);
        this.doc.append("route", this.request.route);
    }

    public Response append(String key, Object obj) {
        this.doc.append(key, obj);
        return this;
    }

    public static void log(String message) {
        Response r = new Response(null);
        r.append("msg", message);
        r.end();
    }

    public static void log(Document doc) {
        Response r = new Response(null);
        r.append("msg", doc);
        r.end();
    }

    public Response setResponse(Document doc) {
        this.doc = new Document(null);
        this.doc.append("res", 1);
        this.doc.append("route", this.request.route);
        doc.forEach((key, val) -> this.doc.append(key, val));
        this.end();
        return this;
    }

    public Response setResponse(String msg) {
        this.doc = new Document();
        this.doc.append("res", 1);
        this.doc.append("message", msg);
        this.end();
        return this;
    }

    public Response err(Exception exc) {
        this.doc = new Document();
        this.doc.append("res", 0);
        this.doc.append("route", this.request.route);
        addException(this.doc, exc);
        this.end();
        return this;
    }

    public Response err(String errMsg) {
        this.doc = new Document();
        this.doc.append("res", 0);
        this.doc.append("route", this.request.route);
        this.doc.append("message", errMsg);
        this.doc.append("request", this.request.pack());
        this.end();
        return this;
    }

    public Response err(String errMsg, Exception exc) {
        this.doc = new Document();

        this.doc.append("res", 0);
        this.doc.append("message", errMsg);
        addException(this.doc, exc);
        this.doc.append("route", this.request.route);
        this.doc.append("request", this.request.pack());
        this.end();
        return this;
    }

//    public Response err(Exception exc) {
//        this.doc = new Document();
//        this.doc.append("res", 0);
//        this.doc.append("exception", exc.toString());
//        this.doc.append("req", this.request.pack());
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw);
//        exc.printStackTrace(pw);
//        String stackTrace = sw.toString();
//        this.doc.append("stackTrace", stackTrace);
//        this.doc.append("request", this.request.pack());
//        this.end();
//        return this;
//    }

    public Response end() {
        if (this.isDone) {
            throw new Error("This response has already finished. You can't end it again.");
        } else {
            System.out.println(doc.toJson());
            this.isDone = true;
            return this;
        }
    }

//    public Response end(String msg) {
//        this.doc.append("message", msg);
//        if (this.isDone) {
//            throw new Error("This response has already finished. You can't end it again.");
//        } else {
//            System.out.println(doc.toJson());
//            this.isDone = true;
//            return this;
//        }
//    }

    public Response end(Boolean status) {
        this.doc.append("res", status? 1 : 0);
        if (this.isDone) {
            throw new Error("This response has already finished. You can't end it again.");
        } else {
            System.out.println(this.doc.toJson());
            this.isDone = true;
            return this;
        }
    }

    public Response end(Boolean status, String msg) {
        this.doc.append("res", status? 1 : 0);
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

    private void addException(Document doc, Exception exc) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        Document exceptionDoc = new Document();
        exceptionDoc.append("toString", exc.toString());
        exceptionDoc.append("message", exc.getMessage());
        exc.printStackTrace(pw);
        String stackTrace = sw.getBuffer().toString();
        exceptionDoc.append("stackTrace", stackTrace);
        doc.append("exception", stackTrace);
    }



}
