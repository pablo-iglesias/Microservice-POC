package adapter.repository.nosql;

import org.bson.Document;

import com.google.common.base.Strings;

import domain.contract.repository.IUserRepository;
import domain.entity.User;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import core.database.DatabaseMongoDB;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.enterprise.inject.Alternative;
/**
 * User repo that expects the database to be MongoDB
 * 
 * @author Peibol
 */
@Alternative
public class UserRepositoryMongo implements IUserRepository {

    @Inject
    private DatabaseMongoDB db;

    public UserRepositoryMongo(DatabaseMongoDB db) throws Exception {
        this.db = db;
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
     *
     * @param user
     * @return
     */
    public boolean findUser(User user) {

        List<Bson> filters = new ArrayList<>();

        if(user.getId() != null)
            filters.add(Filters.eq("id", user.getId()));

        if(!Strings.isNullOrEmpty(user.getUsername()))
            filters.add(Filters.eq("username", user.getUsername()));

        if(!Strings.isNullOrEmpty(user.getPassword()))
            filters.add(Filters.eq("password", user.getPassword()));

        if(filters.size() > 0 && db.retrieveDocument("users", Filters.and(filters))){
            user.setId(db.getInt("id"));
            user.setUsername(db.getString("username"));
            user.setRoles(db.getArray("roles").stream().toArray(size -> new Integer[size]));
            return true;
        }
        else{
            return false;
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
    public boolean deleteUser(User user) {

        return db.removeDocument("users", Filters.eq("id", user.getId()));
    }
}
