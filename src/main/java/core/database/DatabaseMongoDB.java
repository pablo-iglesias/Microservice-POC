package core.database;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
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

import javax.enterprise.inject.Alternative;

@Alternative
public class DatabaseMongoDB extends Database {

    protected static MongoClient conn = null;
    protected static MongoCredential credential = null;
    protected static MongoDatabase database = null;
    protected MongoCursor<Document> it = null;
    protected Document document = null;

    public boolean connect(){
        if (conn != null) {
            return true;
        }

        try {
            String host = Server.getConfig(Server.Config.MONGO_HOST);
            String port = Server.getConfig(Server.Config.MONGO_PORT);
            String db   = Server.getConfig(Server.Config.MONGO_DB);
            String user = Server.getConfig(Server.Config.MONGO_USER);
            String pass = Server.getConfig(Server.Config.MONGO_PASS);

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
        if (unaware()) {
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

    private boolean unaware(){
        return conn == null || credential == null || database == null;
    }

    public boolean retrieveCollection(String collectionName) {
        if (unaware()) {
            return false;
        }

        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            FindIterable<Document> iterDoc = collection.find();
            it = iterDoc.iterator();
            return it.hasNext();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean retrieveDocuments(String collectionName, Bson filter) {
        if (unaware()) {
            return false;
        }

        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            FindIterable<Document> iterDoc = collection.find(filter);
            it = iterDoc.iterator();
            return it.hasNext();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean retrieveDocument(String collectionName, Bson filter) {
        if(retrieveDocuments(collectionName, filter)){
            document = it.next();
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
                document = it.next();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String getString(String paramName) {
        if (document != null) {
            return document.getString(paramName);
        }
        return "";
    }

    public int getInt(String paramName) {
        if (document != null) {
            return document.getInteger(paramName);
        }
        return 0;
    }

    public List<Object> getArray(String paramName) {
        if (document != null) {
            return document.get(paramName, List.class);
        }
        return null;
    }

    public Integer insertDocument(String collectionName, Document document) {
        if (unaware()) {
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
        if (unaware()) {
            return false;
        }

        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            UpdateResult result = collection.updateOne(filter, update);
            return result.wasAcknowledged();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean removeDocument(String collectionName, Bson filter) {
        if (unaware()) {
            return false;
        }

        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            DeleteResult result = collection.deleteOne(filter);
            return result.wasAcknowledged();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
