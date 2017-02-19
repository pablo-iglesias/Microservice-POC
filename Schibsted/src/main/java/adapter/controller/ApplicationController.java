package adapter.controller;

import core.Helper;
import core.Server;
import core.entity.HttpRequest;
import core.entity.Session;

import domain.application.usecase.UsecaseAuthenticateUser;
import domain.application.usecase.UsecasePage;
import domain.application.usecase.UsecaseWelcome;
import domain.factory.UserFactory;

import java.util.Map;
import java.util.HashMap;

import adapter.response.*;

public class ApplicationController {
	
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
			
			Map<String, String> params = Helper.parseRequestBody(request.getBody());
	        
			UsecaseAuthenticateUser usecase = new UsecaseAuthenticateUser(new UserFactory());
			usecase.username = params.get("username");
			usecase.password = params.get("password");
			
			if(usecase.execute()){
				session = Server.createSession(usecase.uid);
				return new ApplicationResponseREDIRECT("/welcome", session);
			}
			else{
				return new ApplicationResponseOK("templates/login.html", null);
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
	 * Welcome page, displays user name
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static ApplicationResponse welcome(HttpRequest request, Session session) throws Exception{
		
		if(session != null){
			
			UsecaseWelcome usecase = new UsecaseWelcome(new UserFactory());
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
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	public static ApplicationResponse page(HttpRequest request, Session session) throws Exception{
		
		if(session != null && request.contains("page")){
			
			UsecasePage usecase = new UsecasePage(new UserFactory());
			usecase.uid = session.getUserId();
			usecase.page = request.getData("page");
			
			if(usecase.execute() && usecase.allowed){
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("page", request.getData("page"));
				return new ApplicationResponseOK("templates/page.html", data);
			}
			
			return new ApplicationResponseDENIED();
		}
		
		return new ApplicationResponseILEGAL();
	}
}
