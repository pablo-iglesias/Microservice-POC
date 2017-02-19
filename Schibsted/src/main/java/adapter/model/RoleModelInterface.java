package adapter.model;

import java.sql.SQLException;

/**
 * All models of the type Role must implement this
 * 
 * @author Peibol
 */
public interface RoleModelInterface{

	public abstract String getRoleNameByRoleId(int rid) throws SQLException;
	public abstract Integer[] getRolesIdsByUserId(int uid) throws Exception;
}