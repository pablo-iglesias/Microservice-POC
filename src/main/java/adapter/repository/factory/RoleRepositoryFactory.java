package adapter.repository.factory;

import adapter.repository.nosql.RoleRepositoryMongo;
import adapter.repository.relational.RoleRepositoryRelational;
import domain.constraints.repository.IRoleRepository;

import javax.enterprise.inject.Produces;

public class RoleRepositoryFactory extends RepositoryFactory {

    @Produces
    public IRoleRepository createRepository() throws Exception{

        switch (getDatabaseType()) {
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
