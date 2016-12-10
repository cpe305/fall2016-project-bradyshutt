package bshutt.coplan.models;

import bshutt.coplan.Database;
import bshutt.coplan.exceptions.DatabaseException;
import com.auth0.jwt.internal.org.bouncycastle.asn1.dvcs.Data;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.xml.bind.DataBindingException;

public abstract class Model<Type> {

    protected Database db = Database.getInstance();
    private MongoCollection<Document> col;
    private String filterKey;
    private String filterValue;
    private Bson filter;

    public Model(String collectionName, String filterKey) {
        this.col = this.db.db.getCollection(collectionName);
        this.filterKey = filterKey;
    }

    Document loadModel(String filterValue) {
        this.filterValue = filterValue;
        this.filter = this.db.filter(this.filterKey, this.filterValue);
        Document doc = this.col.find(this.filter).first();
        return doc;
    }

    public void save() throws DatabaseException {
        Document doc = this.serialize();
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
        } else {
            throw new DatabaseException("Model failed to be validated");
        }
    }

    private void setFilter() {
        this.filterValue = this.getFilterValue();
        this.filter = this.db.filter(this.filterKey, this.filterValue);
    }

    private boolean existsInDB() {
        if (this.filterValue ==  null || this.filter == null) {
            this.setFilter();
        }
        return this.col.find(this.filter).first() != null;
    }

    public abstract boolean validate(Document doc);
    public abstract String getFilterValue();
    public abstract Document serialize();
    public abstract Type deserialize(Document doc);
}

