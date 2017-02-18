package core;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import core.database.*;
import core.templating.TemplateParser;
import core.templating.TemplateParserFactory;

public class Server {
	
	private static final int PORT = 8000;
	
	private static Database usersDb;
	private static TemplateParser templateParser;
	
	public static Database getUsersDatabase(){
		return usersDb;
	}
	
	public static TemplateParser getTemplateParser(){
		return templateParser;
	}
	
	public static void Initialize(){
		try{
			
			// Init database
			usersDb = DatabaseFactory.create(Database.TYPE_SQLITE, "users");
			if(usersDb == null){
				throw new Exception("Database Initialization failed");
			}
			
			// Init templating engine
			templateParser = TemplateParserFactory.create(TemplateParser.TYPE_DEFAULT);
			if(templateParser == null){
				throw new Exception("Templating Engine Initialization failed");
			}
			
			// Init HTTP server
			InetSocketAddress socket = new InetSocketAddress(PORT);
			RequestHandler handler = new RequestHandler();
			HttpServer server = HttpServer.create(socket, 0);
    		server.createContext("/", handler);
    		server.start();
    		System.out.println("Running in port " + PORT + "...");
    	}
    	catch(Exception e){
    		System.out.println("Initialization failed, aborting");
    		System.out.println(e);
    		System.exit(1);
    	}
	}

}
