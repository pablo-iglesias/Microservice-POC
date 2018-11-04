package core.database;

import core.Server;

import java.io.File;
import java.sql.DriverManager;

import javax.enterprise.inject.Alternative;

@Alternative
public class DatabaseSQLite extends DatabaseRelational {

    public boolean connect() {
        if (conn != null) {
            return true;
        }

        try {
            Class.forName("org.sqlite.JDBC");
            String db = Server.getConfig(Server.Config.SQLITE_DB);
            File f = new File(db);
            boolean newDatabase = !f.exists();
            conn = DriverManager.getConnection("jdbc:sqlite:" + db);
            if (newDatabase) {
                dump(loadResourceAsString("sql/dbdump.sql"));
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            conn = null;
            return false;
        }
    }
}
