package adapter.repository.nosql;

import core.Server;
import domain.contract.repository.IRoleRepository;
import com.mongodb.client.model.Filters;
import core.database.DatabaseMongoDB;
import domain.entity.Role;
import domain.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.Alternative;

/**
 * Role repo that expects the database to be MongoDB
 * 
 * @author Peibol
 */
@Alternative
public class RoleRepositoryMongo implements IRoleRepository {

    private DatabaseMongoDB db;

    public RoleRepositoryMongo() {
        db = Server.getInstance(DatabaseMongoDB.class);
    }

    /**
     * Get roles by user id
     */
    public Role[] getRolesByUser(User user) {

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

    /**
     * Get roles by user id
     */
    public Role[] getRolesByUsers(User[] users) {

        Integer[] userIds = Arrays.stream(users)
                .map(user -> user.getId())
                .toArray(Integer[]::new);

        List<Role> roles = new ArrayList<>();

        if(db.retrieveDocument("users", Filters.in("id", userIds)))
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
