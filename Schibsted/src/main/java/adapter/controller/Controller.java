package adapter.controller;

import java.util.Map;

import core.Helper;
import domain.usecase.UsecaseAuthenticateUser;

public class Controller {

	/**
	 * The authenticate method, rather than being itself a page or rest endpoint handler, is used by those
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	protected static Integer authenticate(String username, String password) throws Exception{
		
		UsecaseAuthenticateUser usecase = new UsecaseAuthenticateUser();
		usecase.username = username;
		usecase.password = password;
		
		if(usecase.execute()){
			return new Integer(usecase.uid);
		}
		
		return null;
	}
	
	/**
	 * Parses a POST or GET query string and returns its parameters as a key-value map
	 * 
	 * @return
	 */
	protected static Map<String, String> parseQueryString(String queryString){
		
		return Helper.map(queryString, "&*([^=]+)=([^&]+)");
	}
}
