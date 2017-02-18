package core.database;

public class DatabaseFactory {
	
	public static Database create(int type, String dbname){
		try{
			switch(type){
				case Database.TYPE_SQLITE:
					Database db = new DatabaseSQLite();
					db.connect(dbname);
					return db;
				default:
					throw new Exception("DatabaseFactory: Specified database type is not available");
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
}
