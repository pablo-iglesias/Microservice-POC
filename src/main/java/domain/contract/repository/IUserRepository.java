package domain.contract.repository;

import domain.entity.User;

/**
 * All repos of the type User must implement this
 * 
 * @author Peibol
 */
public interface IUserRepository {

    User[] getAllUsers() throws Exception;
    boolean findUser(User user) throws Exception;
    Integer insertUser(User user) throws Exception;
    boolean updateUser(User user) throws Exception;
    boolean deleteUser(User user) throws Exception;
}
