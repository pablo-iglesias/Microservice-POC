package adapter.repository.relational;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import core.Server;
import core.database.DatabaseRelational;

import domain.entity.Role;
import domain.contract.repository.IRoleRepository;
import domain.entity.User;

import javax.enterprise.inject.Alternative;

/**
 * Role repo that expects the database to be relational and SQL driven
 * 
 * @author Peibol
 */
@Alternative
public class RoleRepositoryRelational implements IRoleRepository {

    private DatabaseRelational db;

    public RoleRepositoryRelational() {
        db = Server.getInstance(DatabaseRelational.class);
    }

    /**
     * Takes a user id and returns the ids of the roles assigned to it
     */
    public Role[] getRolesByUser(User user) {

        db.prepare(
        "SELECT role_id, role_name, role_page FROM roles JOIN user_has_role ON role_id = fk_role_id WHERE fk_user_id = ? ORDER BY role_name ASC"
        );
        db.add(user.getId());

        List<Role> roles = new ArrayList<>();

        if (db.select()) {

            while (db.next()) {
                roles.add(
                    new Role(
                        db.getInt("role_id"),
                        db.getString("role_name"),
                        db.getString("role_page")
                    )
                );
            }
        }

        return Arrays.copyOf(roles.toArray(), roles.size(), Role[].class);
    }

    /**
     * Takes a user ids and returns the ids of the roles assigned to them
     */
    public Role[] getRolesByUsers(User[] users) {

        Integer[] userIds =
            Arrays.stream(users)
                .map(user -> user.getId())
                .toArray(Integer[]::new);

        String template =
            Arrays.stream(userIds)
                .map(userId -> "?")
                .collect(Collectors.joining(","));

        db.prepare(
                "SELECT DISTINCT role_id, role_name, role_page FROM roles JOIN user_has_role ON role_id = fk_role_id WHERE fk_user_id IN (" + template + ") ORDER BY role_name ASC"
        );

        for(Integer userId : userIds){
            db.add(userId);
        }

        List<Role> roles = new ArrayList<>();

        if (db.select()) {

            while (db.next()) {
                roles.add(
                    new Role(
                        db.getInt("role_id"),
                        db.getString("role_name"),
                        db.getString("role_page")
                    )
                );
            }
        }

        return Arrays.copyOf(roles.toArray(), roles.size(), Role[].class);
    }
}
