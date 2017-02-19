package core.templating;

import java.util.Map;

import core.ResourceLoader;

public abstract class TemplateEngine extends ResourceLoader {

	public static final int TYPE_DEFAULT = 1;
	public static final int TYPE_TWIG = 2;
	
	public abstract String parseTemplate(String view, Map<String, Object> params) throws Exception;
}
