package adapter.response.api.json;

import domain.entity.Role;
import domain.entity.User;

public class ApiResponseUserCollection extends ApiResponse{

	private User[] users;
	private Role[] roles;
	
	public ApiResponseUserCollection(User[] users, Role[] roles){
		this.setUsers(users);
		this.setRoles(roles);
	}

	public User[] getUsers() {
		return users;
	}

	public void setUsers(User[] users) {
		this.users = users;
	}

	public Role[] getRoles() {
		return roles;
	}

	public void setRoles(Role[] roles) {
		this.roles = roles;
	}
}
