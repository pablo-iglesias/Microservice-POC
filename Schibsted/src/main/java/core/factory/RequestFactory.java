package core.factory;

import com.sun.net.httpserver.HttpExchange;

import core.Helper;
import core.ResourceLoader;
import core.entity.HttpRequest;

public class RequestFactory extends ResourceLoader {

	public HttpRequest create(HttpExchange exchange){
		String body = Helper.convertInputStreamToString(exchange.getRequestBody());	
		return new HttpRequest(exchange.getRequestMethod(), exchange.getRequestHeaders(), body);
	}
}
