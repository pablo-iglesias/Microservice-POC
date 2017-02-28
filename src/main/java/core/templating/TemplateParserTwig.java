package core.templating;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class TemplateParserTwig extends TemplateEngine {

    public String parseTemplate(String view, Map<String, Object> params) throws Exception {

        JtwigTemplate template = JtwigTemplate.classpathTemplate(view);
        JtwigModel model = JtwigModel.newModel();

        if (params != null) {
            Iterator<Entry<String, Object>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();
                model.with(pair.getKey(), pair.getValue());
                it.remove();
            }
        }

        OutputStream stream = new ByteArrayOutputStream();
        template.render(model, stream);

        return stream.toString();
    }
}
