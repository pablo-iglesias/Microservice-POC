package domain.usecase.application;

import domain.entity.Role;
import domain.entity.User;
import domain.factory.UserFactory;
import domain.usecase.Usecase;

public class UsecasePage extends Usecase{

	// Factory
	public UserFactory factory;
		
	// Input data
	public int uid = 0;
	public String page = "";
			
	// Output data
	public String username = "";
	public boolean allowed = false;
	
	public boolean execute() throws Exception{
		
		User user = factory.create(uid);
		username = user.getUsername();
		
		if(user != null){
			for(Role role : user.getRoles()){
				if(role.getName().matches("PAGE_" + page)){
					allowed = true;
				}
			}
			return true;
		}
		
		return false;
	}
}
