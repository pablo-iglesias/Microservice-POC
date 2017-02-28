package adapter.model;

import java.util.Vector;

/**
 * All models of the type User must implement this
 * 
 * @author Peibol
 */
public interface UserModel {

    public abstract Vector<Object[]> getUsers() throws Exception;
    public abstract boolean selectUserExistsByUseraname(String username) throws Exception;
    public abstract Integer selectUserIdByUseranameAndPassword(String username, String password) throws Exception;
    public abstract Integer selectUserIdByUseraname(String username) throws Exception;
    public abstract String selectUsernameByUserId(Integer uid) throws Exception;
    public abstract boolean selectUserIsAdminRole(Integer uid) throws Exception;
    public abstract boolean selectUserExists(Integer uid) throws Exception;
    public abstract Integer insertUser(String username, String password) throws Exception;
    public abstract boolean updateUser(Integer uid, String username, String password) throws Exception;
    public abstract boolean deleteUser(Integer uid) throws Exception;
}
