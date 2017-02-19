package adapter.response;

import java.util.Map;

import core.entity.Session;

/**
 * This class is a value object for the standard response of a Controller to the underlying architecture
 * 
 * @author Peibol
 */
public class ApplicationResponse {
	
	public static final int RESPONSE_DENIED = -2;
	public static final int RESPONSE_ILEGAL = -1;
	public static final int RESPONSE_OK = 0;
	public static final int RESPONSE_REDIRECT = 1;
	
	private int response;
	private Session session = null;
	private String location;
	private String view;
	private Map<String, Object> data;
	
	public ApplicationResponse(int response){
		setResponseCode(response);
	}
	
	public ApplicationResponse(int response, String view, Map<String, Object> data){
		setResponseCode(response);
		setView(view);
		setData(data);
	}
		
	public int getResponseCode(){
		return response;
	}
	
	public void setResponseCode(int responseCode){
		this.response = responseCode;
	}
	
	public String getView(){
		return view;
	}
	
	public void setView(String view){
		this.view = view;
	}
	
	public Map<String, Object> getData(){
		return data;
	}
	
	public void setData(Map<String, Object> data){
		this.data = data;
	}
	
	public String getLocation(){
		return location;
	}
	
	public void setLocation(String location){
		this.location = location;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
