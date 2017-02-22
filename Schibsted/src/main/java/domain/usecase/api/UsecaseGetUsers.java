package domain.usecase.api;

import java.util.Arrays;
import java.util.Vector;

import domain.entity.Role;
import domain.entity.User;
import domain.factory.RoleFactory;
import domain.factory.UserFactory;
import domain.usecase.Usecase;

public class UsecaseGetUsers extends Usecase{

	// Factory
	private UserFactory userFactory;
	private RoleFactory roleFactory;
	
	// Output data
	public User[] users = null;
	public Role[] roles = null;
		
	public UsecaseGetUsers() throws Exception{
		userFactory = new UserFactory();
		roleFactory = new RoleFactory();
	}
	
	public UsecaseGetUsers(UserFactory userFactory, RoleFactory roleFactory){
		this.userFactory = userFactory;
		this.roleFactory = roleFactory;
	}
	
	public boolean execute() throws Exception{
		
		users = userFactory.create();
		
		if(users != null){
		
			Vector<Integer> rolesVector = new Vector<Integer>();
			
			for(User user : users){
				for(int role : user.getRoles()){
					if(!rolesVector.contains(role)){
						rolesVector.add(role);
					}
				}
			}
			
			int[] rolesArray = Arrays.stream(rolesVector.toArray()).mapToInt(o -> (int)o).toArray();
			
			roles = roleFactory.createByIds(rolesArray);
			
			return true;
		}
		
		return false;
	}
}
