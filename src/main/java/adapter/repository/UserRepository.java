package adapter.repository;

import java.util.Vector;

/**
 * All repos of the type User must implement this
 * 
 * @author Peibol
 */
public interface UserRepository {

    Vector<Object[]> getUsers() throws Exception;
    boolean selectUserExistsByUseraname(String username) throws Exception;
    Integer selectUserIdByUseranameAndPassword(String username, String password) throws Exception;
    Integer selectUserIdByUseraname(String username) throws Exception;
    String selectUsernameByUserId(Integer uid) throws Exception;
    boolean selectUserIsAdminRole(Integer uid) throws Exception;
    boolean selectUserExists(Integer uid) throws Exception;
    Integer insertUser(String username, String password) throws Exception;
    boolean updateUser(Integer uid, String username, String password) throws Exception;
    boolean deleteUser(Integer uid) throws Exception;
}
