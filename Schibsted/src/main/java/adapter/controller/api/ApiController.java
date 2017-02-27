package adapter.controller.api;

import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import core.Helper;
import core.Server;
import core.database.Database;
import core.entity.HttpRequest;
import core.entity.HttpResponse;

import adapter.controller.Controller;
import adapter.model.RoleModel;
import adapter.model.UserModel;
import adapter.model.factory.ModelFactory;
import adapter.response.api.ApiResponse;
import adapter.response.api.ApiResponseError;
import adapter.response.api.ApiResponseUserCollection;
import adapter.response.api.ApiResponseUserResource;
import domain.entity.User;
import domain.usecase.api.UsecaseUpdateExistingUser;
import domain.usecase.api.UsecaseAddNewUser;
import domain.usecase.api.UsecaseDeleteOneUser;
import domain.usecase.api.UsecaseGetOneUser;
import domain.usecase.api.UsecaseGetUsers;

public class ApiController extends Controller {
	
	private static final String[] SUPPORTED_MEDIA_TYPES = {"*/*", "application/*", "application/json", "application/xml"};
	
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
	 * Creates and returns final HttpResponse object, used only for those responses that deliver actual content, handles content negotiation
	 * 
	 * @param request
	 * @param httpCode
	 * @param message
	 * @return
	 */
	private static HttpResponse respond(HttpRequest request, int httpCode, ApiResponse response) throws Exception {
		
		String accept = request.getHeaders().getFirst("Accept");
		
		if(accept == null){
			accept = SUPPORTED_MEDIA_TYPES[0];
		}
		
		double highest = 0;
		String type = "";
		
		for(String mediaType : SUPPORTED_MEDIA_TYPES)
		{
			Map<String, String> match = Helper.group(accept, Pattern.quote(mediaType) + "(;q=(?<quality>[0-9.]*))?");
			
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
		
		HttpResponse httpResponse;
		
		switch(type){
			case "*/*":
			case "application/*":
			case "application/json":
				httpResponse = new HttpResponse(httpCode, response.getJson());
				httpResponse.setHeader("Content-Type", "application/json");
				return httpResponse;
			case "application/xml":
				httpResponse = new HttpResponse(httpCode, response.getXml());
				httpResponse.setHeader("Content-Type", "application/xml");
				return httpResponse;
		}
		
		return new HttpResponse(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
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
					return respond(
							request, 
							HttpURLConnection.HTTP_BAD_REQUEST, 
							new ApiResponseError("Invalid user resource identifier format, integer expected"));
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
		
		return respond(
				request, 
				HttpURLConnection.HTTP_BAD_REQUEST, 
				new ApiResponseError("The requested action is not supported by the specified resource"));
	}
	
	/**
	 * Get users collection
	 * 
	 * @return
	 * @throws Exception 
	 */
	private static HttpResponse GET(HttpRequest request) throws Exception{
		
		try {
			ModelFactory factory = new ModelFactory();
			UserModel userModel = (UserModel) factory.create("User");
			RoleModel roleModel = (RoleModel) factory.create("Role");
			
			UsecaseGetUsers usecase = new UsecaseGetUsers(userModel, roleModel);
			
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
			return respond(
					request, 
					HttpURLConnection.HTTP_INTERNAL_ERROR, 
					new ApiResponseError(e.getMessage()));
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
			ModelFactory factory = new ModelFactory();
			UserModel userModel = (UserModel) factory.create("User");
			RoleModel roleModel = (RoleModel) factory.create("Role");
			
			UsecaseGetOneUser usecase = new UsecaseGetOneUser(userModel, roleModel);
			usecase.uid = refdUserId;
			
			if(usecase.execute()){
				return respond(
						request,
						HttpURLConnection.HTTP_OK, 
						new ApiResponseUserResource(usecase.user, usecase.roles));
			}
			else{
				return respond(
						request, 
						HttpURLConnection.HTTP_NOT_FOUND, 
						new ApiResponseError("User with this id does not exist"));
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return respond(
					request, 
					HttpURLConnection.HTTP_INTERNAL_ERROR, 
					new ApiResponseError(e.getMessage()));
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
				
				return respond(
						request, 
						HttpURLConnection.HTTP_BAD_REQUEST, 
						new ApiResponseError("Insufficient data supplied, need username, password and at least one role"));
			}
			
			Database db = Server.getDatabase();
			db.startTransaction();
			
			ModelFactory factory = new ModelFactory();
			UserModel userModel = (UserModel) factory.create("User");
			RoleModel roleModel = (RoleModel) factory.create("Role");
			
			UsecaseAddNewUser usecase = new UsecaseAddNewUser(userModel, roleModel);
			usecase.uid = authUserId;
			usecase.user = user;
				
			int result = usecase.execute();
			
			if(result == UsecaseAddNewUser.RESULT_USER_NOT_CREATED){
				db.rollback();
			}
			else{
				db.commit();
			}
			
			switch(result){
				case UsecaseAddNewUser.RESULT_USER_CREATED_SUCCESSFULLY:
					return new HttpResponse(HttpURLConnection.HTTP_NO_CONTENT);
					
				case UsecaseAddNewUser.RESULT_NOT_AUTHORISED:
					return new HttpResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
					
				case UsecaseAddNewUser.RESULT_USER_ALREADY_EXISTS:
					return respond(
							request, 
							HttpURLConnection.HTTP_BAD_REQUEST, 
							new ApiResponseError("User with this username already exists"));
				
				case UsecaseAddNewUser.RESULT_BAD_INPUT_DATA:
				case UsecaseAddNewUser.RESULT_USER_NOT_CREATED:
				default:
					return respond(
							request, 
							HttpURLConnection.HTTP_INTERNAL_ERROR, 
							new ApiResponseError("Unknown error"));
			}
		}
		catch(JsonSyntaxException e){
			return respond(
					request, 
					HttpURLConnection.HTTP_BAD_REQUEST, 
					new ApiResponseError("Json syntax"));
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return respond(
					request, 
					HttpURLConnection.HTTP_INTERNAL_ERROR, 
					new ApiResponseError(e.getMessage()));
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
				
				return respond(
						request, 
						HttpURLConnection.HTTP_BAD_REQUEST, 
						new ApiResponseError("Insufficient data supplied, need username, password and at least one role"));
			}
			
			Database db = Server.getDatabase();
			db.startTransaction();
			
			ModelFactory factory = new ModelFactory();
			UserModel userModel = (UserModel) factory.create("User");
			RoleModel roleModel = (RoleModel) factory.create("Role");
			
			UsecaseUpdateExistingUser usecase = new UsecaseUpdateExistingUser(userModel, roleModel);
			usecase.authUserId = authUserId;
			usecase.refUserId = refUserId;
			usecase.userData = user;
				
			int result = usecase.execute();
			
			if(result == UsecaseUpdateExistingUser.RESULT_USER_NOT_UPDATED){
				db.rollback();
			}
			else{
				db.commit();
			}
			
			switch(result){
				case UsecaseUpdateExistingUser.RESULT_USER_UPDATED_SUCCESSFULLY:
					return new HttpResponse(HttpURLConnection.HTTP_NO_CONTENT);
					
				case UsecaseUpdateExistingUser.RESULT_NOT_AUTHORISED:
					return new HttpResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
					
				case UsecaseUpdateExistingUser.RESULT_USER_DOES_NOT_EXIST:
					return respond(
							request, 
							HttpURLConnection.HTTP_NOT_FOUND, 
							new ApiResponseError("User with this id does not exist"));
					
				case UsecaseUpdateExistingUser.RESULT_USERNAME_ALREADY_TAKEN:
					return respond(
							request, 
							HttpURLConnection.HTTP_CONFLICT, 
							new ApiResponseError("The specified username is already in use"));
				
				case UsecaseUpdateExistingUser.RESULT_BAD_INPUT_DATA:
				case UsecaseUpdateExistingUser.RESULT_USER_NOT_UPDATED:
				default:
					return respond(
							request, 
							HttpURLConnection.HTTP_INTERNAL_ERROR, 
							new ApiResponseError("Unknown error"));
			}
		}
		catch(JsonSyntaxException e){
			return respond(
					request, 
					HttpURLConnection.HTTP_BAD_REQUEST, 
					new ApiResponseError("Json syntax"));
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return respond(
					request, 
					HttpURLConnection.HTTP_INTERNAL_ERROR, 
					new ApiResponseError(e.getMessage()));
		}
	}
	
	/**
	 * Remove a user resource
	 * 
	 * @param refdUserId
	 * @return
	 * @throws Exception
	 */
	private static HttpResponse DELETE(HttpRequest request, Integer authUserId, Integer refUserId) throws Exception{
	
		try {
			Database db = Server.getDatabase();
			db.startTransaction();
			
			ModelFactory factory = new ModelFactory();
			UserModel userModel = (UserModel) factory.create("User");
			RoleModel roleModel = (RoleModel) factory.create("Role");
			
			UsecaseDeleteOneUser usecase = new UsecaseDeleteOneUser(userModel, roleModel);
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
					return respond(
							request, 
							HttpURLConnection.HTTP_NOT_FOUND, 
							new ApiResponseError("User with this id does not exist"));
				
				case UsecaseDeleteOneUser.RESULT_BAD_INPUT_DATA:
				case UsecaseDeleteOneUser.RESULT_USER_NOT_DELETED:
				default:
					return respond(
							request, 
							HttpURLConnection.HTTP_INTERNAL_ERROR, 
							new ApiResponseError("Unknown error"));
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return respond(
					request, 
					HttpURLConnection.HTTP_INTERNAL_ERROR, new 
					ApiResponseError(e.getMessage()));
		}
	}
}
