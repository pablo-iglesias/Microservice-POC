package core.templating;

import java.util.Map;

import core.ResourceLoader;

public abstract class TemplateEngine extends ResourceLoader {

    public enum Type{
        DEFAULT,
        TWIG,
        UNKNOWN
    }

    public abstract String parseTemplate(String view, Map<String, Object> params);
}
