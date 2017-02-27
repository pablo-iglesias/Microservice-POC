package domain.factory;

import java.util.Vector;

import adapter.model.RoleModel;
import adapter.model.UserModel;

import domain.Helper;
import domain.entity.User;

public class UserFactory {

	private static final int USER_ID = 0;
	private static final int USER_NAME = 1;
	
	private UserModel userModel;
	private RoleModel roleModel;
	
	public UserFactory(UserModel userModel, RoleModel roleModel) throws Exception{
		this.userModel = userModel;
		this.roleModel = roleModel;
	}
	
	/**
	 * Create an array of User objects representing all the users in existance
	 * 
	 * @return
	 * @throws Exception
	 */
	public User[] create() throws Exception{
		
		Vector<Object[]> records = userModel.getUsers();
		
		if(records != null){
			User[] users = new User[records.size()];
			for(int i = 0; i < users.length; i++){
				Integer uid  = (Integer)records.get(i)[USER_ID];
				String  name =  (String)records.get(i)[USER_NAME];
				users[i] = new User(uid, name, roleModel.getRoleIdsByUserId(uid));
			}
			return users;
		}
		
		return null;
	}
	
	/**
	 * Create a User object from the user id
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public User create(int uid) throws Exception{
		
		String name = userModel.selectUsernameByUserId(uid);
		if(name != null){
			return new User(uid, name, roleModel.getRoleIdsByUserId(uid));
		}
		return null;
	}
	
	/**
	 * Create user object from username and password
	 * 
	 * @param name
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public User create(String name, String password) throws Exception{
		
		if(name != null && password != null){
			String hash = Helper.SHA1(password);
			Integer uid = userModel.selectUserIdByUseranameAndPassword(name, hash);
			if(uid != null){
				return new User(uid, name, roleModel.getRoleIdsByUserId(uid));
			}
		}
		return null;
	}
}
