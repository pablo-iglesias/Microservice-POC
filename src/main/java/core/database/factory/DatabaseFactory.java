package core.database.factory;

import core.Server;
import core.database.*;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class DatabaseFactory {
    
    @Produces
    public static Database createDatabase() {

        try {
            switch (Server.getDatabaseType()) {
                case SQLITE:
                    return new DatabaseSQLite();
                case SQLITE_MEMORY:
                    return new DatabaseSQLiteMemory();
                case MYSQL:
                    return new DatabaseMySQL();
                case MONGODB:
                    return new DatabaseMongoDB();
                default:
                    throw new Exception("DatabaseFactory: Cannot determine database kind");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Produces
    @Named("DatabaseRelational")
    public static DatabaseRelational createDatabaseRelational() {
        try {
            switch (Server.getDatabaseType()) {
                case SQLITE:
                    return new DatabaseSQLite();
                case SQLITE_MEMORY:
                    return new DatabaseSQLiteMemory();
                case MYSQL:
                    return new DatabaseMySQL();
                default:
                    throw new Exception("DatabaseFactory: Specified database type is not available");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Produces
    @Named("DatabaseMongoDB")
    public static DatabaseMongoDB createDatabaseMongoDB() {
        return new DatabaseMongoDB();
    }
}
