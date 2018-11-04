package core;

import com.sun.net.httpserver.HttpServer;
import core.database.Database;
import core.database.factory.DatabaseFactory;
import core.entity.Session;
import core.entity.factory.SessionFactory;
import core.entity.factory.TemplateFactory;
import core.templating.TemplateEngine;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server class
 * 
 * @author Peibol
 */
public class Server {

    private static final String ENVIRONMENT_VARIABLE_PREFIX = "POC_";

    // Dependency injector
    private final static class Injector {
        private static final SeContainer injector = SeContainerInitializer.newInstance().initialize();
        public static <O extends Object> O getInstance(Class<O> a){
            return injector.select(a).get();
        }
    }

    public enum Config{
        PORT,
        DATABASE_ENGINE,
        TEMPLATE_ENGINE,
        MYSQL_HOST,
        MYSQL_PORT,
        MYSQL_DB,
        MYSQL_USER,
        MYSQL_PASS,
        SQLITE_DB,
        MONGO_HOST,
        MONGO_PORT,
        MONGO_DB,
        MONGO_USER,
        MONGO_PASS
    }

    private static boolean debug = false;
    private static final Map<Config, String> config = new HashMap<>();
    private static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    public static void Initialize(String[] args) {
        try {
            // Check arguments
            for(String arg : args){
                // Is debug mode enabled ?
                if(arg.equals("debug")){
                    debug = true;
                }
            }

            // Load configs from environment var or set default values from properties file
            for(Config entry : Config.values()){
                loadConfig(entry);
            }

            // Init database
            Database database = getDatabase();
            if (database != null && !database.connect()) {
                throw new Exception("Database connection refused");
            }

            // Init HTTP server
            InetSocketAddress socket = new InetSocketAddress(Integer.valueOf(getConfig(Config.PORT)));
            RequestHandler handler = new RequestHandler();
            HttpServer server = HttpServer.create(socket, 0);
            server.createContext("/", handler);
            server.start();
            System.out.println("Running at port " + getConfig(Config.PORT) + (debug ? " in debug mode" : "") + "... \r\n");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Initialization failed, aborting");
            System.exit(1);
        }
    }

    public static <O extends Object> O getInstance(Class<O> a){
        return Injector.getInstance(a);
    }

    public static Database getDatabase() throws Exception {
        return DatabaseFactory.getDatabase();
    }

    public static Database.Type getDatabaseType(){
        try {
            return Database.Type.valueOf(
                getConfig(Config.DATABASE_ENGINE)
            );
        }
        catch(IllegalArgumentException e){
            return Database.Type.UNKNOWN;
        }
    }

    public static TemplateEngine getTemplateParser() throws Exception {
        return TemplateFactory.getTemplateParser();
    }

    public static TemplateEngine.Type getTemplateEngineType() {
        try {
            return TemplateEngine.Type.valueOf(
                    getConfig(Config.TEMPLATE_ENGINE)
            );
        }
        catch(IllegalArgumentException e){
            return TemplateEngine.Type.UNKNOWN;
        }
    }

    private static void loadConfig(Config config) throws IOException{
        String value = System.getenv(ENVIRONMENT_VARIABLE_PREFIX + config.name());
        if(value == null){
            value = AppProperties.get(config.name());
        }
        setConfig(config, value);
    }

    public static Session createSession(int uid) {
        Session session = SessionFactory.create(uid);
        sessions.put(session.getSessionToken(), session);
        return session;
    }

    public static void removeSession(String sessionToken) {
        sessions.remove(sessionToken);
    }

    public static Session getSession(String sessionToken) {
        return sessions.get(sessionToken);
    }

    private static void setConfig(Config name, String value){
        config.put(name, value);
    }

    public static String getConfig(Config entry){
        return config.get(entry);
    }

    public static boolean isDebug(){
        return debug;
    }
}
