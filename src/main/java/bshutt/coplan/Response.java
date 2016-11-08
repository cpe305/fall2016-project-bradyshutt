package bshutt.coplan;

import org.bson.Document;

public class Response {

    private String response;
    private Document doc;
    public boolean isDone = false;

    private static class TYPES {
        public static String GOOD = "good";
        public static String MSG = "message";
        public static String ERR = "error";
    }

    public Response() {
        this.doc = new Document();
        this.doc.append("status", TYPES.GOOD);
        this.doc.append("body", new Document());
    }

    public Response append(String key, Object obj) {
        this.doc.get("body", Document.class).append(key, obj);
        return this;
    }

    public static void msg(String message) {
        Response r = new Response();
        r.append("type", TYPES.MSG);
        r.append("msg", message);
        r.end();
    }

    public Response setResponse(Document doc) {
        this.doc = new Document();
        this.doc.append("type", TYPES.GOOD);
        this.doc.append("body", doc);
        this.end();
        return this;
    }

    public Response setResponse(String msg) {
        this.doc = new Document();
        this.doc.append("type", TYPES.MSG);
        this.doc.append("message", msg);
        this.end();
        return this;
    }

    public Response err(Exception exc) {
        this.doc = new Document();
        this.doc.append("type", TYPES.ERR);
        this.doc.append("errMessage", exc.getMessage());
        this.doc.append("stackTrace", exc.getStackTrace().toString());
        this.end();
        return this;
    }

    public Response err(String errMsg, Request req) {
        this.doc = new Document();
        this.doc.append("type", TYPES.ERR);
        this.doc.append("errMessage", errMsg);
        this.doc.append("req", req.pack());
        this.end();
        return this;
    }

    public Response err(Exception exc, Request req) {
        this.doc = new Document();
        this.doc.append("type", TYPES.ERR);
        this.doc.append("msg", exc.getMessage());
        this.doc.append("req", req.pack());
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


}
