package adapter.model.factory;

import core.Server;
import core.database.Database;

import adapter.model.Model;

public class ModelFactory {

    /**
     * Creates a model of the appropriate class depending on the kind of model
     * and also on the kind of data source
     * 
     * @param name
     * @return
     * @throws Exception
     */
    public Model create(String name) throws Exception {

        String className = name;

        try {
            switch (Server.DATABASE_ENGINE) {
                case Database.TYPE_SQLITE:
                case Database.TYPE_SQLITE_MEMORY:
                default:
                    className = "adapter.model.relational." + name + "ModelRelational";
                    Class<?> c = Class.forName(className);
                    return (Model) c.newInstance();
            }
        } 
        catch (ClassNotFoundException e) {
            System.out.println("Did not find proper " + name + " model. Suitable model class should be: " + className);
            return null;
        }
    }
}
