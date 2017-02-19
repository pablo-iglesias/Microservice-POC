package domain.application.usecase;

import domain.entity.Role;
import domain.entity.User;
import domain.factory.UserFactory;

public class UsecasePage extends Usecase{

	// Factory
	private UserFactory factory;
		
	// Input data
	public int uid = 0;
	public String page = "";
			
	// Output data
	public boolean allowed = false;
	
	public UsecasePage(UserFactory factory){
		this.factory = factory;
	}
	
	public boolean execute() throws Exception{
		
		User user = factory.create(uid);
		
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
