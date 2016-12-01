package bshutt.coplan.models;

import bshutt.coplan.Database;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

public abstract class Model<Type> {

    protected Database db = Database.getInstance();
    private boolean existsInDB = false;
    private MongoCollection col;
    private String collectionName;
    private String filterKey;
    private String filterValue;
    private Bson filter;
    private String uniqueAttributeKey;
    protected boolean hasUpdates = true;

    //public Document attributes;

    public Model(String collectionName, String filterKey) {
        this.collectionName = collectionName;
        this.col = this.db.db.getCollection(collectionName);
        this.filterKey = filterKey;
        //this.filterKey = this.db.filter(uniqueAttributeKey, uniqueAttributeValue);
    }

    public Document loadModel(String uniqueValue) {
        this.filterValue = uniqueValue;
        this.filter = this.db.filter(this.filterKey, this.filterValue);
        this.existsInDB = true;
        return (Document) this.col.find(this.filter).first();
    }

//    public String get(String key) {
//        return this.attributes.getString(key);
//    }

//    public <T> T get(String key, Class<T> type) {
//        return this.attributes.get(key, type);
//    }

//    public void set(String key, Object value) {
//        if (this.attributes.containsKey(key))
//            this.attributes.replace(key, value);
//        else
//            this.attributes.append(key, value);
//    }
//
//    public Document getAttributes() {
//        return this.attributes;
//    }

    public void save() throws Exception {
        if (!this.hasUpdates)
            return;
        Document doc = this.toDoc();
        if (this.validate(doc)) {
            if (this.existsInDB) {
                this.col.findOneAndReplace(this.filter, doc);
            } else {
                this.col.insertOne(doc);
                this.existsInDB = true;
            }
        }
        this.hasUpdates = false;
    }

    //public abstract Type load(String filterVal) throws Exception;
    public abstract boolean validate(Document doc) throws Exception;
    public abstract Document toDoc() throws Exception;
    public abstract Type fromDoc(Document doc) throws Exception;

}
