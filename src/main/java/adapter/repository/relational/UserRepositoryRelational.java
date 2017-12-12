package adapter.repository.relational;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Strings;

import core.Server;
import core.database.DatabaseRelational;

import domain.entity.User;
import domain.constraints.repository.IUserRepository;

/**
 * User repo that expects the database to be relational and SQL driven
 * 
 * @author Peibol
 */
public class UserRepositoryRelational implements IUserRepository {

    private DatabaseRelational db;

    public UserRepositoryRelational() throws Exception {
        db = (DatabaseRelational) Server.getDatabase();
    }

    /**
     * Get all users
     */
    public User[] getAllUsers() throws SQLException {

        db.prepare("SELECT user_id, user_name FROM users");

        if (db.select()) {
            List<User> users = new ArrayList<>();
            while (db.next()) {
                Integer uid = db.getInt("user_id");
                String username = db.getString("user_name");
                users.add(new User(uid, username, selectUserRoles(uid)));
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
    public boolean findUser(User user) throws SQLException {

        if(user.getId() != null){
            return findUserById(user);
        }
        else if(!Strings.isNullOrEmpty(user.getUsername())) {

            if(Strings.isNullOrEmpty(user.getPassword()))
                return findUserByName(user);
            else
                return findUserByNamePass(user);
        }

        return false;
    }

    /**
     * Get user
     */
    private boolean findUserById(User user) throws SQLException {

        db.prepare("SELECT user_name FROM users WHERE user_id = ?");
        db.add(user.getId());

        if (db.selectOne()) {
            String username = db.getString("user_name");
            user.setUsername(username);
            user.setRoles(selectUserRoles(user.getId()));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get user
     */
    private boolean findUserByName(User user) throws SQLException {

        db.prepare("SELECT user_id FROM users WHERE user_name = ?");
        db.add(user.getUsername());

        if (db.selectOne()) {
            Integer uid = db.getInt("user_id");
            user.setId(uid);
            user.setRoles(selectUserRoles(uid));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get user
     */
    private boolean findUserByNamePass(User user) throws SQLException {

        db.prepare("SELECT user_id FROM users WHERE user_name = ? AND user_password = ?");
        db.add(user.getUsername());
        db.add(user.getPassword());

        if (db.selectOne()) {
            Integer uid = db.getInt("user_id");
            user.setId(uid);
            user.setRoles(selectUserRoles(uid));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Inserts new user
     */
    public Integer insertUser(User user) throws SQLException {

        db.prepare("INSERT INTO users(user_name, user_password) VALUES(?, ?)");
        db.add(user.getUsername());
        db.add(user.getPassword());

        Integer uid = db.insert();

        if(uid != null) {
            setRolesToUser(uid, user.getRoleIds());
            return uid;
        }

        return null;
    }

    /**
     * Updates existing user
     */
    public boolean updateUser(User user) throws SQLException {

        db.prepare("UPDATE users SET user_name = ?, user_password = ? WHERE user_id = ?");
        db.add(user.getUsername());
        db.add(user.getPassword());
        db.add(user.getId());

        if(db.update()){
            return setRolesToUser(user.getId(), user.getRoleIds());
        }

        return false;
    }

    /**
     * Removes existing user
     */
    public boolean deleteUser(User user) throws SQLException {

        db.prepare("DELETE FROM users WHERE user_id = ?");
        db.add(user.getId());

        if(db.delete()){
            return removeAllRolesFromUser(user.getId());
        }

        return false;
    }

    private Integer[] selectUserRoles(Integer uid) throws SQLException{

        DatabaseRelational db = (DatabaseRelational) Server.getDatabase();
        db.prepare("SELECT fk_role_id FROM user_has_role WHERE fk_user_id = ? ORDER BY fk_role_id ASC");
        db.add(uid);

        List<Integer> roles = new ArrayList<>();

        if (db.select()) {
            while (db.next()) {
                roles.add(db.getInt("fk_role_id"));
            }
        }

        return Arrays.copyOf(roles.toArray(), roles.size(), Integer[].class);
    }

    /**
     * Insert row in the table user_has_role
     */
    private boolean setRolesToUser(Integer uid, Integer[] rids) throws SQLException {

        if(removeAllRolesFromUser(uid)){
            for (Integer roleId : rids) {
                db.prepare("INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(?, ?)");
                db.add(uid);
                db.add(roleId);
                db.insert();
            }
            return true;
        }

        return false;
    }

    /**
     * Delete rows from the table user_has_roles
     */
    private boolean removeAllRolesFromUser(Integer uid) throws SQLException {

        db.prepare("DELETE FROM user_has_role WHERE fk_user_id = ?");
        db.add(uid);

        return db.delete();
    }
}
