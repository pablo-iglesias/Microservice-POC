package core.templating;

import core.database.Database;
import core.database.DatabaseSQLite;

public class TemplateParserFactory {

	public static TemplateParser create(int type){
		try{
			switch(type){
				case TemplateParser.TYPE_DEFAULT:
					TemplateParser parser = new TemplateParserDefault();
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
