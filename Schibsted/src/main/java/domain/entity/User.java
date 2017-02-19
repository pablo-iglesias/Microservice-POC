package domain.entity;

public class User {

	private int uid;
	private String username;
	
	public User(int uid, String username){
		this.uid = uid;
		this.username = username;
	}

	public int getUid() {
		return uid;
	}

	public String getUsername() {
		return username;
	}
}
