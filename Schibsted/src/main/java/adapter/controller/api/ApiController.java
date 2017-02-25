package adapter.controller.api;

import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import core.Helper;
import core.Server;
import core.database.Database;
import core.entity.HttpRequest;
import core.entity.HttpResponse;

import adapter.controller.Controller;
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
				
				Integer refUserId = null;
				
				// Get id of referenced user
				try{
					refUserId = new Integer(request.get("uid"));
				}
				catch(NumberFormatException e){
					return new HttpResponse(HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("Invalid user resource identifier format, integer expected").getJson());
				}
				
				switch(request.getMethod()){
					case "GET":  	return GET(refUserId);
					case "PUT":  	return PUT(authUserId, refUserId, request.getBody());
					case "DELETE":	return DELETE(authUserId, refUserId);
				}
			}
			// Request that refer the entire collection of users
			else{
				switch(request.getMethod()){
					case "GET":  return GET();
					case "POST": return POST(authUserId, request.getBody());
				}
			}
		}
		else{
			return new HttpResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
		}
		
		return new HttpResponse(HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("The requested action is not supported by the specified resource").getJson());
	}
	
	/**
	 * Get users collection
	 * 
	 * @return
	 * @throws Exception 
	 */
	private static HttpResponse GET(){
		
		try {
			UsecaseGetUsers usecase = new UsecaseGetUsers();
			
			if(usecase.execute()){
			    return new HttpResponse(
			    		HttpURLConnection.HTTP_OK, 
			    		new ApiResponseUserCollection(usecase.users, usecase.roles).getJson());
			}
			else{
				return new HttpResponse(
						HttpURLConnection.HTTP_OK, 
						new ApiResponseUserCollection().getJson());
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new HttpResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError(e.getMessage()).getJson());
		}
	}

	/**
	 * Get a single user resource
	 * 
	 * @param refdUserId
	 * @return
	 * @throws Exception
	 */
	private static HttpResponse GET(Integer refdUserId){
	
		try {
			UsecaseGetOneUser usecase = new UsecaseGetOneUser();
			usecase.uid = refdUserId;
			
			if(usecase.execute()){
				return new HttpResponse(
						HttpURLConnection.HTTP_OK, 
						new ApiResponseUserResource(usecase.user, usecase.roles).getJson());
			}
			else{
				return new HttpResponse(HttpURLConnection.HTTP_NOT_FOUND, new ApiResponseError("User with this id does not exist").getJson());
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new HttpResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError(e.getMessage()).getJson());
		}
	}
	
	/**
	 * Create a new user
	 * 
	 * @param body
	 * @return
	 * @throws Exception
	 */
	private static HttpResponse POST(Integer authUserId, String body){
		
		try{
			Gson gson = new Gson();
			User user = gson.fromJson(body, User.class);
			
			if(	!(user.getUsername() != null && user.getUsername().length() > 0) ||
				!(user.getPassword() != null && user.getPassword().length() > 0) ||
				!(user.getRoles() != null && user.getRoles().length > 0)){
				
				return new HttpResponse(HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("Insufficient data supplied, need username, password and at least one role").getJson());
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
					return new HttpResponse(HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("User with this username and password already exists").getJson());
				
				case UsecaseAddNewUser.RESULT_BAD_INPUT_DATA:
				case UsecaseAddNewUser.RESULT_USER_NOT_CREATED:
				default:
					return new HttpResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError("Unknown error").getJson());
			}
		}
		catch(JsonSyntaxException e){
			return new HttpResponse(HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("Json syntax").getJson());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new HttpResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError(e.getMessage()).getJson());
		}
	}
	
	/**
	 * Update an existing user
	 * 
	 * @param body
	 * @return
	 * @throws Exception
	 */
	private static HttpResponse PUT(Integer authUserId, Integer refUserId, String body){
		
		try{
			Gson gson = new Gson();
			User user = gson.fromJson(body, User.class);
			
			if(	!(user.getUsername() != null && user.getUsername().length() > 0) ||
				!(user.getPassword() != null && user.getPassword().length() > 0) ||
				!(user.getRoles() != null && user.getRoles().length > 0)){
				
				return new HttpResponse(HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("Insufficient data supplied, need username, password and at least one role").getJson());
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
					return new HttpResponse(HttpURLConnection.HTTP_NOT_FOUND, new ApiResponseError("User with this id does not exist").getJson());
				
				case UsecaseUpdateExistingUser.RESULT_BAD_INPUT_DATA:
				case UsecaseUpdateExistingUser.RESULT_USER_NOT_CREATED:
				default:
					return new HttpResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError("Unknown error").getJson());
			}
		}
		catch(JsonSyntaxException e){
			return new HttpResponse(HttpURLConnection.HTTP_BAD_REQUEST, new ApiResponseError("Json syntax").getJson());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new HttpResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError(e.getMessage()).getJson());
		}
	}
	
	/**
	 * Get a single user resource
	 * 
	 * @param refdUserId
	 * @return
	 * @throws Exception
	 */
	private static HttpResponse DELETE(Integer authUserId, Integer refUserId){
	
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
					return new HttpResponse(HttpURLConnection.HTTP_NOT_FOUND, new ApiResponseError("User with this id does not exist").getJson());
				
				case UsecaseDeleteOneUser.RESULT_BAD_INPUT_DATA:
				case UsecaseDeleteOneUser.RESULT_USER_NOT_DELETED:
				default:
					return new HttpResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError("Unknown error").getJson());
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new HttpResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, new ApiResponseError(e.getMessage()).getJson());
		}
	}
}
