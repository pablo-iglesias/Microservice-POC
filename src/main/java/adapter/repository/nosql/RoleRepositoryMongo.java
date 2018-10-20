package adapter.repository.nosql;

import domain.contract.repository.IRoleRepository;
import com.mongodb.client.model.Filters;
import core.database.DatabaseMongoDB;
import domain.entity.Role;
import domain.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

/**
 * Role repo that expects the database to be MongoDB
 * 
 * @author Peibol
 */
@Alternative
public class RoleRepositoryMongo implements IRoleRepository {

    @Inject
    private DatabaseMongoDB db;

    public RoleRepositoryMongo(DatabaseMongoDB db) throws Exception {
        this.db = db;
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
