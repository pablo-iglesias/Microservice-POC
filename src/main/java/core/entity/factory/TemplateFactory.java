package core.entity.factory;

import core.Server;
import core.templating.TemplateEngine;
import core.templating.TemplateParserDefault;
import core.templating.TemplateParserTwig;

public class TemplateFactory {

    public static TemplateEngine getTemplateParser() throws Exception {

        switch (Server.getTemplateEngineType()) {
            case DEFAULT: return new TemplateParserDefault();
            case TWIG: return new TemplateParserTwig();
            default:
                throw new Exception("TemplateFactory: Specified templating engine is not available");
        }
    }
}
