package adapter.controller.api;

import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.Map;

import com.google.gson.Gson;

import core.Helper;
import core.entity.HttpRequest;
import core.entity.HttpResponse;

import adapter.controller.Controller;

import domain.usecase.api.UsecaseGetOneUser;
import domain.usecase.api.UsecaseGetUsers;

public class ApiController extends Controller {
	
	/**
	 * Perform Basic Authentication check
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private static Integer authenticate(HttpRequest request) throws Exception{
		
		String auth = request.getHeaders().getFirst("Authorization");
		
		if(auth != null){
			auth = Helper.match(auth, "Basic (.+)");
			
			if(auth != null){
				auth = new String(Base64.getDecoder().decode(auth));
				Map<String, String> credentials = Helper.group(auth, "(?<username>[^:]+):(?<password>.+)");
				
				if(!credentials.isEmpty()){
					Integer uid = authenticate(credentials.get("username"), credentials.get("password"));
					
					if(uid != null){
						return uid;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Handler for Api requests, diverts requests to appropriate method of the controller
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse handler(HttpRequest request) throws Exception{
		
		// Get id of authenticated user
		Integer authUserId = authenticate(request); 
		
		if(authUserId != null){
			
			// Requests that refer an specific user id in the URI
			if(request.contains("uid")){
				
				// Get id of referenced user
				Integer refUserId = new Integer(request.get("uid"));
				
				switch(request.getMethod()){
					case "GET": return GET(refUserId.intValue());
						
				}
			}
			// Request that refer the entire collection of users
			else{
				switch(request.getMethod()){
					case "GET": return GET();
				}
			}
		}
		
		return new HttpResponse(HttpURLConnection.HTTP_UNAUTHORIZED, "");
	}
	
	/**
	 * Get users collection
	 * 
	 * @param authUserId
	 * @return
	 * @throws Exception 
	 */
	private static HttpResponse GET() throws Exception{
		
		UsecaseGetUsers usecase = new UsecaseGetUsers();
		
		if(usecase.execute()){
		    return new HttpResponse(
		    		HttpURLConnection.HTTP_OK, 
		    		new Gson().toJson(usecase.users) 
		    		);
		}
		else{
			return new HttpResponse(HttpURLConnection.HTTP_OK, "[]");
		}
	}

	/**
	 * Get a single user resource
	 * 
	 * @param refdUserId
	 * @return
	 * @throws Exception
	 */
	private static HttpResponse GET(int refdUserId) throws Exception{
	
		UsecaseGetOneUser usecase = new UsecaseGetOneUser();
		usecase.uid = refdUserId;
		
		if(usecase.execute()){
			return new HttpResponse(
					HttpURLConnection.HTTP_OK, 
					new Gson().toJson(usecase.user)
					);
		}
		else{
			return new HttpResponse(HttpURLConnection.HTTP_OK, "{}");
		}
	}
}
