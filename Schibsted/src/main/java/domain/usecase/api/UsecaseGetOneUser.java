package domain.usecase.api;

import domain.entity.Role;
import domain.entity.User;
import domain.factory.RoleFactory;
import domain.factory.UserFactory;
import domain.usecase.Usecase;

public class UsecaseGetOneUser extends Usecase{

	// Factory
	private UserFactory userFactory;
	private RoleFactory roleFactory;
	
	// Input data
	public int uid = 0;
	
	// Output data
	public User user = null;
	public Role[] roles = new Role[]{};
		
	public UsecaseGetOneUser() throws Exception{
		userFactory = new UserFactory();
		roleFactory = new RoleFactory();
	}
	
	public UsecaseGetOneUser(UserFactory userFactory, RoleFactory roleFactory){
		this.userFactory = userFactory;
		this.roleFactory = roleFactory;
	}
	
	public boolean execute() throws Exception{
		
		user = userFactory.create(uid);
		
		if(user != null){
			roles = roleFactory.createByIds(user.getRoles());
			
			return true;
		}
		
		return false;
	}

}