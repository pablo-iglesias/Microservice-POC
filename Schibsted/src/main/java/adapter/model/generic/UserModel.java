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
		
	public Integer getUserIdByUseranameAndPassword(String username, String password) throws Exception{
		return model.getUserIdByUseranameAndPassword(username, password);
	}
	
	public String getUsernameByUserId(int uid) throws Exception{
		return model.getUsernameByUserId(uid);
	}
}
