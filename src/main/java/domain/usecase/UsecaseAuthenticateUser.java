package domain.usecase;

import adapter.model.RoleModel;
import adapter.model.UserModel;
import domain.entity.User;
import domain.factory.UserFactory;

public class UsecaseAuthenticateUser extends Usecase {

	// Factory
	private UserFactory factory;
		
	// Input data
	public String username = "";
	public String password = "";
	
	// Output data
	public int uid = 0;
	
	public UsecaseAuthenticateUser(UserModel userModel, RoleModel roleModel) throws Exception{
		factory = new UserFactory(userModel, roleModel);
	}
	
	public UsecaseAuthenticateUser(UserFactory factory){
		this.factory = factory;
	}
	
	public boolean execute() throws Exception{
		
		User user = factory.create(username, password);
		
		if(user != null){
			uid = user.getId();
			return true;
		}
		
		return false;
	}
}
