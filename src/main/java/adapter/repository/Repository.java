package adapter.repository;

import core.Server;
import core.database.Database;

public abstract class Repository {

    protected abstract Class getMongoClass();
    protected abstract Class getRelationalClass();

    protected Object createRepository(Repository repo) throws Exception{
        switch (Server.getConfig(Server.DATABASE_ENGINE)) {
            case Database.TYPE_MONGODB:
                return repo.getMongoClass().newInstance();
            case Database.TYPE_SQLITE:
            case Database.TYPE_SQLITE_MEMORY:
            case Database.TYPE_MYSQL:
            default:
                return repo.getRelationalClass().newInstance();
        }
    }
}
