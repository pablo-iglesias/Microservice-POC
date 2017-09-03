package domain.model;

import java.util.Vector;

/**
 * All repos of the type Role must implement this
 * 
 * @author Peibol
 */
public interface RoleModel {

    Vector<Object[]> getRolesByIds(Integer rids[]) throws Exception;
    Integer[] getRoleIdsByUserId(Integer uid) throws Exception;
    boolean insertUserHasRoles(Integer uid, Integer[] rids) throws Exception;
    boolean deleteUserHasRoles(Integer uid, Integer[] rids) throws Exception;
    boolean deleteUserHasRolesByUserId(Integer uid) throws Exception;
}