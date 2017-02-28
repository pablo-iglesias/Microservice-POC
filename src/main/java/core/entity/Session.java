package core.entity;

import java.util.Calendar;
import java.util.Date;

public class Session extends DataContainer{
	
	private static final int SESSION_LENGTH_MINUTES = 5;
	
	private int uid;
	private Date touched;
	private String sessionToken;
	
	public Session(int uid, String sessionToken){
		super();
		this.uid = uid;
		this.sessionToken = sessionToken;
		touch();
	}

	public int getUserId() {
		return uid;
	}

	public String getSessionToken() {
		return sessionToken;
	}
	
	public void touch(){
		touched = new Date();
	}
	
	public Date getExpiryTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(touched);
		calendar.add(Calendar.MINUTE, SESSION_LENGTH_MINUTES);
		return calendar.getTime();
	}
	
	public boolean isExpired(){
		return getExpiryTime().before(new Date());
	}
}
