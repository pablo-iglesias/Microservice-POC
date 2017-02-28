package adapter.model;

import java.util.Vector;

/**
 * All models of the type Role must implement this
 * 
 * @author Peibol
 */
public interface RoleModel{

	public abstract Vector<Object[]> getRolesByIds(Integer rids[]) throws Exception;
	public abstract Integer[] getRoleIdsByUserId(Integer uid) throws Exception;
	public abstract boolean insertUserHasRoles(Integer uid, Integer[] rids) throws Exception;
	public abstract boolean deleteUserHasRoles(Integer uid, Integer[] rids) throws Exception;
	public abstract boolean deleteUserHasRolesByUserId(Integer uid) throws Exception;
}