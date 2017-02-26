package domain.entity;

import javax.xml.bind.annotation.XmlElement;

public class User {

	@XmlElement(name="id")
	private int id;
	
	@XmlElement(name="username")
	private String username = null;
	
	private String password = null;
	
	@XmlElement(name="role_id")
	private Integer[] roles = null;
	
	public User(int id, String username, Integer[] roles){
		this.id = id;
		this.username = username;
		this.roles = roles;
		password = null;
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
