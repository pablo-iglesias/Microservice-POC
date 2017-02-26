package adapter.response.api.json;

import javax.xml.bind.annotation.XmlRootElement;

import domain.entity.Role;
import domain.entity.User;

@XmlRootElement
public class ApiResponseUserCollection extends ApiResponse{

	public String getXml() throws Exception{
		
        return ApiResponse.getXml(this);
	}
	
	private User[] users;
	private Role[] roles;
	
	public ApiResponseUserCollection(){
		users = new User[0];
		roles = new Role[0];
	}
	
	public ApiResponseUserCollection(User[] users, Role[] roles){
		this.setUsers(users);
		this.setRoles(roles);
	}

	public User[] getUsers() {
		return users;
	}

	public void setUsers(User[] users) {
		if(users != null){
			this.users = users;
		}
		else{
			users = new User[0];
		}
	}

	public Role[] getRoles() {
		return roles;
	}

	public void setRoles(Role[] roles) {
		if(roles != null){
			this.roles = roles;
		}
		else{
			roles = new Role[0];
		}
	}
}
