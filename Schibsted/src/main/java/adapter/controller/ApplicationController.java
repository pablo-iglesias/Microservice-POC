package adapter.controller;

import java.util.Map;
import java.util.HashMap;

import adapter.response.ApplicationResponse;

public class ApplicationController {
	
	public static ApplicationResponse index(){
				
		Map<String, String> data = new HashMap<String, String>();
		data.put("var", "World");
		
		return new ApplicationResponse(200, "templates/index.html", data);
	}
}
