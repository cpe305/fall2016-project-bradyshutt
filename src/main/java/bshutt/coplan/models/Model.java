package bshutt.coplan.models;

import bshutt.coplan.DBException;
import bshutt.coplan.Database;
import com.mongodb.MongoWriteException;
import org.bson.Document;

public abstract class Model<Type> {

    protected Database db = Database.getInstance();
    protected boolean existsInDB = false;

    public Document attributes;

    public String get(String key) {
        return this.attributes.getString(key);
    }

    public <T> T get(String key, Class<T> type) {
        return this.attributes.get(key, type);
    }

    public void set(String key, Object value) {
        if (this.attributes.containsKey(key))
            this.attributes.replace(key, value);
        else
            this.attributes.append(key, value);
    }

    public Document getAttributes() {
        return this.attributes;
    }

    public abstract Type build(Document doc);
    public abstract Type load(String filterVal) throws DBException;
    public abstract void save() throws DBException;
    public abstract boolean validate() throws DBException;

}
