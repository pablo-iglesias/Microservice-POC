package adapter.repository.nosql;

import adapter.repository.Repository;
import adapter.repository.RoleRepository;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import core.Server;
import core.database.DatabaseMongoDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Vector;

/**
 * Role repo that expects the database to be MongoDB
 * 
 * @author Peibol
 */
public class RoleRepositoryMongo extends Repository implements RoleRepository {

    private DatabaseMongoDB db;

    public RoleRepositoryMongo() throws Exception {
        db = (DatabaseMongoDB) Server.getDatabase();
    }

    /**
     * Takes a user id and returns the ids of the roles assigned to it
     */
    public Vector<Object[]> getRolesByIds(Integer rids[]) throws SQLException {

        if(db.retrieveDocuments("roles", Filters.in("id", Arrays.asList(rids)))){
            Vector<Object[]> roles = new Vector<>();
            while (db.next()) {
                roles.add(new Object[] {
                    db.getInt("id"),
                    db.getString("name"),
                    db.getString("page")
                });
            }
            return roles;
        }
        else {
            return null;
        }
    }

    /**
     * Takes a user id and returns the ids of the roles assigned to it
     */
    public Integer[] getRoleIdsByUserId(Integer uid) throws SQLException {

        if(db.retrieveDocument("users", Filters.eq("id", uid))){
            List<Object> roles = db.getArray("roles");
            Object[] roleObjects = roles.toArray();
            return Arrays.copyOf(roleObjects, roleObjects.length, Integer[].class);
        }

        return new Integer[]{};
    }

    /**
     * Insert rows in the table user_has_role
     */
    public boolean insertUserHasRoles(Integer uid, Integer[] rids) throws SQLException {

        return db.updateDocument("users",
            Filters.eq("id", uid),
            Updates.addEachToSet("roles", Arrays.asList(rids))
        );
    }

    /**
     * Delete rows from the table user_has_roles
     */
    public boolean deleteUserHasRoles(Integer uid, Integer[] rids) throws SQLException {

        return db.updateDocument("users",
                Filters.eq("id", uid),
                Updates.pullAll("roles", Arrays.asList(rids))
        );
    }

    /**
     * Delete rows from the table user_has_roles
     */
    public boolean deleteUserHasRolesByUserId(Integer uid) throws SQLException {

        return db.updateDocument("users",
            Filters.eq("id", uid),
            Updates.set("roles", new ArrayList<>())
        );
    }
}
