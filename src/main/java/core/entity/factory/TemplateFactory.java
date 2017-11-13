package core.entity.factory;

import core.ResourceLoader;
import core.templating.TemplateEngine;
import core.templating.TemplateParserDefault;
import core.templating.TemplateParserTwig;

public class TemplateFactory extends ResourceLoader {

    public TemplateEngine create(String type) {
        try {
            switch (type) {
                case TemplateEngine.TYPE_DEFAULT:
                    return new TemplateParserDefault();
                case TemplateEngine.TYPE_TWIG:
                    return new TemplateParserTwig();
                default:
                    throw new Exception("TemplateFactory: Specified templating engine is not available");
            }
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
