package core;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.net.httpserver.HttpServer;

import core.database.Database;
import core.entity.Session;
import core.entity.factory.DatabaseFactory;
import core.entity.factory.SessionFactory;
import core.entity.factory.TemplateFactory;
import core.templating.TemplateEngine;

/**
 * Server class
 * 
 * @author Peibol
 */
public class Server {

    // Environment vars
    public static final String PORT = "POC_PORT";
    public static final String DATABASE_ENGINE = "POC_DATABASE_ENGINE";
    public static final String TEMPLATE_ENGINE = "POC_TEMPLATE_ENGINE";

    public static final String MYSQL_HOST = "POC_MYSQL_HOST";
    public static final String MYSQL_PORT = "POC_MYSQL_PORT";
    public static final String MYSQL_DB = "POC_MYSQL_DB";
    public static final String MYSQL_USER = "POC_MYSQL_USER";
    public static final String MYSQL_PASS = "POC_MYSQL_PASS";

    public static final String SQLITE_DB = "POC_SQLITE_DB";

    public static final String MONGO_HOST = "POC_MONGO_HOST";
    public static final String MONGO_PORT = "POC_MONGO_PORT";
    public static final String MONGO_DB = "POC_MONGO_DB";
    public static final String MONGO_USER = "POC_MONGO_USER";
    public static final String MONGO_PASS = "POC_MONGO_PASS";

    private static boolean debug = false;
    private static Map<String, Object> config = new HashMap<>();
    private static ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    public static void Initialize(String[] args) {
        try {
            // Is debug mode enabled ?
            for(String arg : args){
                if(arg.equals("debug")){
                    debug = true;
                }
            }

            // Load configs from enviornment or set to defaults
            loadConfigs();

            // Init database
            Database database = getDatabase();
            if (!database.connect()) {
                throw new Exception("Database Initialization failed");
            }

            // Init templating engine
            TemplateEngine templateParser = getTemplateParser();
            if (templateParser == null) {
                throw new Exception("Templating Engine Initialization failed");
            }

            // Init HTTP server
            InetSocketAddress socket = new InetSocketAddress(Integer.valueOf(getConfig(PORT)));
            RequestHandler handler = new RequestHandler();
            HttpServer server = HttpServer.create(socket, 0);
            server.createContext("/", handler);
            server.start();
            System.out.println("Running in port " + getConfig(PORT) + "... \r\n");
        }
        catch (Exception e) {
            System.out.println("Initialization failed, aborting");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void loadConfigs(){

        loadConfig(PORT, "8000");
        loadConfig(DATABASE_ENGINE, Database.TYPE_SQLITE_MEMORY);
        loadConfig(TEMPLATE_ENGINE, TemplateEngine.TYPE_TWIG);

        loadConfig(MYSQL_HOST, "localhost");
        loadConfig(MYSQL_PORT, "3306");
        loadConfig(MYSQL_DB, "poc");
        loadConfig(MYSQL_USER, "user");
        loadConfig(MYSQL_PASS, "pass");

        loadConfig(SQLITE_DB, "users.db");

        loadConfig(MONGO_HOST, "localhost");
        loadConfig(MONGO_PORT, "27017");
        loadConfig(MONGO_DB, "poc");
        loadConfig(MONGO_USER, "user");
        loadConfig(MONGO_PASS, "pass");
    }

    private static void loadConfig(String name, String defaultValue){
        String value = System.getenv(name);
        if(value == null){
            value = defaultValue;
        }
        setConfig(name, value);
    }

    public static Database getDatabase() {
        DatabaseFactory factory = new DatabaseFactory();
        return factory.create(getConfig(DATABASE_ENGINE));
    }

    public static TemplateEngine getTemplateParser() {
        TemplateFactory factory = new TemplateFactory();
        return factory.create(getConfig(TEMPLATE_ENGINE));
    }

    public static Session createSession(int uid) {
        SessionFactory factory = new SessionFactory();
        Session session = factory.create(uid);
        String sessionToken = session.getSessionToken();
        sessions.put(sessionToken, session);
        return session;
    }

    public static void removeSession(String sessionToken) {
        sessions.remove(sessionToken);
    }

    public static Session getSession(String sessionToken) {
        return sessions.get(sessionToken);
    }

    private static void setConfig(String name, String value){
        config.put(name, value);
    }

    public static String getConfig(String entry){
        return (String)config.get(entry);
    }

    public static boolean isDebug(){
        return debug;
    }
}
