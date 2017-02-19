package core.database;

import java.sql.SQLException;

public abstract class DatabaseRelational extends Database {

	public abstract boolean dump(String sql);
	public abstract boolean prepare(String sql);
	public abstract boolean add(String param);
	public abstract boolean add(int param);
	public abstract boolean select();
	public abstract boolean selectOne();
	public abstract String getString(String paramName) throws SQLException;
	public abstract int getInt(String paramName) throws SQLException;
	public abstract boolean next() throws SQLException;
	public abstract int count() throws SQLException;
	public abstract int pointer();
}
