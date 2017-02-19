package core.factory;

import core.ResourceLoader;
import core.templating.TemplateEngine;
import core.templating.TemplateParserDefault;

public class TemplateFactory extends ResourceLoader {

	public TemplateEngine create(int type){
		try{
			switch(type){
				case TemplateEngine.TYPE_DEFAULT:
					TemplateEngine parser = new TemplateParserDefault();
					return parser;
				default:
					throw new Exception("TemplateParserFactory: Specified templating engine is not available");
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
}
