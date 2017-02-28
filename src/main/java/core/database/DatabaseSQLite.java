package core.database;

import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import core.Helper;

public class DatabaseSQLite extends DatabaseRelational {
	
	protected PreparedStatement pstmt = null;
	protected ResultSet rs = null;
	protected int pointer = 0;
	protected boolean closed = false;
	
	public boolean connect() 
	{
		if(conn != null){
			return true;
		}
		
        try {
            Class.forName("org.sqlite.JDBC");
            File f = new File("users.db");
            boolean newDatabase = !f.exists(); 
            conn = DriverManager.getConnection("jdbc:sqlite:users.db");
            if(newDatabase){
            	dump(loadResourceAsString("sql/dbdump.sql"));
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            conn = null;
            return false;
        }
    }
	
	public boolean dump(String dump) 
	{
		if(conn == null){
			return false;
		}
		
        try {
        	Statement stmt = conn.createStatement();
        	
        	// Get all the statements of the SQL dump line by line
        	Vector<String> vector = Helper.vector(dump, "([^;]+;)");
        	
        	for (String sql : vector) {
        		stmt.execute(sql);
        	}
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
	
	public boolean prepare(String sql){
		if(conn == null){
			return false;
		}
		
        try {
        	pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        	pointer = 1;
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
	}
	
	public boolean add(String param){
		if(conn == null || pstmt == null){
			return false;
		}
		
        try {
        	pstmt.setString(pointer, param);
        	pointer++;
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
	}
	
	public boolean add(int param){
		if(conn == null || pstmt == null){
			return false;
		}
		
        try {
        	pstmt.setInt(pointer, param);
        	pointer++;
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
	}
	
	public boolean startTransaction(){
		if(conn == null){
			return false;
		}
		
		try {
			conn.setAutoCommit(false);
			return true;
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        return false;
	    }
	}
	
	public boolean rollback(){
		if(conn == null){
			return false;
		}
		
		try {
			conn.rollback();
			return true;
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        return false;
	    }
	}
	
	public boolean commit(){
		if(conn == null){
			return false;
		}
		
		try {
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        return false;
	    }
	}
	
	public boolean select(){
		if(conn == null || pstmt == null){
			return false;
		}
		
        try {
        	rs = pstmt.executeQuery();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
	}
	
	public boolean selectOne(){
		if(conn == null || pstmt == null){
			return false;
		}
		
        try {
        	rs = pstmt.executeQuery();
        	if(next()){
        		return true;
        	}
        	else{
        		return false;
        	}
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
	}
	
	public Integer insert(){
		if(conn == null || pstmt == null){
			return null;
		}
		
        try {
        	pstmt.executeUpdate();
        	ResultSet rs = pstmt.getGeneratedKeys();

        	if (rs.next()) {
        		return rs.getInt(1);
        	}
        	
        	return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
	}
	
	public boolean update(){
		if(conn == null || pstmt == null){
			return false;
		}
		
        try {
        	pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
	}
	
	public boolean delete(){
		if(conn == null || pstmt == null){
			return false;
		}
		
        try {
        	pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
	}
	
	public String getString(String paramName) throws SQLException{
		if(rs != null){
			return rs.getString(paramName);
		}
		return "";
	}
	
	public int getInt(String paramName) throws SQLException{
		if(rs != null){
			return rs.getInt(paramName);
		}
		return 0;
	}
	
	public boolean next() throws SQLException{
		if(!rs.next()){
    		rs = null;
    		return false;
    	}
		
		return true;
	}
	
	public int count() throws SQLException{
		int current = rs.getRow();
		if (rs.last()) {
		    int rows = rs.getRow();
		    rs.absolute(current);
		    return rows;
		}
		
		return 0;
	}
	
	public int pointer(){
		return pointer;
	}
}