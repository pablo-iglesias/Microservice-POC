package domain.entity;

public class User {

	private int id;
	private String username = null;
	private String password = null;
	private Integer[] roles = null;
	
	public User(int id, String username, Integer[] roles){
		this.id = id;
		this.username = username;
		this.roles = roles;
		this.password = null;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public Integer[] getRoles() {
		return roles;
	}
	
	public String getPassword() {
		return password;
	}
}
