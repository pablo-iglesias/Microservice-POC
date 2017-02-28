package core.database;

import core.ResourceLoader;

public abstract class Database extends ResourceLoader {

    public static final int TYPE_SQLITE = 1;
    public static final int TYPE_SQLITE_MEMORY = 2;

    public abstract boolean connect();
    public abstract boolean startTransaction();
    public abstract boolean rollback();
    public abstract boolean commit();
}
