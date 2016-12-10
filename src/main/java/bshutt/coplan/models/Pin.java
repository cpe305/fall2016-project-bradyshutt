package bshutt.coplan.models;

import bshutt.coplan.exceptions.PinValidationException;
import org.bson.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Pin implements Serializable {
    public String name;
    public String content;
    public Date createDate;
    public Date deadline;



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
        String name = pinDoc.getString("pinName");
        //String type = pinDoc.getString("type");
        Pin pin = new Pin(name);
        pin.content = (pinDoc.containsKey("content") ? pinDoc.getString("content") : null);
        pin.deadline = (pinDoc.containsKey("deadline") ? pinDoc.getDate("deadline") : null);
        return pin;
    }

    public Document tzoDoc() {
        Document doc = new Document();
        doc.append("pinName", this.name);
        doc.append("createDate", this.createDate.toString());
        if (this.deadline != null)
            doc.append("deadline", this.deadline.toString());
        else
            doc.append("deadline", null);

        doc.append("content", this.content);
        return doc;
    }

    public boolean validate() throws PinValidationException {
        return true;
    }

    public String toString() {
        return "Pin: { " + this.name + ", " + this.content + " }";
    }

    public Document serialize() {
        Document doc = new Document();
        doc.append("pinName", this.name);
        doc.append("createDate", this.createDate.toString());
        if (this.deadline != null)
            doc.append("deadline", this.deadline.toString());
        else
            doc.append("deadline", null);
        doc.append("content", this.content);
        return doc;
    }

    public static ArrayList<Pin> toPinList(ArrayList<Document> docs) {
        ArrayList<Pin> pins = new ArrayList<>();
        if (docs != null) {
            docs.forEach((doc) -> {
                if (doc != null)
                    pins.add(Pin.fromDoc(doc));
            });
            return pins;
        } else {
           return null;
        }
    }

    public static ArrayList<Document> toDocList(ArrayList<Pin> pins) {
        ArrayList<Document> docs = new ArrayList<>();
        if (pins != null) {
            pins.forEach((pin) -> {
                docs.add(pin.serialize());
            });
        }
        return docs;
    }
}
