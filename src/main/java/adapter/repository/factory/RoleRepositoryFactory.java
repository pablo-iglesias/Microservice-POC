package adapter.repository.factory;

import adapter.repository.nosql.RoleRepositoryMongo;
import adapter.repository.relational.RoleRepositoryRelational;
import domain.contract.repository.IRoleRepository;

import core.Server;

import javax.enterprise.inject.Produces;

public class RoleRepositoryFactory {

    @Produces
    public IRoleRepository createRepository() {

        switch (Server.getDatabaseType()) {
            case MONGODB:
                return new RoleRepositoryMongo();
            case SQLITE:
            case SQLITE_MEMORY:
            case MYSQL:
            default:
                return new RoleRepositoryRelational();
        }
    }
}
