package core.factory;

import com.sun.net.httpserver.HttpExchange;

import core.Helper;
import core.entity.Cookie;

public class CookieFactory {

	public Cookie create(HttpExchange exchange){
		if(exchange.getRequestHeaders().containsKey("Cookie")){
			return new Cookie(Helper.parseCookie(exchange.getRequestHeaders().getFirst("Cookie")));
		}
		else{
			return null;
		}
	}
}
