package domain.application.usecase;

import domain.entity.User;
import domain.factory.UserFactory;

public class UsecaseWelcome extends Usecase {

	// Input data
	public int uid = 0;
	
	// Output data
	public String username;
	
	public boolean execute() throws Exception{
		
		UserFactory factory = new UserFactory();
		User user = factory.create(uid);
		
		if(user != null){
			username = user.getUsername();
			return true;
		}
		
		return false;
	}
}
