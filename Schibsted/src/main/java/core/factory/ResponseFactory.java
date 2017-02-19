package core.factory;

import core.entity.HttpResponse;

public class ResponseFactory {

	public HttpResponse create(int code, String body){
		
		 return new HttpResponse(code, body);
	}
}
