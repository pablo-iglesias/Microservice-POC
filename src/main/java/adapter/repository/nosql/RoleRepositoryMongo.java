package adapter.repository.nosql;

import domain.constraints.repository.IRoleRepository;
import com.mongodb.client.model.Filters;
import core.Server;
import core.database.DatabaseMongoDB;
import domain.entity.Role;
import domain.entity.User;

import javax.enterprise.inject.Alternative;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Role repo that expects the database to be MongoDB
 * 
 * @author Peibol
 */
@Alternative
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

        List<Role> roles = new ArrayList<>();

        if(db.retrieveDocument("users", Filters.eq("id", user.getId())))
        {
            Integer[] roleIds = db.getArray("roles").stream().toArray(size -> new Integer[size]);

            if(db.retrieveDocuments("roles", Filters.in("id", roleIds))) {
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

        return Arrays.copyOf(roles.toArray(), roles.size(), Role[].class);
    }
}
