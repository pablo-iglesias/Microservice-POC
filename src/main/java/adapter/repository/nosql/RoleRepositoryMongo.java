package adapter.repository.nosql;

import domain.constraints.repository.IRoleRepository;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import core.Server;
import core.database.DatabaseMongoDB;
import domain.entity.Role;
import domain.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Role repo that expects the database to be MongoDB
 * 
 * @author Peibol
 */
public class RoleRepositoryMongo implements IRoleRepository {

    private DatabaseMongoDB db;

    public RoleRepositoryMongo() throws Exception {
        db = (DatabaseMongoDB) Server.getDatabase();
    }

    /**
     * Get roles by ids
     */
    public Role getRole(Integer rid) throws Exception {

        if(db.retrieveDocument("roles", Filters.eq("id", rid))) {
            return new Role(
                db.getInt("id"),
                db.getString("name"),
                db.getString("page")
            );
        }

        return null;
    }

    /**
     * Get roles by user id
     */
    public Role[] getRolesByUser(User user) throws Exception {

        if(db.retrieveDocument("users", Filters.eq("id", user.getId())))
        {
            Integer[] roleIds = db.getArray("roles")
                .stream().toArray(size -> new Integer[size]);

            if(db.retrieveDocuments("roles", Filters.in("id", roleIds))) {
                List<Role> roles = new ArrayList<>();
                while(db.next()) {
                    roles.add(
                        new Role(
                            db.getInt("id"),
                            db.getString("name"),
                            db.getString("page")
                        )
                    );
                }
            }
        }

        return new Role[]{};
    }

    /**
     * Add roles to the user upon user creation, not needed in MongoDB
     */
    public boolean setupUserRoles(User user, Role[] roles) {
        return true;
    }

    /**
     * Add roles to the user
     */
    public boolean setRolesToUser(User user, Role[] roles) {

        return db.updateDocument("users",
            Filters.eq("id", user.getId()),
            Updates.set("roles", roles)
        );
    }

    /**
     * Remove all roles from user
     */
    public boolean removeAllRolesFromUser(Integer uid) {

        return db.updateDocument("users",
            Filters.eq("id", uid),
            Updates.set("roles", new ArrayList<>())
        );
    }
}
