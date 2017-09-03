package adapter.repository.factory;

import core.Server;
import core.database.Database;

import adapter.repository.Repository;

public class RepositoryFactory {

    /**
     * Creates a model of the appropriate class depending on the kind of model
     * and also on the kind of data source
     * 
     * @param name
     * @return
     * @throws Exception
     */
    public Repository create(String name) throws Exception {

        String className = name;

        try {
            switch (Server.DATABASE_ENGINE) {
                case Database.TYPE_SQLITE:
                case Database.TYPE_SQLITE_MEMORY:
                default:
                    className = "adapter.repository.relational." + name + "RepositoryRelational";
                    Class<?> c = Class.forName(className);
                    return (Repository) c.newInstance();
            }
        } 
        catch (ClassNotFoundException e) {
            System.out.println("Did not find proper " + name + " model. Suitable model class should be: " + className);
            return null;
        }
    }
}
