package domain.usecase.application;

import domain.entity.Role;
import domain.entity.User;
import domain.factory.RoleFactory;
import domain.factory.UserFactory;
import domain.usecase.Usecase;

public class UsecasePage extends Usecase{

	// Factory
	private UserFactory userFactory;
	private RoleFactory roleFactory;
		
	// Input data
	public int uid = 0;
	public String page = "";
			
	// Output data
	public String username = "";
	public boolean allowed = false;
	
	public UsecasePage() throws Exception{
		userFactory = new UserFactory();
		roleFactory = new RoleFactory();
	}
	
	public UsecasePage(UserFactory userFactory, RoleFactory roleFactory){
		this.userFactory = userFactory;
		this.roleFactory = roleFactory;
	}
	
	public boolean execute() throws Exception{
		
		User user = userFactory.create(uid);
		username = user.getUsername();
		
		if(user != null){
			
			Role[] roles = roleFactory.createByIds(user.getRoles());
			
			for(Role role : roles){
				if(role.getName().matches("PAGE_" + page)){
					allowed = true;
				}
			}
			return true;
		}
		
		return false;
	}
}
