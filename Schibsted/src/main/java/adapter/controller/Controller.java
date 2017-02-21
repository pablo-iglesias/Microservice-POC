package adapter.controller;

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
	public static Integer authenticate(String username, String password) throws Exception{
		
		UsecaseAuthenticateUser usecase = new UsecaseAuthenticateUser();
		usecase.username = username;
		usecase.password = password;
		
		if(usecase.execute()){
			return new Integer(usecase.uid);
		}
		
		return null;
	}
}
