package adapter.model.relational;

import java.sql.SQLException;
import java.util.Vector;

import core.Server;
import core.database.DatabaseRelational;
import adapter.model.Model;
import adapter.model.UserModel;

/**
 * User model that expects the database to be relational and SQL driven
 * 
 * @author Peibol
 */
public class UserModelRelational extends Model implements UserModel{

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
	public String selectUsernameByUserId(Integer uid) throws SQLException{
		
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
	 * Returns true if a user with this username already exists
	 * 
	 */
	public boolean selectUserExistsByUseraname(String username) throws Exception{
		
		db.prepare("SELECT 1 FROM users WHERE user_name = ?");
		db.add(username);
		
		if(db.selectOne()){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Takes user name and hashed user password and returns the corresponding user id, used for authentication 
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Integer selectUserIdByUseranameAndPassword(String username, String password) throws SQLException{
					
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
	
	/**
	 * Takes user name and returns the corresponding user id, used for checking if the username is already taken 
	 * 
	 */
	public Integer selectUserIdByUseraname(String username) throws Exception{

		db.prepare("SELECT user_id FROM users WHERE user_name = ?");
		db.add(username);
		
		if(db.selectOne()){
			return new Integer(db.getInt("user_id"));
		}
		else{
			return null;
		}
	}
	
	/**
	 * Return true if the user has the ADMIN role
	 * 
	 */
	public boolean	selectUserIsAdminRole(Integer uid) throws SQLException
	{
		db.prepare("SELECT 1 FROM roles JOIN user_has_role ON role_id = fk_role_id JOIN users ON fk_user_id = user_id WHERE user_id = ? AND role_name = 'ADMIN'");
		db.add(uid);
		
		if(db.selectOne()){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Return true if a user with this id already exists
	 * 
	 */
	public boolean	selectUserExists(Integer uid) throws SQLException
	{
		db.prepare("SELECT 1 FROM users WHERE user_id = ?");
		db.add(uid);
		
		if(db.selectOne()){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Inserts new user
	 */
	public Integer insertUser(String username, String password) throws SQLException{
		
		db.prepare("INSERT INTO users(user_name, user_password) VALUES(?, ?)");
		db.add(username);
		db.add(password);
		
		return db.insert();
	}
	
	/**
	 * Updates existing user
	 */
	public boolean updateUser(Integer uid, String username, String password) throws SQLException{
		
		db.prepare("UPDATE users SET user_name = ?, user_password = ? WHERE user_id = ?");
		db.add(username);
		db.add(password);
		db.add(uid);
		
		return db.update();
	}
	
	/**
	 * Removes existing user
	 */
	public boolean deleteUser(Integer uid) throws SQLException{

		db.prepare("DELETE FROM users WHERE user_id = ?");
		db.add(uid);
		
		return db.delete();
	}
}
