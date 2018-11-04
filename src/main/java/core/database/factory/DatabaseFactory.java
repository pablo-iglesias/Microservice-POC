package core.database.factory;

import core.Server;
import core.database.*;

import javax.enterprise.inject.Produces;

public class DatabaseFactory {

    public static Database getDatabase() throws Exception {

        switch (Server.getDatabaseType()) {
            case SQLITE:
            case SQLITE_MEMORY:
            case MYSQL:
                return Server.getInstance(DatabaseRelational.class);
            case MONGODB:
                return Server.getInstance(DatabaseMongoDB.class);
            default:
                throw new Exception("DatabaseFactory: Specified database type is not available");
        }
    }

    @Produces
    public static DatabaseRelational createDatabaseRelational() throws Exception {

        switch (Server.getDatabaseType()) {
            case SQLITE: return new DatabaseSQLite();
            case SQLITE_MEMORY: return new DatabaseSQLiteMemory();
            case MYSQL: return new DatabaseMySQL();
            default:
                throw new Exception("DatabaseFactory: Specified database type is not available");
        }
    }

    @Produces
    public static DatabaseMongoDB createDatabaseMongoDB() {
        return new DatabaseMongoDB();
    }
}
