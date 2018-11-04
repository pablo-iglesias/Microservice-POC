package core.templating;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TemplateParserDefault extends TemplateEngine {

    public String parseTemplate(String view, Map<String, Object> params) {

        String template = loadResourceAsString(view);

        if (template != null) {
            if (params != null) {
                Iterator<Entry<String, Object>> it = params.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> pair = it.next();
                    template = template.replaceAll("\\{\\{" + pair.getKey() + "\\}\\}", (String) pair.getValue());
                    it.remove();
                }
            }

            return template;
        } else {
            return "";
        }
    }
}
