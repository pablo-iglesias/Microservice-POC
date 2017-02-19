package domain.usecase.application;

import domain.entity.User;
import domain.factory.UserFactory;
import domain.usecase.Usecase;

public class UsecaseAuthenticateUser extends Usecase {

	// Factory
	public UserFactory factory;
		
	// Input data
	public String username = "";
	public String password = "";
	
	// Output data
	public int uid = 0;
		
	public boolean execute() throws Exception{
		
		User user = factory.create(username, password);
		
		if(user != null){
			uid = user.getId();
			return true;
		}
		
		return false;
	}
}
