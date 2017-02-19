package adapter.model.relational;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import core.Server;
import core.database.DatabaseRelational;

import adapter.model.Model;
import adapter.model.interfaces.RoleModelInterface;

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
	
	public String getRoleNameByRoleId(int rid) throws SQLException{
		
		db.prepare("SELECT role_name FROM roles WHERE role_id = ?");
		db.add(rid);
				
		if(db.selectOne()){
			return db.getString("role_name");
		}
		else{
			return null;
		}
	}
	
	public Integer[] getRolesIdsByUserId(int uid) throws SQLException{
					
		db.prepare("SELECT role_id FROM roles JOIN user_has_role ON role_id = fk_role_id JOIN users ON fk_user_id = user_id WHERE user_id = ?");
		db.add(uid);
				
		if(db.select()){			
			List<Integer> rids = new ArrayList<Integer>();
			while(db.next()){
				rids.add(db.getInt("role_id"));
			}
			return rids.toArray(new Integer[rids.size()]);
		}
		else{
			return null;
		}
	}
}
