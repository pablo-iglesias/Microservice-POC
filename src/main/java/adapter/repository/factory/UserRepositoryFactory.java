package adapter.repository.factory;

import adapter.repository.nosql.UserRepositoryMongo;
import adapter.repository.relational.UserRepositoryRelational;
import domain.constraints.repository.IUserRepository;

import javax.enterprise.inject.Produces;

public class UserRepositoryFactory extends RepositoryFactory {

    @Produces
    public IUserRepository createRepository() throws Exception{

        switch (getDatabaseType()) {
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
