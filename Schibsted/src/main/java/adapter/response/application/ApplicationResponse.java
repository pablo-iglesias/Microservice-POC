package adapter.response.application;

import java.util.Map;

import core.entity.Session;

/**
 * This class is a value object for the standard response of the application controller to the request handler
 * 
 * @author Peibol
 */
public class ApplicationResponse {
	
	// Response codes
	public static final int RESPONSE_DENIED = -2;
	public static final int RESPONSE_ILEGAL = -1;
	public static final int RESPONSE_OK = 0;
	public static final int RESPONSE_REDIRECT = 1;
	
	private int response;						// Application response code
	private Session session = null;				// New session created by the Application
	private String location = null;				// Redirect to this location
	private String startPage = null;			// Page to get redirected after login
	private String view = null;					// Template to render
	private Map<String, Object> data = null;	// Data to populate the template
	
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

	public String getStart() {
		return startPage;
	}

	public void setStart(String start) {
		this.startPage = start;
	}
}
