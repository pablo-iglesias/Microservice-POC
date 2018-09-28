package core.database;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import core.Helper;
import core.Server;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Iterator;

import javax.enterprise.inject.Alternative;

@Alternative
public class DatabaseMongoDB extends Database {

    protected static MongoClient conn = null;
    protected static MongoCredential credential = null;
    protected static MongoDatabase database = null;
    protected Iterator it = null;
    protected Document doc = null;

    public boolean connect(){
        if (conn != null) {
            return true;
        }

        try {
            String host = Server.getConfig(Server.MONGO_HOST);
            String port = Server.getConfig(Server.MONGO_PORT);
            String db = Server.getConfig(Server.MONGO_DB);
            String user = Server.getConfig(Server.MONGO_USER);
            String pass = Server.getConfig(Server.MONGO_PASS);

            conn = new MongoClient(host ,Integer.valueOf(port));
            credential = MongoCredential.createCredential(user, db, pass.toCharArray());
            database = conn.getDatabase(db);

            // Database is empty
            if(!database.listCollectionNames().iterator().hasNext()){
                dump(loadResourceAsString("mongo/mongo.dump"));
            }

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            conn = null;
            return false;
        }
    }

    public boolean dump(String dump) {
        if (!aware()) {
            return false;
        }

        try {
            // Get all the statements of the Mongo dump line by line
            List<String> statements = Helper.list(dump, "([^;]+;)");

            for (String line : statements) {
                Bson command = new Document("eval", line);
                database.runCommand(command);
            }

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean startTransaction(){
        return true;
    }

    public boolean rollback(){
        return true;
    }

    public boolean commit(){
        return true;
    }

    private boolean aware(){
        if (conn == null || credential == null || database == null) {
            return false;
        }

        return true;
    }

    public boolean retrieveCollection(String collectionName) {
        if (!aware()) {
            return false;
        }

        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            FindIterable<Document> iterDoc = collection.find();
            it = iterDoc.iterator();
            if(it.hasNext()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean retrieveDocuments(String collectionName, Bson filter) {
        if (!aware()) {
            return false;
        }

        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            FindIterable<Document> iterDoc = collection.find(filter);
            it = iterDoc.iterator();
            if(it.hasNext()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean retrieveDocument(String collectionName, Bson filter) {
        if(retrieveDocuments(collectionName, filter)){
            doc = (Document)it.next();
            it = null;
            return true;
        }

        return false;
    }

    public boolean next() {
        if(it == null){
            return false;
        }

        try {
            if(it.hasNext()) {
                doc = (Document)it.next();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String getString(String paramName) {
        if (doc != null) {
            return doc.getString(paramName);
        }
        return "";
    }

    public int getInt(String paramName) {
        if (doc != null) {
            return doc.getInteger(paramName);
        }
        return 0;
    }

    public List<Object> getArray(String paramName) {
        if (doc != null) {
            return (List) doc.get(paramName);
        }
        return null;
    }

    public Integer insertDocument(String collectionName, Document document) {
        if (!aware()) {
            return null;
        }

        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            Document last = collection.find().sort(new BasicDBObject("id",-1)).first();
            Integer id = last.getInteger("id") + 1;
            document.put("id", id);
            collection.insertOne(document);
            return id;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean updateDocument(String collectionName, Bson filter, Bson update) {
        if (!aware()) {
            return false;
        }

        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            UpdateResult result = collection.updateOne(filter, update);
            if(!result.wasAcknowledged()){
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean removeDocument(String collectionName, Bson filter) {
        if (!aware()) {
            return false;
        }

        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            DeleteResult result = collection.deleteOne(filter);
            if(!result.wasAcknowledged()){
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
