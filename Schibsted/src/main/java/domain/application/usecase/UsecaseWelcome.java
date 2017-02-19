package domain.application.usecase;

import domain.entity.User;
import domain.entity.Role;

import domain.factory.UserFactory;

public class UsecaseWelcome extends Usecase {

	// Factory
	private UserFactory factory;
	
	// Input data
	public int uid = 0;
	
	// Output data
	public String username = "";
	public String[][] roles = null;
	
	public UsecaseWelcome(UserFactory factory){
		this.factory = factory;
	}
	
	public boolean execute() throws Exception{
		
		User user = factory.create(uid);
		
		if(user != null){
			username = user.getUsername();
			Role[] roles = user.getRoles();
			if(roles != null && roles.length > 0){
				this.roles = new String[roles.length][];
				for(int i = 0; i < roles.length; i++){
					this.roles[i] = new String[2];
					this.roles[i][0] = roles[i].getName();
					this.roles[i][1] = "";
					if(!roles[i].getName().matches("ADMIN")){
						this.roles[i][1] = roles[i].getName().toLowerCase();
					}
				}
			}
			
			return true;
		}
		
		return false;
	}
}
