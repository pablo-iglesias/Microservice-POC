package domain.entity;

public class User {

	private int id;
	private String username;
	private Role[] roles;
	
	public User(int id, String username, Role[] roles){
		this.id = id;
		this.username = username;
		this.roles = roles;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public Role[] getRoles() {
		return roles;
	}
}
