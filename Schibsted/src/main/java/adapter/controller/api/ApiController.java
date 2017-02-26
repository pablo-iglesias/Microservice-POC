package adapter.controller.api;

import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import core.Helper;
import core.Server;
import core.database.Database;
import core.entity.HttpRequest;
import core.entity.HttpResponse;

import adapter.controller.Controller;
import adapter.response.api.json.ApiResponse;
import adapter.response.api.json.ApiResponseError;
import adapter.response.api.json.ApiResponseUserCollection;
import adapter.response.api.json.ApiResponseUserResource;

import domain.entity.User;
import domain.usecase.api.UsecaseUpdateExistingUser;
import domain.usecase.api.UsecaseAddNewUser;
import domain.usecase.api.UsecaseDeleteOneUser;
import domain.usecase.api.UsecaseGetOneUser;
import domain.usecase.api.UsecaseGetUsers;

public class ApiController extends Controller {
	
	private static final String[] supportedMediaTypes = {"application/json", "application/xml"};
	
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
	 * Creates and returns final HttpResponse object, handles content negotiation
	 * 
	 * @param request
	 * @param httpCode
	 * @param message
	 * @return
	 */
	private static HttpResponse respond(HttpRequest request, int httpCode, ApiResponse response) throws Exception{
		String accept = request.getHeaders().getFirst("Accept");
		
		double highest = 0;
		String type = "";
		
		for(String mediaType : supportedMediaTypes)
		{
			Map<String, String> match = Helper.group(accept, mediaType + "(;q=(?<quality>[0-9.]*))?");
			
			if(match != null){
				double quality = 0;
				if(match.containsKey("quality") && match.get("quality") != null){
					quality = Double.valueOf(match.get("quality"));
				}
				else{
					quality = 1d;
				}
				
				if(quality > highest){
					highest = quality;
					type = mediaType;
				}
				
				if(quality >= 1)
					break;
			}
		}
		
		HttpResponse httpResponse = null;
		
		switch(type){
			case "application/json": 
				httpResponse = new HttpResponse(httpCode, response.getJson());
				break;
			case "application/xml":  
				httpResponse = new HttpResponse(httpCode, response.getXml());
				break;
		}
		
		if(httpResponse != null){
			httpResponse.setHeader("Content-Type", type);
			return httpResponse;
		}
		else{
			return new HttpResponse(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
		}
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
			if(request.contains("uid") && request.get("uid") != null){
				
				Integer refUserId = null;
				
				// Get id of referenced user
				try{
					refUserId = new Integer(request.get("uid"));
				}
				catch(NumberFormatException e){
					return respond(request, HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("Invalid user resource identifier format, integer expected"));
				}
				
				switch(request.getMethod()){
					case "GET":  	return GET(request, refUserId);
					case "PUT":  	return PUT(request, authUserId, refUserId, request.getBody());
					case "DELETE":	return DELETE(request, authUserId, refUserId);
				}
			}
			// Request that refer the entire collection of users
			else{
				switch(request.getMethod()){
					case "GET":  return GET(request);
					case "POST": return POST(request, authUserId, request.getBody());
				}
			}
		}
		else{
			return new HttpResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
		}
		
		return respond(request, HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("The requested action is not supported by the specified resource"));
	}
	
	/**
	 * Get users collection
	 * 
	 * @return
	 * @throws Exception 
	 */
	private static HttpResponse GET(HttpRequest request) throws Exception{
		
		try {
			UsecaseGetUsers usecase = new UsecaseGetUsers();
			
			if(usecase.execute()){
			    return respond(
			    		request,
			    		HttpURLConnection.HTTP_OK, 
			    		new ApiResponseUserCollection(usecase.users, usecase.roles));
			}
			else{
				return respond(
						request,
						HttpURLConnection.HTTP_OK, 
						new ApiResponseUserCollection());
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return respond(request, HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError(e.getMessage()));
		}
	}

	/**
	 * Get a single user resource
	 * 
	 * @param refdUserId
	 * @return
	 * @throws Exception
	 */
	private static HttpResponse GET(HttpRequest request, Integer refdUserId) throws Exception{
	
		try {
			UsecaseGetOneUser usecase = new UsecaseGetOneUser();
			usecase.uid = refdUserId;
			
			if(usecase.execute()){
				return respond(
						request,
						HttpURLConnection.HTTP_OK, 
						new ApiResponseUserResource(usecase.user, usecase.roles));
			}
			else{
				return respond(request, HttpURLConnection.HTTP_NOT_FOUND, new ApiResponseError("User with this id does not exist"));
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return respond(request, HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError(e.getMessage()));
		}
	}
	
	/**
	 * Create a new user
	 * 
	 * @param body
	 * @return
	 * @throws Exception
	 */
	private static HttpResponse POST(HttpRequest request, Integer authUserId, String body) throws Exception{
		
		try{
			Gson gson = new Gson();
			User user = gson.fromJson(body, User.class);
			
			if(	!(user.getUsername() != null && user.getUsername().length() > 0) ||
				!(user.getPassword() != null && user.getPassword().length() > 0) ||
				!(user.getRoles() != null && user.getRoles().length > 0)){
				
				return respond(request, HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("Insufficient data supplied, need username, password and at least one role"));
			}
			
			Database db = Server.getDatabase();
			db.startTransaction();
			
			UsecaseAddNewUser usecase = new UsecaseAddNewUser();
			usecase.uid = authUserId;
			usecase.user = user;
				
			int result = usecase.execute();
			
			if(result == UsecaseAddNewUser.RESULT_USER_CREATED_SUCCESSFULLY){
				db.commit();
			}
			else{
				db.rollback();
			}
			
			switch(result){
				case UsecaseAddNewUser.RESULT_USER_CREATED_SUCCESSFULLY:
					return new HttpResponse(HttpURLConnection.HTTP_NO_CONTENT);
					
				case UsecaseAddNewUser.RESULT_NOT_AUTHORISED:
					return new HttpResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
					
				case UsecaseAddNewUser.RESULT_USER_ALREADY_EXISTS:
					return respond(request, HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("User with this username and password already exists"));
				
				case UsecaseAddNewUser.RESULT_BAD_INPUT_DATA:
				case UsecaseAddNewUser.RESULT_USER_NOT_CREATED:
				default:
					return respond(request, HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError("Unknown error"));
			}
		}
		catch(JsonSyntaxException e){
			return respond(request, HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("Json syntax"));
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return respond(request, HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError(e.getMessage()));
		}
	}
	
	/**
	 * Update an existing user
	 * 
	 * @param body
	 * @return
	 * @throws Exception
	 */
	private static HttpResponse PUT(HttpRequest request, Integer authUserId, Integer refUserId, String body) throws Exception{
		
		try{
			Gson gson = new Gson();
			User user = gson.fromJson(body, User.class);
			
			if(	!(user.getUsername() != null && user.getUsername().length() > 0) ||
				!(user.getPassword() != null && user.getPassword().length() > 0) ||
				!(user.getRoles() != null && user.getRoles().length > 0)){
				
				return respond(request, HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("Insufficient data supplied, need username, password and at least one role"));
			}
			
			Database db = Server.getDatabase();
			db.startTransaction();
			
			UsecaseUpdateExistingUser usecase = new UsecaseUpdateExistingUser();
			usecase.authUserId = authUserId;
			usecase.refUserId = refUserId;
			usecase.userData = user;
				
			int result = usecase.execute();
			
			if(result == UsecaseUpdateExistingUser.RESULT_USER_UPDATED_SUCCESSFULLY){
				db.commit();
			}
			else{
				db.rollback();
			}
			
			switch(result){
				case UsecaseUpdateExistingUser.RESULT_USER_UPDATED_SUCCESSFULLY:
					return new HttpResponse(HttpURLConnection.HTTP_NO_CONTENT);
					
				case UsecaseUpdateExistingUser.RESULT_NOT_AUTHORISED:
					return new HttpResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
					
				case UsecaseUpdateExistingUser.RESULT_USER_DOES_NOT_EXIST:
					return respond(request, HttpURLConnection.HTTP_NOT_FOUND, new ApiResponseError("User with this id does not exist"));
				
				case UsecaseUpdateExistingUser.RESULT_BAD_INPUT_DATA:
				case UsecaseUpdateExistingUser.RESULT_USER_NOT_CREATED:
				default:
					return respond(request, HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError("Unknown error"));
			}
		}
		catch(JsonSyntaxException e){
			return respond(request, HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("Json syntax"));
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return respond(request, HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError(e.getMessage()));
		}
	}
	
	/**
	 * Get a single user resource
	 * 
	 * @param refdUserId
	 * @return
	 * @throws Exception
	 */
	private static HttpResponse DELETE(HttpRequest request, Integer authUserId, Integer refUserId) throws Exception{
	
		try {
			Database db = Server.getDatabase();
			db.startTransaction();
			
			UsecaseDeleteOneUser usecase = new UsecaseDeleteOneUser();
			usecase.authUserId = authUserId;
			usecase.refUserId = refUserId;
			
			int result = usecase.execute();
			
			if(result == UsecaseDeleteOneUser.RESULT_USER_DELETED_SUCCESSFULLY){
				db.commit();
			}
			else{
				db.rollback();
			}
			
			switch(result){
				case UsecaseDeleteOneUser.RESULT_USER_DELETED_SUCCESSFULLY:
					return new HttpResponse(HttpURLConnection.HTTP_NO_CONTENT);
				
				case UsecaseDeleteOneUser.RESULT_NOT_AUTHORISED:
					return new HttpResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
					
				case UsecaseDeleteOneUser.RESULT_USER_DOES_NOT_EXIST:
					return respond(request, HttpURLConnection.HTTP_NOT_FOUND, new ApiResponseError("User with this id does not exist"));
				
				case UsecaseDeleteOneUser.RESULT_BAD_INPUT_DATA:
				case UsecaseDeleteOneUser.RESULT_USER_NOT_DELETED:
				default:
					return respond(request, HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError("Unknown error"));
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return respond(request, HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError(e.getMessage()));
		}
	}
}
