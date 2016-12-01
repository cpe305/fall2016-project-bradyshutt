package bshutt.coplan.models;

import org.bson.Document;

import java.util.Date;

public class Pin {
    private String name;
    private String type;
    private String details;
    private Date createDate;
    private Date deadline;

    public Pin(String pinType, String name) {
        this.type = pinType;
        this.name = name;
        this.createDate = new Date();
        this.deadline = null;
    }

    public Pin(String pinType, String name, String details) {
        this.type = pinType;
        this.name = name;
        this.details = details;
        this.createDate = new Date();
        this.deadline = null;
    }

    public Pin(String pinType, String name, Date deadline) {
        this.type = pinType;
        this.name = name;
        this.deadline = deadline;
        this.createDate = new Date();
        this.deadline = null;
    }

    public Pin(String pinType, String name, String details, Date deadline) {
        this.type = pinType;
        this.name = name;
        this.deadline = deadline;
        this.details = details;
        this.createDate = new Date();
        this.deadline = null;
    }

    public static Pin fromDoc(Document pinDoc) {
        String name = pinDoc.getString("name");
        String type = pinDoc.getString("type");
        Pin pin = new Pin(type, name);
        pin.details = (pinDoc.containsKey("details") ? pinDoc.getString("details") : null);
        pin.deadline = (pinDoc.containsKey("deadline") ? pinDoc.getDate("deadline") : null);
        return pin;
    }

    public Document toDoc() {
        Document doc = new Document();
        doc.append("pinName", this.name);
        doc.append("type", this.type);
        doc.append("createDate", this.createDate);
        doc.append("deadline", this.deadline);
        doc.append("details", this.details);
        return doc;
    }
}
