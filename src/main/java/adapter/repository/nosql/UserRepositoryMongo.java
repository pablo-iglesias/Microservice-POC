package adapter.repository.nosql;

import adapter.repository.Repository;
import adapter.repository.UserRepository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;

import core.Server;
import core.database.DatabaseMongoDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * User repo that expects the database to be MongoDB
 * 
 * @author Peibol
 */
public class UserRepositoryMongo extends Repository implements UserRepository {

    private DatabaseMongoDB db;

    public UserRepositoryMongo() throws Exception {
        db = (DatabaseMongoDB) Server.getDatabase();
    }

    /**
     * Get all users
     *
     * @return
     */
    public Vector<Object[]> getUsers() {

        if (db.retrieveCollection("users")) {
            Vector<Object[]> users = new Vector<>();
            while (db.next()) {
                users.add(new Object[] { db.getInt("id"), db.getString("username") });
            }
            return users;
        } else {
            return null;
        }
    }

    /**
     * Takes user id and returns the user name
     * 
     * @return
     */
    public String selectUsernameByUserId(Integer uid) {

        if(db.retrieveDocument("users", Filters.eq("id", uid))){
            return db.getString("username");
        }
        else{
            return null;
        }
    }

    /**
     * Return true if a user with this id already exists
     *
     */
    public boolean selectUserExists(Integer uid) {
        if(db.retrieveDocument("users", Filters.eq("id", uid))){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Returns true if a user with this username already exists
     * 
     */
    public boolean selectUserExistsByUseraname(String username) {

        if(db.retrieveDocument("users", Filters.eq("username", username))){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Takes user name and hashed user password and returns the corresponding
     * user id, used for authentication
     * 
     * @return
     */
    public Integer selectUserIdByUseranameAndPassword(String username, String password) {

        if(db.retrieveDocument("users",
                Filters.and(
                    Filters.eq("username", username),
                    Filters.eq("password", password)
                )
        )){
            return new Integer(db.getInt("id"));
        }
        else{
            return null;
        }
    }

    /**
     * Takes user name and returns the corresponding user id, used for checking
     * if the username is already taken
     */
    public Integer selectUserIdByUseraname(String username) {

        if(db.retrieveDocument("users", Filters.eq("username", username))){
            return new Integer(db.getInt("id"));
        }
        else{
            return null;
        }
    }

    /**
     * Return true if the user has the ADMIN role
     * 
     */
    public boolean selectUserIsAdminRole(Integer uid) {

        Integer adminRoleId = null;

        if(db.retrieveDocument("roles", Filters.eq("name", "ADMIN"))){
            adminRoleId = db.getInt("id");
        }
        else{
            return false;
        }

        if(db.retrieveDocument("users",
                Filters.and(
                    Filters.eq("id", uid),
                    Filters.eq("roles", adminRoleId)
                )
        )){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Inserts new user
     */
    public Integer insertUser(String username, String password) {

        List<BasicDBObject> roles = new ArrayList<>();

        Document document = new Document()
        .append("username", username)
        .append("password", password)
        .append("roles", roles);

        return db.insertDocument("users", document);
    }

    /**
     * Updates existing user
     */
    public boolean updateUser(Integer uid, String username, String password) {

        return db.updateDocument("users",
            Filters.eq("id", uid),
            Updates.combine(
                Updates.set("username", username),
                Updates.set("password", password)
            )
        );
    }

    /**
     * Removes existing user
     */
    public boolean deleteUser(Integer uid) {

        return db.removeDocument("users", Filters.eq("id", uid));
    }
}
