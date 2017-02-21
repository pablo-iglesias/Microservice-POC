package domain.usecase.api;

import domain.entity.User;
import domain.factory.UserFactory;
import domain.usecase.Usecase;

public class UsecaseGetUsers extends Usecase{

	// Factory
	private UserFactory factory;
	
	// Output data
	public User users[] = null;
		
	public UsecaseGetUsers() throws Exception{
		factory = new UserFactory();
	}
	
	public UsecaseGetUsers(UserFactory factory){
		this.factory = factory;
	}
	
	public boolean execute() throws Exception{
		
		users = factory.create();

		if(users != null){
			return true;
		}
		
		return false;
	}
}
