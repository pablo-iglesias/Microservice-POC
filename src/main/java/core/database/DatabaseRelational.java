package core.database;

import core.Helper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.enterprise.inject.Alternative;

@Alternative
public abstract class DatabaseRelational extends Database {

    protected static Connection conn = null;
    protected PreparedStatement pstmt = null;
    protected ResultSet rs = null;
    protected int pointer = 0;

    public abstract boolean connect();

    public boolean dump(String dump) {
        if (conn == null) {
            return false;
        }

        try {
            startTransaction();

            Statement stmt = conn.createStatement();

            // Get all the statements of the SQL dump line by line
            List<String> list = Helper.list(dump, "([^;]+;)");

            for (String sql : list) {
                stmt.execute(sql);
            }

            commit();
            return true;
        } catch (SQLException e) {
            rollback();
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean prepare(String sql) {
        if (conn == null) {
            return false;
        }

        try {
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pointer = 1;
            rs = null;
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean add(String param) {
        if (conn == null || pstmt == null) {
            return false;
        }

        try {
            pstmt.setString(pointer, param);
            pointer++;
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean add(int param) {
        if (conn == null || pstmt == null) {
            return false;
        }

        try {
            pstmt.setInt(pointer, param);
            pointer++;
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean startTransaction() {
        if (conn == null) {
            return false;
        }

        try {
            conn.setAutoCommit(false);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean rollback() {
        if (conn == null) {
            return false;
        }

        try {
            conn.rollback();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean commit() {
        if (conn == null) {
            return false;
        }

        try {
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean select() {
        if (conn == null || pstmt == null) {
            return false;
        }

        try {
            rs = pstmt.executeQuery();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean selectOne() {
        if (conn == null || pstmt == null) {
            return false;
        }

        try {
            rs = pstmt.executeQuery();
            return next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Integer insert() {
        if (conn == null || pstmt == null) {
            return null;
        }

        try {
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean update() {
        if (conn == null || pstmt == null) {
            return false;
        }

        try {
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean delete() {
        if (conn == null || pstmt == null) {
            return false;
        }

        try {
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String getString(String paramName) {
        try {
            if (rs != null) {
                return rs.getString(paramName);
            }
            return "";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public int getInt(String paramName) {
        try {
            if (rs != null) {
                return rs.getInt(paramName);
            }
            return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public boolean next() {
        try {
            if (!rs.next()) {
                rs = null;
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int count() {
        try {
            int current = rs.getRow();
            if (rs.last()) {
                int rows = rs.getRow();
                rs.absolute(current);
                return rows;
            }
            return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int pointer() {
        return pointer;
    }
}
