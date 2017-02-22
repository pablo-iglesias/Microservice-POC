package adapter.model.relational;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

import core.Server;
import core.database.DatabaseRelational;

import adapter.model.Model;
import adapter.model.RoleModelInterface;

/**
 * Role model that expects the database to be relational and SQL driven
 * 
 * @author Peibol
 */
public class RoleModelRelational extends Model implements RoleModelInterface{

	private DatabaseRelational db;
	
	public RoleModelRelational() throws Exception{
		db = (DatabaseRelational) Server.getDatabase();
	}
		
	/**
	 * Takes a user id and returns the ids of the roles assigned to it
	 */
	public Vector<Object[]> getRolesByIds(int rids[]) throws SQLException{
		
		String[] interrogators = new String[rids.length];
		Arrays.fill(interrogators, "?");
		
		db.prepare("SELECT role_id, role_name, role_page FROM roles WHERE role_id IN (" + String.join(", ", interrogators) + ")");
		
		for(int rid : rids){
			db.add(Integer.toString(rid));
		}
			
		Vector<Object[]> roles = new Vector<Object[]>();
		
		if(db.select()){		
			
			while(db.next()){
				roles.add(new Object[]{
						db.getInt("role_id"), 
						db.getString("role_name"), 
						db.getString("role_page")});
			}
		}
		
		return roles;
	}
	
	/**
	 * Takes a user id and returns the ids of the roles assigned to it
	 */
	public int[] getRoleIdsByUserId(int uid) throws SQLException{
					
		db.prepare("SELECT role_id FROM roles JOIN user_has_role ON role_id = fk_role_id JOIN users ON fk_user_id = user_id WHERE user_id = ? ORDER BY role_name ASC");
		db.add(uid);
		
		Vector<Integer> roles = new Vector<Integer>();
		
		if(db.select()){
			
			while(db.next()){
				roles.add(new Integer(db.getInt("role_id")));
			}
		}
		
		Object[] roleObjects = roles.toArray();
		Integer[] integerArray = Arrays.copyOf(roleObjects, roleObjects.length, Integer[].class);
		return Arrays.stream(integerArray).mapToInt(Integer::intValue).toArray();
	}
}
