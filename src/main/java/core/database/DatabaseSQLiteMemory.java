package core.database;

import java.sql.DriverManager;

import javax.enterprise.inject.Alternative;

@Alternative
public class DatabaseSQLiteMemory extends DatabaseSQLite {

    public boolean connect() {
        if (conn != null) {
            return true;
        }

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite::memory:");
            dump(loadResourceAsString("sql/dbdump.sql"));
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            conn = null;
            return false;
        }
    }
}
