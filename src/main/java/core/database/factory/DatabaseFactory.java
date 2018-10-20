package core.database.factory;

import core.Server;
import core.database.*;

import javax.enterprise.inject.Produces;

public class DatabaseFactory {

    @Produces
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
    public static DatabaseMongoDB createDatabaseMongoDB() {
        return new DatabaseMongoDB();
    }
}
