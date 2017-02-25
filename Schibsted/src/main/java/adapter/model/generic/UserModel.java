package adapter.model.generic;

import java.util.Vector;

import adapter.model.Model;
import adapter.model.UserModelInterface;
import adapter.model.factory.ModelFactory;

/**
 * Standard User model, abstracted from data source
 * 
 * @author Peibol
 */
public class UserModel extends Model implements UserModelInterface {

	private ModelFactory factory;
	private UserModelInterface model;
	
	public UserModel() throws Exception{
		factory = new ModelFactory();
		model = (UserModelInterface) factory.create("User");
	}
	
	public Vector<Object[]> getUsers() throws Exception{
		return model.getUsers();
	}
		
	public Integer selectUserIdByUseranameAndPassword(String username, String password) throws Exception{
		return model.selectUserIdByUseranameAndPassword(username, password);
	}
	
	public String selectUsernameByUserId(Integer uid) throws Exception{
		return model.selectUsernameByUserId(uid);
	}
	
	public boolean	selectUserIsAdminRole(Integer uid) throws Exception{
		return model.selectUserIsAdminRole(uid);
	}
	
	public Integer insertUser(String username, String password) throws Exception{
		return model.insertUser(username, password);
	}
	
	public boolean selectUserExists(Integer uid) throws Exception{
		return model.selectUserExists(uid);
	}
	
	public boolean updateUser(Integer uid, String username, String password) throws Exception{
		return model.updateUser(uid, username, password);
	}
	
	public boolean deleteUser(Integer uid) throws Exception{
		return model.deleteUser(uid);
	}
}
