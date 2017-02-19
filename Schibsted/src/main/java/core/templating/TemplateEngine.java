package core.templating;

import java.util.Map;

import core.ResourceLoader;

public abstract class TemplateEngine extends ResourceLoader {

	public static final int TYPE_DEFAULT = 1;
	
	public abstract String parseTemplate(String view, Map<String, String> params);
}
