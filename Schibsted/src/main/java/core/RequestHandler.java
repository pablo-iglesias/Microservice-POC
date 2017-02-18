package core;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import adapter.controller.ApplicationController;
import adapter.response.ApplicationResponse;

public class RequestHandler implements HttpHandler {
	
	private static final int PATH = 0;
	private static final int CONTROLLER = 1;
	private static final int METHOD = 2;
	
	// Path, controller, method
	private static final String[][] routes = {
		{"/", 		"Application",	"index"},
		{"/page1", 	"Application",	"page1"},
		{"/page2", 	"Application",	"page2"},
		{"/page3", 	"Application",	"page3"}
	};
	
	public void handle(HttpExchange exchange) throws IOException {
       
		try{
			String path = exchange.getRequestURI().getPath();
			for(String[] route : routes){
				if(path.matches(route[PATH])){
					switch(route[CONTROLLER]){
						case "Application": 
							Class<?> controller = ApplicationController.class;
							Method method = controller.getMethod(route[METHOD]);
							ApplicationResponse response = (ApplicationResponse) method.invoke(null);
							String body = Server.getTemplateParser().parseTemplate(response.getView(), response.getData());
							exchange.sendResponseHeaders(response.getStatusCode(), body.length());
							OutputStream stream = exchange.getResponseBody();
							stream.write(body.getBytes());
							stream.close();
						return;
						default: throw new Exception("Unknown controller " + route[CONTROLLER]);
					}
				}
			}
			exchange.sendResponseHeaders(404, -1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			exchange.sendResponseHeaders(500, -1);
		}
    }
}
