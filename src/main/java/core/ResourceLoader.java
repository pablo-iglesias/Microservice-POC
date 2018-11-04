package core;

import java.io.InputStream;

public abstract class ResourceLoader {

    /**
     * Load a file from the filesystem as an input stream
     * 
     * @param path
     * @return
     */
    protected static InputStream loadResourceAsInputStream(String path) {

        try {
            InputStream resource = ResourceLoader.class.getClassLoader().getResourceAsStream(path);
            return resource;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Load a file from the filesystem as a String
     * 
     * @param path
     * @return
     */
    protected static String loadResourceAsString(String path) {

        InputStream resource = loadResourceAsInputStream(path);

        if (resource != null) {
            return Helper.convertInputStreamToString(resource);
        } else {
            return null;
        }
    }
}
