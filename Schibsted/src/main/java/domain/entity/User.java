package domain.entity;

import javax.xml.bind.annotation.XmlElement;

public class User {

	@XmlElement(name="id")
	private int id;
	
	@XmlElement(name="name")
	private String username = null;
	
	private String password = null;
	
	@XmlElement(name="role_id")
	private Integer[] roles = null;
	
	public User(int id, String username, String password, Integer[] roles){
		this.id = id;
		this.username = username;
		this.password = password;
		this.roles = roles;
	}
	
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
	
	public boolean equals(Object o){
		
		if(o.getClass() != this.getClass()){
			return false;
		}
			
		User user = (User) o;
		
		if(	user.getId() != id || 
			user.getUsername() != username ||
			user.getPassword() != password ||
			user.getRoles().length != roles.length){
			
			return false;
		}
			
		for(int i = 0; i < roles.length; i++){
		
			if(user.getRoles()[i] != roles[i]){
				return false;
			}
		}
			
		return true;
	}
}
