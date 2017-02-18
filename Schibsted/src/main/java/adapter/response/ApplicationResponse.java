package adapter.response;

import java.util.Map;

public class ApplicationResponse {
	
	private int status;
	private String view;
	private Map<String, String> data;
	
	public ApplicationResponse(int statusCode){
		setStatusCode(statusCode);
	}
	
	public ApplicationResponse(int status, String view, Map<String, String> data){
		setStatusCode(status);
		setView(view);
		setData(data);
	}
	
	public void setStatusCode(int statusCode){
		this.status = statusCode;
	}
	
	public int getStatusCode(){
		return status;
	}
	
	public void setView(String view){
		this.view = view;
	}
	
	public String getView(){
		return view;
	}
	
	public Map<String, String> getData(){
		return data;
	}
	
	public void setData(Map<String, String> data){
		this.data = data;
	}
}
