package core;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.HashMap;

import com.sun.net.httpserver.HttpServer;

import core.database.Database;
import core.entity.Session;
import core.factory.DatabaseFactory;
import core.factory.SessionFactory;
import core.factory.TemplateFactory;
import core.templating.TemplateEngine;

/**
 * Server class
 * 
 * @author Peibol
 */
public class Server {

    private static final int PORT = 8000;

    public static final int DATABASE_ENGINE = Database.TYPE_SQLITE_MEMORY;
    public static final int TEMPLATE_ENGINE = TemplateEngine.TYPE_TWIG;

    private static Map<String, Session> sessions = new HashMap<String, Session>();

    public static void Initialize() {
        try {
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
            InetSocketAddress socket = new InetSocketAddress(PORT);
            RequestHandler handler = new RequestHandler();
            HttpServer server = HttpServer.create(socket, 0);
            server.createContext("/", handler);
            server.start();
            System.out.println("Running in port " + PORT + "... \r\n");
        } 
        catch (Exception e) {
            System.out.println("Initialization failed, aborting");
            System.out.println(e);
            System.exit(1);
        }
    }

    public static Database getDatabase() {
        DatabaseFactory factory = new DatabaseFactory();
        return factory.create(DATABASE_ENGINE);
    }

    public static TemplateEngine getTemplateParser() {
        TemplateFactory factory = new TemplateFactory();
        return factory.create(TEMPLATE_ENGINE);
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
        Session session = sessions.get(sessionToken);
        return session;
    }
}
