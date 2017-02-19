package domain.factory;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import adapter.model.generic.UserModel;
import domain.entity.Role;
import domain.entity.User;

public class UserFactory {

	UserModel model;
	
	public UserFactory() throws Exception{
		model = new UserModel();
	}
		
	public User create(int uid) throws Exception{
		
		String username = model.getUsernameByUserId(uid);
		if(username != null){
			RoleFactory factory = new RoleFactory();
			Role[] roles = factory.createByUser(uid);
			return new User(uid, username, roles);
		}
		return null;
	}
	
	public User create(String username, String password) throws Exception{
		
		if(username != null && password != null){
			String hash = Hashing.sha1().hashString(password, Charsets.UTF_8 ).toString();
			Integer uid = model.getUserIdByUseranameAndPassword(username, hash);
			if(uid != null){
				RoleFactory factory = new RoleFactory();
				Role[] roles = factory.createByUser(uid.intValue());
				return new User(uid.intValue(), username, roles);
			}
		}
		return null;
	}
}
