package core.entity;

import com.sun.net.httpserver.Headers;

public class HttpRequest extends DataContainer{
	
	private String method;
	private Headers headers;
	private String body;
	
	public HttpRequest(String method, Headers headers, String body){
		this.method = method;
		this.headers = headers;
		this.body = body;
	}
	
	public String getMethod(){
		return method;
	}
	
	public Headers getHeaders(){
		return headers;
	}
	
	public String getBody(){
		return body;
	}
}
