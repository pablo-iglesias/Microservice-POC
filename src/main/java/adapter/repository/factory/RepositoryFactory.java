package adapter.repository.factory;

import core.Server;
import core.database.Database;

public abstract class RepositoryFactory {

    private static final String repositoryRoot = "adapter.repository";

    public static Object createRepository(Class c) throws Exception{

        switch (Server.getConfig(Server.DATABASE_ENGINE)) {
            case Database.TYPE_MONGODB:
                return getRepositoryInstance(
                    c.getSimpleName(),
                    "nosql",
                    "Mongo"
                );
            case Database.TYPE_SQLITE:
            case Database.TYPE_SQLITE_MEMORY:
            case Database.TYPE_MYSQL:
            default:
                return getRepositoryInstance(
                    c.getSimpleName(),
                    "relational",
                    "Relational"
                );
        }
    }

    /**
     * Creates a repository instance of the given class, package and kind
     *
     * @param className     Base class
     * @param packageName   Target package
     * @param kind          Kind of datasource
     * @return
     * @throws Exception
     */
    private static Object getRepositoryInstance(String className, String packageName, String kind) throws Exception{

        return Class.forName(
            String.format("%s.%s.%s%s",
                repositoryRoot,
                packageName,
                className,
                kind
            )
        ).newInstance();
    }
}
