package core.factory;

import core.ResourceLoader;
import core.database.Database;
import core.database.DatabaseSQLite;
import core.database.DatabaseSQLiteMemory;

public class DatabaseFactory extends ResourceLoader {

    public Database create(int type) {
        try {
            switch (type) {
                case Database.TYPE_SQLITE:
                    return new DatabaseSQLite();
                case Database.TYPE_SQLITE_MEMORY:
                    return new DatabaseSQLiteMemory();
                default:
                    throw new Exception("DatabaseFactory: Specified database type is not available");
            }
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
