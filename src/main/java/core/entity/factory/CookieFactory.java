package core.entity.factory;

import com.sun.net.httpserver.HttpExchange;

import core.Helper;
import core.entity.Cookie;

public class CookieFactory {

    /**
     * Create a cookie object with the information in the request headers, if available
     * 
     * @param exchange
     * @return
     */
    public static Cookie create(HttpExchange exchange) {
        if (exchange.getRequestHeaders().containsKey("Cookie")) {
            String cookie = exchange.getRequestHeaders().getFirst("Cookie");
            return new Cookie(Helper.map(cookie, ";*([^=]*)=([^;]*)"));
        } else {
            return null;
        }
    }
}
