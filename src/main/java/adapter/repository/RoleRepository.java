package adapter.repository;

import java.util.Vector;

/**
 * All repos of the type Role must implement this
 * 
 * @author Peibol
 */
public interface RoleRepository {

    Vector<Object[]> getRolesByIds(Integer rids[]) throws Exception;
    Integer[] getRoleIdsByUserId(Integer uid) throws Exception;
    boolean insertUserHasRoles(Integer uid, Integer[] rids) throws Exception;
    boolean deleteUserHasRoles(Integer uid, Integer[] rids) throws Exception;
    boolean deleteUserHasRolesByUserId(Integer uid) throws Exception;
}