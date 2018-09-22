package core.database;

import core.ResourceLoader;

public abstract class Database extends ResourceLoader {

    public enum Type{
        SQLITE,
        SQLITE_MEMORY,
        MYSQL,
        MONGODB
    }

    public abstract boolean connect();
    public abstract boolean startTransaction();
    public abstract boolean rollback();
    public abstract boolean commit();
}
