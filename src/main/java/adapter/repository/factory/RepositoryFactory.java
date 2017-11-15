package adapter.repository.factory;

import core.Server;
import core.database.Database;

import adapter.repository.Repository;

public class RepositoryFactory {

    /**
     * Creates a repository of the appropriate class depending on the kind of repository
     * and also on the kind of data source
     * 
     * @param name
     * @return
     * @throws Exception
     */
    public Repository create(String name) throws Exception {

        String className = name;
        Class<?> c;

        try {
            switch (Server.getConfig(Server.DATABASE_ENGINE)) {
                case Database.TYPE_MONGODB:
                    className = "adapter.repository.nosql." + name + "RepositoryMongo";
                    c = Class.forName(className);
                    return (Repository) c.newInstance();
                case Database.TYPE_SQLITE:
                case Database.TYPE_SQLITE_MEMORY:
                case Database.TYPE_MYSQL:
                default:
                    className = "adapter.repository.relational." + name + "RepositoryRelational";
                    c = Class.forName(className);
                    return (Repository) c.newInstance();
            }
        } 
        catch (ClassNotFoundException e) {
            System.out.println("Did not find proper " + name + " model. Suitable model class should be: " + className);
            return null;
        }
    }
}
