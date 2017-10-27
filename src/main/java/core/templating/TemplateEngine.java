package core.templating;

import java.util.Map;

import core.ResourceLoader;

public abstract class TemplateEngine extends ResourceLoader {

    public static final String TYPE_DEFAULT = "DEFAULT";
    public static final String TYPE_TWIG = "TWIG";

    public abstract String parseTemplate(String view, Map<String, Object> params) throws Exception;
}
