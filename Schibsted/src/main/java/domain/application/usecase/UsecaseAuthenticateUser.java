package domain.application.usecase;

import domain.entity.User;
import domain.factory.UserFactory;

public class UsecaseAuthenticateUser extends Usecase {

	// Input data
	public String username = null;
	public String password = null;
	
	// Output data
	public int uid = 0;
	
	public boolean execute() throws Exception{
		
		UserFactory factory = new UserFactory();
		User user = factory.create(username, password);
		
		if(user != null){
			uid = user.getUid();
			return true;
		}
		
		return false;
	}
}
