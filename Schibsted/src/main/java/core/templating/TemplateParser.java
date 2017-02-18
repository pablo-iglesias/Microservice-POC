package core.templating;

import java.util.Map;

public abstract class TemplateParser {

	public static final int TYPE_DEFAULT = 1;
	
	public abstract String parseTemplate(String view, Map<String, String> params);
}
