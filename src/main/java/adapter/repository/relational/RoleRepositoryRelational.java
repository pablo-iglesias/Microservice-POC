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

/**
 * Role repo that expects the database to be relational and SQL driven
 * 
 * @author Peibol
 */
public class RoleRepositoryRelational implements IRoleRepository {

    private DatabaseRelational db;

    public RoleRepositoryRelational() throws Exception {
        db = (DatabaseRelational) Server.getDatabase();
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

    /**
     * Insert row in the table user_has_role
     */
    public boolean setRolesToUser(User user, Role[] roles) throws SQLException {

        if(removeAllRolesFromUser(user.getId())){

            for(Role role : roles) {
                db.prepare("INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(?, ?)");
                db.add(user.getId());
                db.add(role.getId());
                db.insert();
            }
            return true;
        }

        return false;
    }

    /**
     * Delete rows from the table user_has_roles
     */
    public boolean removeAllRolesFromUser(Integer uid) throws SQLException {

        db.prepare("DELETE FROM user_has_role WHERE fk_user_id = ?");
        db.add(uid);

        return db.delete();
    }
}
