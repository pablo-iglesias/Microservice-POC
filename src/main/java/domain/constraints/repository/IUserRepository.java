package domain.constraints.repository;

import domain.entity.User;

/**
 * All repos of the type User must implement this
 * 
 * @author Peibol
 */
public interface IUserRepository {

    User[] getAllUsers() throws Exception;
    User getUser(Integer uid) throws Exception;
    User getUser(String username) throws Exception;
    User getUser(String username, String password) throws Exception;
    Integer insertUser(User user) throws Exception;
    boolean updateUser(User user) throws Exception;
    boolean deleteUser(Integer uid) throws Exception;
}
