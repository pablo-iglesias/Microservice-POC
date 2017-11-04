package core.database;

import java.io.File;
import java.sql.DriverManager;

public class DatabaseSQLite extends DatabaseRelational {

    public boolean connect() {
        if (conn != null) {
            return true;
        }

        try {
            Class.forName("org.sqlite.JDBC");
            File f = new File("users.db");
            boolean newDatabase = !f.exists();
            conn = DriverManager.getConnection("jdbc:sqlite:users.db");
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
