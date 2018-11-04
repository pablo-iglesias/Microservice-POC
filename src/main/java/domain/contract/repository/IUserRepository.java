package domain.contract.repository;

import domain.entity.User;

/**
 * All repos of the type User must implement this
 * 
 * @author Peibol
 */
public interface IUserRepository {

    User[] getAllUsers();
    boolean findUser(User user);
    Integer insertUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(User user);
}
