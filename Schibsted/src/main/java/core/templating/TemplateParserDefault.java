package core.templating;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class TemplateParserDefault extends TemplateEngine {
	
	public String parseTemplate(String view, Map<String, String> params){
		
		String template = loadResourceAsString(view);
		
		if(template != null){
			if(params != null){
				Iterator<Entry<String, String>> it = params.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
			        template = template.replaceAll("\\{\\{"+pair.getKey()+"\\}\\}", pair.getValue());
			        it.remove();
			    }
			}
			
			return template;
		}
		else{
			return "";
		}
	}
}
