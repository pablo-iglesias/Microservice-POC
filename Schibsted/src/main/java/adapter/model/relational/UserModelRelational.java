package adapter.model.relational;

import java.sql.SQLException;

import core.Server;
import core.database.DatabaseRelational;

import adapter.model.Model;
import adapter.model.interfaces.UserModelInterface;

/**
 * User model that expects the database to be relational and SQL driven
 * 
 * @author Peibol
 */
public class UserModelRelational extends Model implements UserModelInterface{

	private DatabaseRelational db;
	
	public UserModelRelational() throws Exception{
		db = (DatabaseRelational) Server.getDatabase();
	}
	
	public String getUsernameByUserId(int uid) throws SQLException{
		
		db.prepare("SELECT user_name FROM users WHERE user_id = ?");
		db.add(uid);
				
		if(db.selectOne()){
			return db.getString("user_name");
		}
		else{
			return null;
		}
	}
	
	public Integer getUserIdByUseranameAndPassword(String username, String password) throws SQLException{
					
		db.prepare("SELECT user_id FROM users WHERE user_name = ? AND user_password = ?");
		db.add(username);
		db.add(password);
		
		if(db.selectOne()){
			return new Integer(db.getInt("user_id"));
		}
		else{
			return null;
		}
	}
}
