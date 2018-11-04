package adapter.repository.factory;

import adapter.repository.nosql.UserRepositoryMongo;
import adapter.repository.relational.UserRepositoryRelational;
import domain.contract.repository.IUserRepository;

import core.Server;

import javax.enterprise.inject.Produces;

public class UserRepositoryFactory {

    @Produces
    public IUserRepository createRepository() {

        switch (Server.getDatabaseType()) {
            case MONGODB:
                return new UserRepositoryMongo();
            case SQLITE:
            case SQLITE_MEMORY:
            case MYSQL:
            default:
                return new UserRepositoryRelational();
        }
    }
}
