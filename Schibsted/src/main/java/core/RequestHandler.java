package core;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.Date;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import adapter.controller.ApplicationController;
import adapter.response.ApplicationResponse;

import core.entity.Cookie;
import core.entity.HttpRequest;
import core.entity.HttpResponse;
import core.entity.Session;
import core.exception.SessionLossException;
import core.factory.CookieFactory;
import core.factory.RequestFactory;
import core.factory.ResponseFactory;

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
		{"/", 			"Application",	"index"},
		{"/login",		"Application",	"login"},
		{"/logout",		"Application",	"logout"},
		{"/welcome",	"Application",	"welcome"},
		{"/page1", 		"Application",	"page1"},
		{"/page2", 		"Application",	"page2"},
		{"/page3", 		"Application",	"page3"}
	};
	
	/**
	 * Main request handler method
	 */
	public void handle(HttpExchange exchange) {
       
		try{
			try{
				
				String path = exchange.getRequestURI().getPath();
				
				for(String[] route : routes)
				{
					if(path.matches(route[PATH]))
					{
						String controllerName = route[CONTROLLER];
						String methodName = route[METHOD];
						
						switch(controllerName)
						{
							case "Application":
								
								// Parse HTTP request
								HttpRequest request = createHttpRequest(exchange);
								Cookie cookie = retrieveHttpCookie(exchange);
								Session session = retrieveHttpSession(cookie);
								
								// Session loss check
								if(session != null && session.isExpired()){
									Server.removeSession(session.getSessionToken());
									throw new SessionLossException();
								}
									
								// Run application through the appropriate controller and method
								Class<?> controller = ApplicationController.class;
								Method method = controller.getMethod(methodName, new Class[]{HttpRequest.class, Session.class});
								ApplicationResponse appResponse = (ApplicationResponse) method.invoke(null, request, session);
								
								// Send HTTM response
								propagateSession(exchange, appResponse, session);
								HttpResponse response = createHttpResponse(exchange, appResponse, session);
								dispatchHttpResponse(exchange, response);
								
							return;
							default: 
								throw new Exception("Unknown controller " + controllerName);
						}
					}
				}
				respondResourceNotFound(exchange);
			}
			catch(SessionLossException e){
				respondSessionLost(exchange);
				return;
			}
			catch(Exception e){
				e.printStackTrace(System.out);
				respondInternalServerError(exchange);
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
			String sessionToken = cookie.getData("sessionToken");
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
	 * @return
	 */
	private HttpRequest createHttpRequest(HttpExchange exchange){
		RequestFactory factory = new RequestFactory();
		HttpRequest request = factory.create(exchange);
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
	 * @return
	 */
	private HttpResponse createHttpResponse(HttpExchange exchange, ApplicationResponse appResponse, Session session){
		
		ResponseFactory factory = new ResponseFactory();
		
		// If the application specified a view and context data, use the template parser to generate response body
		String body = "";
		if(appResponse.getView() != null){
			body = Server.getTemplateParser().parseTemplate(appResponse.getView(), appResponse.getData());
		}	
		
		switch(appResponse.getResponseCode()){
			
			case ApplicationResponse.RESPONSE_OK:
				return factory.create(HttpURLConnection.HTTP_OK, body);
				
			case ApplicationResponse.RESPONSE_REDIRECT:
				exchange.getResponseHeaders().set("Location", appResponse.getLocation());
				return factory.create(HttpURLConnection.HTTP_SEE_OTHER, body);
				
			case ApplicationResponse.RESPONSE_ILEGAL:
			default:
				exchange.getResponseHeaders().set("Location", "/");
				return factory.create(HttpURLConnection.HTTP_SEE_OTHER, body);
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
		
		exchange.sendResponseHeaders(response.getCode(), response.getBody().length());
		
		OutputStream stream = exchange.getResponseBody();
		stream.write(response.getBody().getBytes());
		stream.close();
	}
	
	/**
	 * Send HTTP 404 to the client
	 * 
	 * @param exchange
	 * @throws IOException
	 */
	private void respondResourceNotFound(HttpExchange exchange) throws IOException{
		ResponseFactory factory = new ResponseFactory();
		HttpResponse response = factory.create(HttpURLConnection.HTTP_NOT_FOUND, "<h1>404 Not Found</h1>");
		dispatchHttpResponse(exchange, response);
	}
	
	/**
	 * Send HTTP 500 to the client
	 * 
	 * @param exchange
	 * @throws IOException
	 */
	private void respondInternalServerError(HttpExchange exchange) throws IOException{
		ResponseFactory factory = new ResponseFactory();
		HttpResponse response = factory.create(HttpURLConnection.HTTP_INTERNAL_ERROR, "<h1>500 Internal</h1>");
		dispatchHttpResponse(exchange, response);
	}
	
	/**
	 * Send HTTP 303 and redirect to index
	 * 
	 * @param exchange
	 * @throws IOException
	 */
	private void respondSessionLost(HttpExchange exchange) throws IOException{
		exchange.getResponseHeaders().set("Location", "/");
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_SEE_OTHER, -1);
	}
}
