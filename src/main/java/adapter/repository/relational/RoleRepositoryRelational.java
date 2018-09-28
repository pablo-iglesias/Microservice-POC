package adapter.repository.relational;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.Server;
import core.database.DatabaseRelational;

import domain.entity.Role;
import domain.constraints.repository.IRoleRepository;
import domain.entity.User;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

/**
 * Role repo that expects the database to be relational and SQL driven
 * 
 * @author Peibol
 */
@Alternative
public class RoleRepositoryRelational implements IRoleRepository {

    @Inject
    private DatabaseRelational db;

    public RoleRepositoryRelational(DatabaseRelational db) throws Exception {
        this.db = db;
    }

    /**
     * Get role
     */
    public Role getRole(Integer rid) throws SQLException {

        db = (DatabaseRelational) Server.getDatabase();
        db.prepare("SELECT role_id, role_name, role_page FROM roles WHERE role_id = ?");
        db.add(rid);

        if (db.selectOne()) {
            return new Role(
                db.getInt("role_id"),
                db.getString("role_name"),
                db.getString("role_page")
            );
        } else {
            return null;
        }
    }

    /**
     * Takes a user id and returns the ids of the roles assigned to it
     */
    public Role[] getRolesByUser(User user) throws SQLException {

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
}
