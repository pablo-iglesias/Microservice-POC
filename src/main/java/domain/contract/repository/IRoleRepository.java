package domain.contract.repository;

import domain.entity.Role;
import domain.entity.User;

/**
 * All repos of the type Role must implement this
 * 
 * @author Peibol
 */
public interface IRoleRepository {

    Role[] getRolesByUser(User user);
    Role[] getRolesByUsers(User[] users);
}