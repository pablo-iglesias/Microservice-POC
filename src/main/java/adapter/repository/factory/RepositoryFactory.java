package adapter.repository.factory;

import core.Server;
import core.database.Database;

public abstract class RepositoryFactory {

    protected Database.Type getDatabaseType(){
        return Database.Type.valueOf(
            Server.getConfig(Server.DATABASE_ENGINE)
        );
    }

    public abstract Object createRepository() throws Exception;
}
