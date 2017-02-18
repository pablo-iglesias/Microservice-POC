package core.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseSQLite extends Database {
	
	private Connection conn = null;
	
	public void connect(String dbname) 
	{
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");
            
            System.out.println("Connection to SQLite has been established");
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
