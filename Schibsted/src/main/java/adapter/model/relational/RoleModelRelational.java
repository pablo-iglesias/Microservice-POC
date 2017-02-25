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
	public Vector<Object[]> getRolesByIds(Integer rids[]) throws SQLException{
		
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
	public Integer[] getRoleIdsByUserId(Integer uid) throws SQLException{
					
		db.prepare("SELECT role_id FROM roles JOIN user_has_role ON role_id = fk_role_id JOIN users ON fk_user_id = user_id WHERE user_id = ? ORDER BY role_name ASC");
		db.add(uid);
		
		Vector<Integer> roles = new Vector<Integer>();
		
		if(db.select()){
			
			while(db.next()){
				roles.add(new Integer(db.getInt("role_id")));
			}
		}
		
		Object[] roleObjects = roles.toArray();
		return Arrays.copyOf(roleObjects, roleObjects.length, Integer[].class);
	}
	
	/**
	 * Insert rows in the table user_has_role
	 */
	public boolean insertUserHasRoles(Integer uid, Integer[] rids) throws Exception{
		
		boolean success = true;
		
		for(int role : rids){
			db.prepare("INSERT INTO user_has_role(fk_user_id, fk_role_id) VALUES(?, ?)");
			db.add(uid);
			db.add(role);
			success = success && db.insert() != null;
		}
		
		return success;
	}
	
	/**
	 * Delete rows from the table user_has_roles
	 */
	public boolean deleteUserHasRoles(Integer uid, Integer[] rids) throws Exception{
		
		String[] interrogators = new String[rids.length];
		Arrays.fill(interrogators, "?");
		
		db.prepare("DELETE FROM user_has_role WHERE fk_user_id = ? AND fk_role_id IN (" + String.join(", ", interrogators) + ")");
		
		db.add(uid);
		
		for(int rid : rids){
			db.add(Integer.toString(rid));
		}
		
		return db.delete();
	}
	
	/**
	 * Delete rows from the table user_has_roles
	 */
	public boolean deleteUserHasRolesByUserId(Integer uid) throws Exception{
				
		db.prepare("DELETE FROM user_has_role WHERE fk_user_id = ?");
		db.add(uid);
				
		return db.delete();
	}
}
