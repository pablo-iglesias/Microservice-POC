package core.entity;

import com.sun.net.httpserver.Headers;

public class HttpRequest extends DataContainer{
	
	private String url;
	private String method;
	private Headers headers;
	private String body;
	
	public HttpRequest(String url, String method, Headers headers, String body){
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.body = body;
	}
	
	public String getURL(){
		return url;
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
