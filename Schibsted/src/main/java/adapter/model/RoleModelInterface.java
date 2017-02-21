package adapter.model;

import java.util.Vector;

/**
 * All models of the type Role must implement this
 * 
 * @author Peibol
 */
public interface RoleModelInterface{

	public abstract Vector<Object[]> getRolesByUserId(int uid) throws Exception;
}