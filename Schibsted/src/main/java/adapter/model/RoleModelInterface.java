package adapter.model;

import java.util.Vector;

/**
 * All models of the type Role must implement this
 * 
 * @author Peibol
 */
public interface RoleModelInterface{

	public abstract Vector<Object[]> getRolesByIds(int rids[]) throws Exception;
	public abstract int[] getRoleIdsByUserId(int uid) throws Exception;
}