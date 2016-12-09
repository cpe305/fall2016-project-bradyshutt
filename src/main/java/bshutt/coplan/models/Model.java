package bshutt.coplan.models;

import bshutt.coplan.Database;
import bshutt.coplan.exceptions.DatabaseException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

public abstract class Model<Type> {

    protected Database db = Database.getInstance();
    private MongoCollection<Document> col;
    private String collectionName;
    private String filterKey;
    private String filterValue;
    private Bson filter;

    //public Document attributes;

    public Model(String collectionName, String filterKey) {
        this.collectionName = collectionName;
        this.col = this.db.db.getCollection(collectionName);
        this.filterKey = filterKey;
        //this.filterKey = this.db.filter(uniqueAttributeKey, uniqueAttributeValue);
    }

    public Document loadModel(String filterValue) {
        this.filterValue = filterValue;
        this.filter = this.db.filter(this.filterKey, this.filterValue);
        Document doc = this.col.find(this.filter).first();
        return doc;
    }

//    public String getData(String key) {
//        return this.attributes.getString(key);
//    }

//    public <T> T getData(String key, Class<T> type) {
//        return this.attributes.getData(key, type);
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

    public void save() throws DatabaseException {
        Document doc = this.toDBDoc();
        if (this.validate(doc)) {
            if (this.existsInDB()) {
                try {
                    this.col.findOneAndReplace(this.filter, doc);
                } catch (Exception exc) {
                    throw new DatabaseException("Error with 'findOneAndReplace': " + exc.getMessage(), exc);
                }
            } else {
                this.col.insertOne(doc);
            }
        }
    }

    public void setFilter() {
        this.filterValue = this.getFilterValue();
        this.filter = this.db.filter(this.filterKey, this.filterValue);
    }

    public boolean existsInDB() {
        if (this.filterValue ==  null || this.filter == null) {
            this.setFilter();
        }
        //System.out.println("filter: "+ this.filter.toBsonDocument(Document.class, MongoClient.getDefaultCodecRegistry()).toString());
        return this.col.find(this.filter).first() != null;
    }

    //public abstract Type load(String filterVal) throws Exception;
    public abstract boolean validate(Document doc);
    public abstract String getFilterValue();
    public abstract Document toDBDoc();
    public abstract Type fromDoc(Document doc);

}
