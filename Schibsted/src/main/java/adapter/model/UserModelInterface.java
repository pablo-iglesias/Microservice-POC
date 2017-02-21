package adapter.model;

import java.util.Vector;

/**
 * All models of the type User must implement this
 * 
 * @author Peibol
 */
public interface UserModelInterface{

	public abstract Vector<Object[]> getUsers() throws Exception;
	public abstract Integer getUserIdByUseranameAndPassword(String username, String password) throws Exception;
	public abstract String 	getUsernameByUserId(int uid) throws Exception;
}
