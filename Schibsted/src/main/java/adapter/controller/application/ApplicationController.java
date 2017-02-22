package adapter.controller.application;

import java.util.Map;
import java.util.HashMap;

import core.Helper;
import core.Server;
import core.entity.HttpRequest;
import core.entity.Session;

import adapter.controller.Controller;
import adapter.response.application.ApplicationResponse;

import domain.entity.Role;
import domain.usecase.application.UsecasePage;
import domain.usecase.application.UsecaseWelcome;

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
			return new ApplicationResponse()
					.setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
					.setLocation("/welcome")
					.setSession(session);
		}
		
		return new ApplicationResponse()
				.setResponseCode(ApplicationResponse.RESPONSE_OK)
				.setView("templates/login.html");
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
							return new ApplicationResponse()
									.setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
									.setLocation("/page_" + params.get("page"))
									.setSession(session);
									
						}
					}
					
					return new ApplicationResponse()
							.setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
							.setLocation("/welcome")
							.setSession(session);
				}
			}
		}
		
		return new ApplicationResponse()
				.setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
				.setLocation("/");
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
			return new ApplicationResponse()
					.setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
					.setLocation("/");
		}
		
		return new ApplicationResponse()
				.setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
				.setLocation("/");
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
					for(Role role : usecase.roles){
						roles.put(role.getName(), role.getPage());
					}
					
					data.put("roles", roles);
				}
				
				return new ApplicationResponse()
						.setResponseCode(ApplicationResponse.RESPONSE_OK)
						.setView("templates/welcome.html")
						.setData(data);
			}
		}
		
		return new ApplicationResponse()
				.setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
				.setLocation("/");
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
				return new ApplicationResponse()
						.setResponseCode(ApplicationResponse.RESPONSE_OK)
						.setView("templates/page.html")
						.setData(data);
			}
			
			return new ApplicationResponse()
					.setResponseCode(ApplicationResponse.RESPONSE_DENIED);
		}
		
		return new ApplicationResponse()
				.setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
				.setLocation("/?page=" + request.get("page"));
	}
}
