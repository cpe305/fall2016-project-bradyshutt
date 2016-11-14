package bshutt.coplan.models;

import org.bson.Document;

import java.util.ArrayList;

public class Board {

    private String name;
    private ArrayList<Pin> pins;
    private boolean changed = false;

    public Board(String name) {
        this.name = name;
        this.pins = new ArrayList<Pin>();
    }

    public Board addPin(Pin pin) {
        this.pins.add(pin);
        this.changed = true;
        return this;
    }

    public Board removePin(Pin pin) {
        this.pins.remove(pin);
        this.changed = true;
        return this;
    }

    public static Board fromDoc(Document boardDoc) {
        Board board = new Board(boardDoc.getString("name"));
        boardDoc.get("pins", ArrayList.class).forEach((pinDoc) -> {
            Pin pin = Pin.fromDoc((Document) pinDoc);
            board.addPin(pin);
        });
        return board;
    }

    public Document toDoc() {
        Document doc = new Document();
        doc.append("boardName", this.name);
        doc.append("pins", this.pins);
        return doc;
    }

}
