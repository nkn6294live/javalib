package nkn6294.java.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DatabaseManager {

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			lastActiveConnection = System.currentTimeMillis();
		} catch (Exception ex) {
		}
	}

	public static Connection getConnection(boolean isCreateNew) {
		try {
			if (isCreateNew) {
				DatabaseManager.closeConnection();
			}
			if (DatabaseManager.isTimeOut()) {
				DatabaseManager.closeConnection();
			}
			if (DatabaseManager.connection == null || DatabaseManager.connection.isClosed()) {
//				DatabaseManager.connection = DriverManager.getConnection(DatabaseManager.getURL());
				DatabaseManager.connection = DatabaseManager.getDataSource(isChangeDatabaseConfig).getConnection();
			}			
			DatabaseManager.updateLastActive();
			return DatabaseManager.connection;
		} catch (SQLException e) {
			
			return null;
		} catch (Exception e) {
			
			return null;
		}
	}
	public static void closeConnection() {
		try {
			if (DatabaseManager.connection == null) {
				return;
			}
			synchronized (DatabaseManager.connection) {
				DatabaseManager.connection.close();
			}
		} catch(Exception ex) {
			
		} finally {
			DatabaseManager.connection = null;
		}
	}
	public static Statement getStatement() {
		Connection connection = DatabaseManager.getConnection(false);
		if (connection == null) {
			return null;
		}
		try {
			Statement statement = connection.createStatement();
			return statement;
		} catch (SQLException e) {
			
			return null;
		} catch (Exception e) {
			
			return null;
		}
	}

	public static CallableStatement getCallableStatement(String query, Object... params) {
		Connection connection = DatabaseManager.getConnection(false);
		if (connection == null) {
			return null;
		}
		try {
			CallableStatement callableStatement = connection.prepareCall(query);
			if (callableStatement == null) {
				return null;
			}
			for (int index = 0; index < params.length; index++) {
				Object param = params[index];
				if (param instanceof String) {
					callableStatement.setString(index + 1, (String) param);
				} else if (param instanceof Integer) {
					callableStatement.setInt(index + 1, (int) param);
				} else if (param instanceof Long) {
					callableStatement.setLong(index + 1, (long) param);
				} else if (param instanceof Float) {
					callableStatement.setFloat(index + 1, (float) param);
				} else {
					callableStatement.setObject(index + 1, null);
				}
			}
			return callableStatement;
		} catch (SQLException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static CallableStatement getCallableStatement(String query, HashMap<String, Object> params) {
		Connection connection = DatabaseManager.getConnection(false);
		if (connection == null) {
			return null;
		}
		try {
			CallableStatement callableStatement = connection.prepareCall(query);
			if (callableStatement == null) {
				return null;
			}
			if (params == null) {
				return callableStatement;
			}
			for (Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof String) {
					callableStatement.setString(key, (String) value);
				} else if (value instanceof Integer) {
					callableStatement.setInt(key, (int) value);
				} else if (value instanceof Float) {
					callableStatement.setFloat(key, (float) value);
				} else {
					callableStatement.setObject(key, null);
				}
			}
			return callableStatement;
		} catch (SQLException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean createDatabaseIfNotExited(String databaseName) {
		DataSource dataSource = getDataSourceWithOutDatabaseName();
		if (dataSource == null) {
			return false;
		}
		Connection connection = null;
		Statement statement = null;
		try {
			String statementQuery = String.format("CREATE DATABASE IF NOT EXISTS `%s` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;", databaseName);
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			return statement.executeUpdate(statementQuery) > 0;
		} catch (Exception e) {
			return false;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch(Exception ex) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch(Exception ex) {
				}
			}
		}
	}
	
	private static String host = "localhost";
	private static int port = 3306;
	private static String database = "testdb";
	private static String user = "root";
	private static String password = "";
	private static Connection connection;
	private static long lastActiveConnection;
	private static MysqlDataSource dataSource;
	private static boolean isChangeDatabaseConfig = false;

	private static DataSource getDataSource(boolean isCreateNew) {
		if (dataSource != null) {
			if (!isCreateNew) {
				return dataSource;
			}
			dataSource = null;  
		}
		dataSource = new MysqlDataSource();
		dataSource.setServerName(host);
		dataSource.setPort(port);
		dataSource.setDatabaseName(database);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		dataSource.setUseUnicode(true);
		dataSource.setCharacterEncoding("UTF8");
		return dataSource;
	}
	
	private static DataSource getDataSourceWithOutDatabaseName() {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName(host);
		dataSource.setPort(port);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		dataSource.setUseUnicode(true);
		dataSource.setCharacterEncoding("UTF8");
		return dataSource;
	}
	
//	private static String getURL() {
//		String url = String.format(
//				"jdbc:mysql://%s:%s/%s?user=%s&password=%s&verifyServerCertificate=false&useSSL=true&autoReconnect=true",
//				host, port, database, user, password);
//		return url;
//	}
	
	private static void updateLastActive() {
		DatabaseManager.lastActiveConnection = System.currentTimeMillis();
	}
	
	private static boolean isTimeOut() {
		return (System.currentTimeMillis() - DatabaseManager.lastActiveConnection) > 100 * 1000;
	}
}
