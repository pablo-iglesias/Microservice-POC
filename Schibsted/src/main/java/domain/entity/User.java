package domain.entity;

public class User {

	private int id;
	private String username;
	private int[] roles;
	
	public User(int id, String username, int[] roles){
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

	public int[] getRoles() {
		return roles;
	}
}
