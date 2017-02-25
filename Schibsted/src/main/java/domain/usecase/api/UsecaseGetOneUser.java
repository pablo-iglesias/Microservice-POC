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
	public Integer uid = null;
	
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
		
		if(uid != null){
			user = userFactory.create(uid);
			
			if(user == null){
				return false;
			}
			
			roles = roleFactory.createByIds(user.getRoles());
			return true;
		}
		
		return false;
	}

}