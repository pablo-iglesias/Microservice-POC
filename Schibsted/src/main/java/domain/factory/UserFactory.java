package domain.factory;

import java.util.Vector;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import adapter.model.generic.UserModel;
import domain.entity.User;

public class UserFactory {

	private UserModel model;
	RoleFactory roleFactory;
	
	public UserFactory(UserModel model, RoleFactory roleFactory) throws Exception{
		this.model = model;
		this.roleFactory = roleFactory;
	}
	
	public UserFactory() throws Exception{
		model = new UserModel();
		roleFactory = new RoleFactory();
	}
	
	public User[] create() throws Exception{
		
		Vector<Object[]> users = model.getUsers();
		
		if(users != null){
			User[] userObjects = new User[users.size()];
			for(int i = 0; i < userObjects.length; i++){
				Integer uid = (Integer)users.get(i)[0];
				String name = (String)users.get(i)[1];
				userObjects[i] = new User(uid, name,  roleFactory.createByUser(uid));
			}
			return userObjects;
		}
		
		return null;
	}
	
	public User create(int uid) throws Exception{
		
		String username = model.getUsernameByUserId(uid);
		if(username != null){
			return new User(uid, username, roleFactory.createByUser(uid));
		}
		return null;
	}
	
	public User create(String username, String password) throws Exception{
		
		if(username != null && password != null){
			String hash = Hashing.sha1().hashString(password, Charsets.UTF_8 ).toString();
			Integer uid = model.getUserIdByUseranameAndPassword(username, hash);
			if(uid != null){
				return new User(uid, username, roleFactory.createByUser(uid));
			}
		}
		return null;
	}
}
