package domain.usecase.api;

import domain.entity.User;
import domain.factory.UserFactory;
import domain.usecase.Usecase;

public class UsecaseGetOneUser extends Usecase{

	// Factory
	private UserFactory factory;
	
	// Input data
	public int uid = 0;
	
	// Output data
	public User user = null;
		
	public UsecaseGetOneUser() throws Exception{
		factory = new UserFactory();
	}
	
	public UsecaseGetOneUser(UserFactory factory){
		this.factory = factory;
	}
	
	public boolean execute() throws Exception{
		
		user = factory.create(uid);

		if(user != null){
			return true;
		}
		
		return false;
	}

}