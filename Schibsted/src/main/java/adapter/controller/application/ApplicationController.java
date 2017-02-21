package adapter.controller.application;

import java.util.Map;
import java.util.HashMap;

import core.Helper;
import core.Server;
import core.entity.HttpRequest;
import core.entity.Session;
import domain.usecase.UsecaseAuthenticateUser;
import domain.usecase.application.UsecasePage;
import domain.usecase.application.UsecaseWelcome;
import adapter.controller.Controller;
import adapter.response.application.*;

public class ApplicationController extends Controller{
	
	/**
	 * Default page, displays login form, if already has a session, redirects to welcome page
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	public static ApplicationResponse index(HttpRequest request, Session session){
		if(session != null){
			return new ApplicationResponseREDIRECT("/welcome", session);
		}
		
		return new ApplicationResponseOK("templates/login.html", null);
	}
	
	/**
	 * Login endpoint, receives user data by POST
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static ApplicationResponse login(HttpRequest request, Session session) throws Exception{
		
		if(request.getMethod().matches("POST")){
			
			// Parse payload of the post for parameters
			Map<String, String> params = Helper.map(request.getBody(), "&*([^=]+)=([^&]+)");
	        
			if(params.containsKey("username") && params.containsKey("password")){
				Integer uid = authenticate(params.get("username"), params.get("password"));
				
				if(uid != null){
					session = Server.createSession(uid.intValue());
					
					if(params.containsKey("page")){
						UsecasePage usecasePage = new UsecasePage();
						usecasePage.uid = session.getUserId();
						usecasePage.page = params.get("page");
						
						if(usecasePage.execute() && usecasePage.allowed){
							return new ApplicationResponseREDIRECT("/page_" + params.get("page"), session);
						}
					}
					
					return new ApplicationResponseREDIRECT("/welcome", session);
				}
			}
		}
		
		return new ApplicationResponseILEGAL();
	}
	
	/**
	 * Logs the user out, sends it to the index page
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static ApplicationResponse logout(HttpRequest request, Session session) throws Exception{
		
		if(session != null){
			Server.removeSession(session.getSessionToken());			
			return new ApplicationResponseREDIRECT("/");
		}
		
		return new ApplicationResponseILEGAL();
	}
	
	/**
	 * Welcome page, displays user name and logout button
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static ApplicationResponse welcome(HttpRequest request, Session session) throws Exception{
		
		if(session != null){
			
			UsecaseWelcome usecase = new UsecaseWelcome();
			usecase.uid = session.getUserId();
			
			if(usecase.execute()){
				
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("user_name", usecase.username);
				
				if(usecase.roles != null){
					Map<String, String> roles = new HashMap<String, String>();
					for(String[] role : usecase.roles){
						roles.put(role[0], role[1]);
					}
					
					data.put("roles", roles);
				}
				
				return new ApplicationResponseOK("templates/welcome.html", data);
			}
		}
		
		return new ApplicationResponseILEGAL();
	}
	
	/**
	 * Standard page, displays user name and logout button
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	public static ApplicationResponse page(HttpRequest request, Session session) throws Exception{
		
		if(session != null && request.contains("page")){
			
			UsecasePage usecase = new UsecasePage();
			usecase.uid = session.getUserId();
			usecase.page = request.get("page");
			
			if(usecase.execute() && usecase.allowed){
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("page", request.get("page"));
				data.put("user_name", usecase.username);
				return new ApplicationResponseOK("templates/page.html", data);
			}
			
			return new ApplicationResponseDENIED();
		}
		
		return new ApplicationResponseILEGAL(request.get("page"));
	}
}
