package adapter.model.relational;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import core.Server;
import core.database.DatabaseRelational;

import adapter.model.Model;
import adapter.model.UserModelInterface;

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
	
	public Vector<Object[]> getUsers() throws SQLException{
		db.prepare("SELECT user_id, user_name FROM users");
		
		if(db.select()){	
			Vector<Object[]> users = new Vector<Object[]>();
			while(db.next()){
				users.add(new Object[]{db.getInt("user_id"), db.getString("user_name")});
			}
			return users;
		}
		else{
			return null;
		}
	}	
	
	/**
	 * Takes user id and returns the user name
	 * 
	 * @return
	 * @throws SQLException 
	 */
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
	
	/**
	 * Takes user name and hashed user password and returns the corresponding user id, used for authentication 
	 * 
	 * @return
	 * @throws SQLException 
	 */
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
