package adapter.model.generic;

import adapter.factory.ModelFactory;
import adapter.model.Model;
import adapter.model.interfaces.UserModelInterface;

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
	
	public Integer getUserIdByUseranameAndPassword(String username, String password) throws Exception{
		return model.getUserIdByUseranameAndPassword(username, password);
	}
	
	public String getUsernameByUserId(int uid) throws Exception{
		return model.getUsernameByUserId(uid);
	}
}
