package adapter.model.relational;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
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
	public Vector<Object[]> getRolesByUserId(int uid) throws SQLException{
					
		db.prepare("SELECT role_id, role_name FROM roles JOIN user_has_role ON role_id = fk_role_id JOIN users ON fk_user_id = user_id WHERE user_id = ? ORDER BY role_name ASC");
		db.add(uid);
				
		if(db.select()){		
			Vector<Object[]> roles = new Vector<Object[]>();
			while(db.next()){
				roles.add(new Object[]{db.getInt("role_id"), db.getString("role_name")});
			}
			return roles;
		}
		else{
			return null;
		}
	}
}
