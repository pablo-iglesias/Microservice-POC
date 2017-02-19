package core;

import java.io.InputStream;

import core.templating.TemplateParserDefault;

public class ResourceLoader {
	
	protected InputStream loadResourceAsInputStream(String path){
		
		try{
			InputStream resource = TemplateParserDefault.class.getClassLoader().getResourceAsStream(path);
			return resource;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}

	protected String loadResourceAsString(String path){
		
		InputStream resource = loadResourceAsInputStream(path);
		
		if(resource != null){
			return Helper.convertInputStreamToString(resource);
		}
		else{
			return null;
		}
	}
}
