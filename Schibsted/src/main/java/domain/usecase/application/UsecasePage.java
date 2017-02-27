package domain.usecase.application;

import adapter.model.RoleModel;
import adapter.model.UserModel;

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
	public int page = 0;
			
	// Output data
	public String username = "";
	public boolean allowed = false;
	
	public UsecasePage(UserModel userModel, RoleModel roleModel) throws Exception{
		userFactory = new UserFactory(userModel, roleModel);
		roleFactory = new RoleFactory(roleModel);
	}
	
	public UsecasePage(UserFactory userFactory, RoleFactory roleFactory){
		this.userFactory = userFactory;
		this.roleFactory = roleFactory;
	}
	
	public boolean execute() throws Exception{
		
		User user = userFactory.create(uid);
				
		if(user != null){
			username = user.getUsername();
			
			Role[] roles = roleFactory.createByIds(user.getRoles());
			
			for(Role role : roles){
				if(role.getPage().matches("page_" + page)){
					allowed = true;
				}
			}
			return true;
		}
		
		return false;
	}
}
