package core.factory;

import com.sun.net.httpserver.HttpExchange;

import core.Helper;
import core.ResourceLoader;
import core.entity.HttpRequest;

public class RequestFactory extends ResourceLoader {

	public HttpRequest create(HttpExchange exchange){
		String body = Helper.convertInputStreamToString(exchange.getRequestBody());
		String requestedURL = "http://" + exchange.getRequestHeaders().getFirst("Host") + exchange.getRequestURI();
		return new HttpRequest(
				requestedURL, 
				exchange.getRequestMethod(), 
				exchange.getRequestHeaders(), 
				body);
	}
}
