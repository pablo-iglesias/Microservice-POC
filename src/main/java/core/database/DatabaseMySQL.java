package core.database;

import core.Server;
import java.sql.DriverManager;

import javax.enterprise.inject.Alternative;

@Alternative
public class DatabaseMySQL extends DatabaseRelational {

    public boolean connect() {
        if (conn != null) {
            return true;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String host = Server.getConfig(Server.Config.MYSQL_HOST);
            String port = Server.getConfig(Server.Config.MYSQL_PORT);
            String db   = Server.getConfig(Server.Config.MYSQL_DB);
            String user = Server.getConfig(Server.Config.MYSQL_USER);
            String pass = Server.getConfig(Server.Config.MYSQL_PASS);

            String connectionString = "jdbc:mysql://" + host + ":" + port + "/" + db;
            conn = DriverManager.getConnection(connectionString, user, pass);

            prepare("SELECT table_name FROM information_schema.tables WHERE table_schema = '" + db + "' AND table_name = 'users'");

            if (!selectOne()) {
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
