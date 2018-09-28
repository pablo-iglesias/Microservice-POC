package adapter.repository.factory;

import adapter.repository.nosql.UserRepositoryMongo;
import adapter.repository.relational.UserRepositoryRelational;
import domain.constraints.repository.IUserRepository;

import core.Server;
import core.database.factory.DatabaseFactory;

import javax.enterprise.inject.Produces;

public class UserRepositoryFactory {

    @Produces
    public IUserRepository createRepository() throws Exception{

        switch (Server.getDatabaseType()) {
            case MONGODB:
                return new UserRepositoryMongo(
                        DatabaseFactory.createDatabaseMongoDB()
                );
            case SQLITE:
            case SQLITE_MEMORY:
            case MYSQL:
            default:
                return new UserRepositoryRelational(
                        DatabaseFactory.createDatabaseRelational()
                );
        }
    }
}
