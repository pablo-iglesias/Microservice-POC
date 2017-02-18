package core.database;

public abstract class Database {
			
	public static final int TYPE_SQLITE = 1;
	
	public abstract void connect(String connStr);
}
