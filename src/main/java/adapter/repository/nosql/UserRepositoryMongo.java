package adapter.repository.nosql;

import domain.constraints.repository.IUserRepository;
import domain.entity.User;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;

import core.Server;
import core.database.DatabaseMongoDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User repo that expects the database to be MongoDB
 * 
 * @author Peibol
 */
public class UserRepositoryMongo implements IUserRepository {

    private DatabaseMongoDB db;

    public UserRepositoryMongo() throws Exception {
        db = (DatabaseMongoDB) Server.getDatabase();
    }

    /**
     * Get all users
     *
     * @return
     */
    public User[] getAllUsers() {

        if (db.retrieveCollection("users")) {
            List<User> users = new ArrayList<>();
            while (db.next()) {
                users.add(new User(
                    db.getInt("id"),
                    db.getString("username"),
                    db.getArray("roles").stream().toArray(size -> new Integer[size])
                ));
            }
            return Arrays.copyOf(users.toArray(), users.size(), User[].class);
        } else {
            return null;
        }
    }

    /**
     * Get user
     */
    public User getUser(Integer uid) {

        if(db.retrieveDocument("users", Filters.eq("id", uid))){
            return new User(
                db.getInt("id"),
                db.getString("username"),
                db.getArray("roles").stream().toArray(size -> new Integer[size])
            );
        }
        else{
            return null;
        }
    }

    /**
     * Get user
     */
    public User getUser(String username) {

        if(db.retrieveDocument("users", Filters.eq("username", username))){
            return new User(
                db.getInt("id"),
                db.getString("username"),
                db.getArray("roles").stream().toArray(size -> new Integer[size])
            );
        }
        else{
            return null;
        }
    }

    /**
     * Get user
     */
    public User getUser(String username, String password) {

        if(db.retrieveDocument("users",
            Filters.and(
                Filters.eq("username", username),
                Filters.eq("password", password)
            )
        )){
            return new User(
                db.getInt("id"),
                db.getString("username"),
                db.getArray("roles").stream().toArray(size -> new Integer[size])
            );
        }
        else{
            return null;
        }
    }

    /**
     * Inserts new user
     */
    public Integer insertUser(User user) {

        Document document = new Document()
        .append("username", user.getUsername())
        .append("password", user.getPassword())
        .append("roles", Arrays.asList(user.getRoleIds()));

        return db.insertDocument("users", document);
    }

    /**
     * Updates existing user
     */
    public boolean updateUser(User user) {

        return db.updateDocument("users",
            Filters.eq("id", user.getId()),
            Updates.combine(
                Updates.set("username", user.getUsername()),
                Updates.set("password", user.getPassword()),
                Updates.set("roles", user.getRoleIds())
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
