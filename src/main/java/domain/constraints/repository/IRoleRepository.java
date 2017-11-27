package domain.constraints.repository;

import domain.entity.Role;
import domain.entity.User;

/**
 * All repos of the type Role must implement this
 * 
 * @author Peibol
 */
public interface IRoleRepository {

    Role getRole(Integer rid) throws Exception;
    Role[] getRolesByUser(User user) throws Exception;
}