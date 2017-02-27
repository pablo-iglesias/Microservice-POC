package core;

import java.io.InputStream;

import core.templating.TemplateParserDefault;

public class ResourceLoader {
	
	/**
	 * Load a file from the filesystem as an input stream
	 * 
	 * @param path
	 * @return
	 */
	protected InputStream loadResourceAsInputStream(String path){
		
		try{
			InputStream resource = ResourceLoader.class.getClassLoader().getResourceAsStream(path);
			return resource;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Load a file from the filesystem as a String
	 * 
	 * @param path
	 * @return
	 */
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
