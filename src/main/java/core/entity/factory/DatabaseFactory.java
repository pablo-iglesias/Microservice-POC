package core.entity.factory;

import core.ResourceLoader;
import core.database.Database;
import core.database.DatabaseMySQL;
import core.database.DatabaseMongoDB;
import core.database.DatabaseSQLite;
import core.database.DatabaseSQLiteMemory;

public class DatabaseFactory extends ResourceLoader {

    public Database create(String type) {
        try {
            switch (Database.Type.valueOf(type)) {
                case SQLITE:
                    return new DatabaseSQLite();
                case SQLITE_MEMORY:
                    return new DatabaseSQLiteMemory();
                case MYSQL:
                    return new DatabaseMySQL();
                case MONGODB:
                    return new DatabaseMongoDB();
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
