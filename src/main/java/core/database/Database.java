package core.database;

import core.ResourceLoader;

public abstract class Database extends ResourceLoader {

    public static final String TYPE_SQLITE = "SQLITE";
    public static final String TYPE_SQLITE_MEMORY = "SQLITE_MEMORY";
    public static final String TYPE_MYSQL = "MYSQL";
    public static final String TYPE_MONGODB = "MONGODB";

    public abstract boolean connect();
    public abstract boolean startTransaction();
    public abstract boolean rollback();
    public abstract boolean commit();
}
