package adapter.response.api.json;

import domain.entity.Role;
import domain.entity.User;

public class ApiResponseUserResource extends ApiResponse{

	private User user;
	private Role[] roles;
	
	public ApiResponseUserResource(User user, Role[] roles){
		this.setUser(user);
		this.setRoles(roles);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role[] getRoles() {
		return roles;
	}

	public void setRoles(Role[] roles) {
		this.roles = roles;
	}
	
	
}
