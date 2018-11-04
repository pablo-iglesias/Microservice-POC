package core;

import java.io.IOException;
import java.util.Properties;

public class AppProperties extends ResourceLoader {

    private static final String PROPERTIES_FILENAME = "app.properties";
    private static final Properties properties = new Properties();

    public static String get(String name) throws IOException {
        if(properties.isEmpty()){
            properties.load(loadResourceAsInputStream(PROPERTIES_FILENAME));
        }
        return properties.getProperty(name);
    }
}
