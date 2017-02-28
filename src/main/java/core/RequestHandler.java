package core;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import adapter.controller.api.ApiController;
import adapter.controller.application.ApplicationController;
import adapter.response.application.ApplicationResponse;

import core.entity.Cookie;
import core.entity.HttpRequest;
import core.entity.HttpResponse;
import core.entity.Session;
import core.factory.CookieFactory;
import core.factory.RequestFactory;

/**
 * Request handler
 * 
 * @author Peibol
 *
 */
public class RequestHandler implements HttpHandler {
	
	private static final int PATH = 0;
	private static final int CONTROLLER = 1;
	private static final int METHOD = 2;
	
	// Path, controller, method
	private static final String[][] routes = {
		{"/", 							"Application",	"index"},
		{"/login",						"Application",	"login"},
		{"/logout",						"Application",	"logout"},
		{"/welcome",					"Application",	"welcome"},
		{"/page_(?<page>[0-9]+)$",		"Application",	"page"},
		{"/api/users/?(?<uid>.*)$",		"Api",			"handler"}
	};
	
	/**
	 * Main request handler method
	 */
	public void handle(HttpExchange exchange) {
       
		String controllerName = null;
		
		try{
			try{
				
				String path = exchange.getRequestURI().getPath();
												
				for(String[] route : routes)
				{
					if(path.matches(route[PATH]))
					{
						controllerName = route[CONTROLLER];
						String methodName = route[METHOD];
						Class<?> controller;
						Method method;
						
						// Parse URI and HTTP request
						Map<String,String> uriSegments = Helper.group(path, route[PATH]);
						HttpRequest request = createHttpRequest(exchange, uriSegments);
						
						switch(controllerName)
						{
							case "Application":
								
								// Parse Cookie and identify Session
								Cookie cookie = retrieveHttpCookie(exchange);
								Session session = retrieveHttpSession(cookie);
								
								// Session loss check
								if(session != null && session.isExpired()){
									Server.removeSession(session.getSessionToken());
								}
								
								// Identify appropriate controller and method
								controller = ApplicationController.class;
								method = controller.getMethod(methodName, new Class[]{HttpRequest.class, Session.class});
						
								// Run controller
								ApplicationResponse appResponse = (ApplicationResponse) method.invoke(null, request, session);
								
								// Send HTTP response
								propagateSession(exchange, appResponse, session);
								dispatchHttpResponse(exchange, createHttpResponse(exchange, appResponse, session));
								
							return;
							
							case "Api":					
								
								// Identify appropriate controller and method
								controller = ApiController.class;
								method = controller.getMethod(methodName, new Class[]{HttpRequest.class});
								
								HttpResponse apiResponse = (HttpResponse) method.invoke(null, request);
								
								// Run controller and send HTTP response
								dispatchHttpResponse(exchange, apiResponse);
								
							return;
							default: 
								throw new Exception("Unknown controller " + controllerName);
						}
					}
				}
				respondResourceNotFound(exchange);
			}
			catch(Exception e){
				e.printStackTrace(System.out);
				switch(controllerName){
					case "Application":
						respondInternalServerError(exchange, true);
					break;
					case "Api":
					default:
						respondInternalServerError(exchange, false);
				}
				return;
			}
		}
		catch(IOException e){
			e.printStackTrace(System.out);
			return;
		}
    }
	
	/**
	 * Retrieve session data using session token if found in the cookie  
	 * 
	 * @param exchange
	 * @return
	 */
	private Session retrieveHttpSession(Cookie cookie){
		if(cookie != null && cookie.contains("sessionToken")){
			String sessionToken = cookie.get("sessionToken");
			return Server.getSession(sessionToken);
		}
		return null;
	}
	
	/**
	 * Retrieve cookie from the request
	 * 
	 * @param exchange
	 * @return
	 */
	private Cookie retrieveHttpCookie(HttpExchange exchange){
		CookieFactory factory = new CookieFactory();
		Cookie cookie = factory.create(exchange);
		return cookie;
	}
	
	/**
	 * Create an HTTP request object with data taken from the request and properly formatted
	 * 
	 * @param exchange
	 * @param uriSegments
	 * @return
	 */
	private HttpRequest createHttpRequest(HttpExchange exchange, Map<String,String> uriSegments){
		RequestFactory factory = new RequestFactory();
		HttpRequest request = factory.create(exchange);
		request.set((HashMap<String,String>) uriSegments);
		request.set("query", exchange.getRequestURI().getQuery());
		return request;
	}
	
	/**
	 * Add extra time to session and renew the cookie through the appropriate HTTP headers
	 * 
	 * @param exchange
	 * @param appResponse
	 * @param session
	 * @return
	 */
	private void propagateSession(HttpExchange exchange, ApplicationResponse appResponse, Session session){
		
		// The Application created a new session
		if(appResponse.getSession() != null){
			session = appResponse.getSession();
		}
		
		if(session != null){
			session.touch();
			Date sessionExpiration = session.getExpiryTime();
			
			// Add session cookie to the response
			String sessionCookie = "sessionToken=" + session.getSessionToken() + ";";
			sessionCookie += " Expires=" + Helper.getGMTDateNotation(sessionExpiration) + ";";
			exchange.getResponseHeaders().set("Set-Cookie", sessionCookie);
		}
		else{
			// Add session cookie to the response in order to remove session cookie
			String sessionCookie = "sessionToken=;";
			sessionCookie += " Expires=" + Helper.getGMTDateNotation(new Date()) + ";";
			exchange.getResponseHeaders().set("Set-Cookie", sessionCookie);
		}
	}
	
	/**
	 * Create an HTTP response object using the output from the application
	 * 
	 * @param exchange
	 * @param appResponse
	 * @param session
	 * @return
	 * @throws Exception
	 */
	private HttpResponse createHttpResponse(HttpExchange exchange, ApplicationResponse appResponse, Session session) throws Exception{
				
		// If the application specified a view and context data, use the template parser to generate response body
		String body = "";
		if(appResponse.getView() != null){
						
			// Render view
			body = Server.getTemplateParser().parseTemplate(appResponse.getView(), appResponse.getData());
		}	
		
		switch(appResponse.getResponseCode()){
			
			case ApplicationResponse.RESPONSE_OK:
				return new HttpResponse(HttpURLConnection.HTTP_OK, body);
				
			case ApplicationResponse.RESPONSE_DENIED:
				return new HttpResponse(HttpURLConnection.HTTP_FORBIDDEN, "<h1>403 Forbidden (~_^)</h1>");
				
			case ApplicationResponse.RESPONSE_REDIRECT:
			default:
				exchange.getResponseHeaders().set("Location", appResponse.getLocation());
				return new HttpResponse(HttpURLConnection.HTTP_SEE_OTHER, body);
		}
	}
	
	/**
	 * Send the HTTP response to the client
	 * 
	 * @param exchange
	 * @param response
	 * @throws IOException
	 */
	private void dispatchHttpResponse(HttpExchange exchange, HttpResponse response) throws IOException{
		
		Map<String, String> headers = response.getHeaders();
		
		if(headers.size() > 0){
			Iterator<Entry<String, String>> it = headers.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
		        exchange.getResponseHeaders().set(pair.getKey(), pair.getValue());
		        it.remove();
		    }
		}
		
		OutputStream stream = exchange.getResponseBody();
		int httpCode = response.getCode();
		if(httpCode == HttpURLConnection.HTTP_NO_CONTENT){
			exchange.sendResponseHeaders(httpCode, -1);
		}
		else{
			exchange.sendResponseHeaders(httpCode, response.getBody().getBytes().length);
			stream.write(response.getBody().getBytes());
			stream.close();
		}
	}
	
	/**
	 * Send HTTP 404 to the client
	 * 
	 * @param exchange
	 * @throws IOException
	 */
	private void respondResourceNotFound(HttpExchange exchange) throws IOException{
		HttpResponse response = new HttpResponse(HttpURLConnection.HTTP_NOT_FOUND, "<h1>404 Not Found (¬_¬)</h1>");
		dispatchHttpResponse(exchange, response);
	}
	
	/**
	 * Send HTTP 500 to the client
	 * 
	 * @param exchange
	 * @throws IOException
	 */
	private void respondInternalServerError(HttpExchange exchange, boolean html) throws IOException{
		HttpResponse response = new HttpResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, html?"<h1>500 Internal (ò_ó)</h1>":"");
		dispatchHttpResponse(exchange, response);
	}
}
