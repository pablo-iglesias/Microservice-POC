package domain.usecase.application;

import domain.entity.User;
import domain.entity.Role;
import domain.factory.RoleFactory;
import domain.factory.UserFactory;
import domain.usecase.Usecase;

public class UsecaseWelcome extends Usecase {

	public static final int ROLE_ID = 0;
	public static final int ROLE_NAME = 1;
	
	// Factory
	private UserFactory userFactory;
	private RoleFactory roleFactory;
	
	// Input data
	public int uid = 0;
	
	// Output data
	public String username = "";
	public Role[] roles = new Role[]{};
	
	public UsecaseWelcome() throws Exception{
		userFactory = new UserFactory();
		roleFactory = new RoleFactory();
	}
	
	public UsecaseWelcome(UserFactory userFactory, RoleFactory roleFactory){
		this.userFactory = userFactory;
		this.roleFactory = roleFactory;
	}
	
	public boolean execute() throws Exception{
		
		User user = userFactory.create(uid);
		
		if(user != null){
			username = user.getUsername();
			roles = roleFactory.createByIds(user.getRoles());
			
			return true;
		}
		
		return false;
	}
}
