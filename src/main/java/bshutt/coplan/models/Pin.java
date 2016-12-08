package bshutt.coplan.models;

import org.bson.Document;

import java.util.Date;

public class Pin {
    private String name;
    private String content;
    private Date createDate;
    private Date deadline;

    public Pin(String name) {
        this.name = name;
        this.createDate = new Date();
        this.deadline = null;
    }

    public Pin(String name, String content) {
        this.name = name;
        this.content = content;
        this.createDate = new Date();
        this.deadline = null;
    }

    public Pin(String name, Date deadline) {
        this.name = name;
        this.deadline = deadline;
        this.createDate = new Date();
        this.deadline = null;
    }

    public Pin(String name, String content, Date deadline) {
        this.name = name;
        this.deadline = deadline;
        this.content = content;
        this.createDate = new Date();
        this.deadline = null;
    }

    public static Pin fromDoc(Document pinDoc) {
        String name = pinDoc.getString("name");
        String type = pinDoc.getString("type");
        Pin pin = new Pin(type, name);
        pin.content = (pinDoc.containsKey("content") ? pinDoc.getString("content") : null);
        pin.deadline = (pinDoc.containsKey("deadline") ? pinDoc.getDate("deadline") : null);
        return pin;
    }

    public Document toDoc() {
        Document doc = new Document();
        doc.append("pinName", this.name);
        doc.append("createDate", this.createDate);
        doc.append("deadline", this.deadline);
        doc.append("content", this.content);
        return doc;
    }
}
