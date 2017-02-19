package adapter.controller;

import core.Helper;
import core.Server;
import core.entity.HttpRequest;
import core.entity.Session;

import domain.application.usecase.UsecaseAuthenticateUser;
import domain.application.usecase.UsecaseWelcome;

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
			return new ResponseREDIRECT("/welcome", session);
		}
		
		return new ResponseOK("templates/login.html", null);
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
	        
			UsecaseAuthenticateUser usecase = new UsecaseAuthenticateUser();
			usecase.username = params.get("username");
			usecase.password = params.get("password");
			
			if(usecase.execute()){
				session = Server.createSession(usecase.uid);
				return new ResponseREDIRECT("/welcome", session);
			}
			else{
				return new ResponseOK("templates/login.html", null);
			}			
		}
		
		return new ResponseILEGAL();
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
			return new ResponseREDIRECT("/");
		}
		
		return new ResponseILEGAL();
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
			
			UsecaseWelcome usecase = new UsecaseWelcome();
			usecase.uid = session.getUserId();
			
			if(usecase.execute()){
				Map<String, String> data = new HashMap<String, String>();
				data.put("user_name", usecase.username);
				return new ResponseOK("templates/welcome.html", data);
			}
		}
		
		return new ResponseILEGAL();
	}
}
