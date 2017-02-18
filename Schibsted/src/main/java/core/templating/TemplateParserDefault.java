package core.templating;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class TemplateParserDefault extends TemplateParser {
	
	public String parseTemplate(String view, Map<String, String> params){
		
		InputStream file = TemplateParserDefault.class.getClassLoader().getResourceAsStream(view);
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("\\A");
		String template = scanner.hasNext() ? scanner.next() : "";
		scanner.close();
		
		Iterator<Entry<String, String>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
	        template = template.replaceAll("\\{\\{"+pair.getKey()+"\\}\\}", pair.getValue());
	        it.remove();
	    }
	    
	    return template;
	}
}
