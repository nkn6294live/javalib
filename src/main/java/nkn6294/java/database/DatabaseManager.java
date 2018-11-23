package nkn6294.java.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import nkn6294.java.lib.util.StringUtils;

public class DatabaseManager {

	static {
//		try {
//			Class.forName("com.mysql.jdbc.Driver").newInstance();
//		} catch (Exception ex) {
//		}
	}

	public static void init(String host, int port, String database, String user, String password, int numberConnectionPool) {
		DatabaseManager.host = host;
		DatabaseManager.port = port;
		DatabaseManager.database = database;
		DatabaseManager.user = user;
		DatabaseManager.password = password;
		DatabaseManager.numberConnectionPool = numberConnectionPool;
		DatabaseManager.createDatabaseIfNotExited(DatabaseManager.database);
		DatabaseManager.isChangeDatabaseConfig = true;
	}
	
	public static void init() {
		DatabaseManager.host = "localhost";
		DatabaseManager.port = 3306;
		DatabaseManager.database = "test";
		DatabaseManager.user = "test";
		DatabaseManager.password = "test";
		DatabaseManager.numberConnectionPool = 5;
		DatabaseManager.createDatabaseIfNotExited(DatabaseManager.database);
		isChangeDatabaseConfig = true;
	}
	public static Statement getStatement() {
		try {
			return DatabaseManager.getConnection().createStatement();
		} catch (Exception e) {
			return null;
		}
	}

	public static void closeResourceWithConnection(Connection connection) {
		if (connection == null) {
			return;
		}
		try {
			connection.close();
		} catch (SQLException e) {
		}
		connection = null;
	}
	public static void closeResourceWithStatement(Statement statement) {
		if (statement == null) {
			return;
		}		
		try {
			statement.close();
		} catch (SQLException e1) {
		}
		try {
			closeResourceWithConnection(statement.getConnection());
		} catch (Exception e) {
		}
		statement = null;
	}
	public static void closeResourceWithResultSet(ResultSet resultSet) {
		if (resultSet == null) {
			return;
		}
		try {
			resultSet.close();
			closeResourceWithStatement(resultSet.getStatement());
		} catch (SQLException e) {
		}
		resultSet = null;
	}
	
	public static CallableStatement getCallableStatement(String query, Object... params) {
		try {
			Connection connection = DatabaseManager.getConnection();
			if (connection == null) {
				return null;
			}
			CallableStatement callableStatement = connection.prepareCall(query);
			if (callableStatement == null) {
				return null;
			}
			for (int index = 0; index < params.length; index++) {
				updateValueForStatement(callableStatement, index + 1, params[index]);
			}
			return callableStatement;
		} catch (Exception e) {
			return null;
		}
	}
	public static CallableStatement getCallableStatement(String query, List<Object> params) {
		try {
			Connection connection = DatabaseManager.getConnection();
			if (connection == null) {
				return null;
			}
			CallableStatement callableStatement = connection.prepareCall(query);
			if (callableStatement == null) {
				return null;
			}
			for (int index = 0; index < params.size(); index++) {
				updateValueForStatement(callableStatement, index + 1, params.get(index));
			}
			return callableStatement;
		} catch (Exception e) {
			return null;
		}
	}
	public static CallableStatement getCallableStatement(String query, Map<String, Object> params) {
		try {
			Connection connection = DatabaseManager.getConnection();
			if (connection == null) {
				return null;
			}
			CallableStatement callableStatement = connection.prepareCall(query);
			if (callableStatement == null) {
				return null;
			}
			if (params == null) {
				return callableStatement;
			}
			for (Entry<String, Object> entry : params.entrySet()) {
				updateValueForStatement(callableStatement, entry.getKey(), entry.getValue());
			}
			return callableStatement;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean createDatabaseIfNotExited(String databaseName) {
		if (StringUtils.isStringNullOrEmpty(databaseName)) {
			return false;
		}
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
			DatabaseManager.closeResourceWithStatement(statement);
		}
	}
	
	private static String host = "localhost";
	private static int port = 3306;
	private static String database = "testdb";
	private static String user = "root";
	private static String password = "";
	private static DataSource dataSource;
	private static boolean isChangeDatabaseConfig = false;
	private static int numberConnectionPool = 5;
	
	private static Connection getConnection() {
		try {
			return configConnection(DatabaseManager.getDataSource(isChangeDatabaseConfig).getConnection());
		} catch (Exception e) {
			return null;
		}
	}
	
	private static DataSource getDataSource(boolean isCreateNew) {
		if (dataSource != null) {
			if (!isCreateNew) {
				return dataSource;
			}
			dataSource = null;  
		}
		dataSource = createNewDataSource(true);
		return dataSource;
	}
	
	private static DataSource getDataSourceWithOutDatabaseName() {
		return createNewDataSource(false);
	}
	
	private static Connection configConnection(Connection connection) {
		// TODO config connection: commit, timeout...
		return connection;
	}
	
	private static DataSource createNewDataSource(boolean withDatabase) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl(getURI(withDatabase));
		dataSource.setMaxIdle(numberConnectionPool);
		dataSource.setMinIdle(numberConnectionPool / 2);
		dataSource.setMaxOpenPreparedStatements(numberConnectionPool * 5);
		// TODO config other dataSource(default value for connection...)
		return dataSource;
	}
	
	private final static String getURI(boolean withDatabase) {
		if (withDatabase) {
			return String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s&verifyServerCertificate=false&useSSL=true&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8", host, port, database, user, password);			
		}
		return String.format("jdbc:mysql://%s:%s?user=%s&password=%s&verifyServerCertificate=false&useSSL=true&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8", host, port, user, password);
	}
	
	private final static void updateValueForStatement(CallableStatement callableStatement, int index, Object param) throws SQLException {
		if (callableStatement == null || index < 1 || param == null) {
			return;
		}
		if (param instanceof String) {
			callableStatement.setString(index, (String) param);
		} else if (param instanceof Integer) {
			callableStatement.setInt(index, (int) param);
		} else if (param instanceof Long) {
			callableStatement.setLong(index, (long) param);
		} else if (param instanceof Float) {
			callableStatement.setFloat(index, (float) param);
		} else if (param instanceof Timestamp) {
			callableStatement.setTimestamp(index, (Timestamp)param);
		} else if (param instanceof Time) {
			callableStatement.setTime(index, (Time)param);
		} else if (param instanceof java.sql.Date) {
			callableStatement.setDate(index, (java.sql.Date)param);
		} else if (param instanceof java.util.Date) {
			callableStatement.setDate(index, (java.sql.Date)param);
		} else {
			callableStatement.setObject(index, param);
		}
	}
	private final static void updateValueForStatement(CallableStatement callableStatement, String key, Object value) throws SQLException {
		if (callableStatement == null || StringUtils.isStringNullOrEmpty(key) || value == null) {
			return;
		}
		if (value instanceof String) {
			callableStatement.setString(key, (String) value);
		} else if (value instanceof Integer) {
			callableStatement.setInt(key, (int) value);
		} else if (value instanceof Float) {
			callableStatement.setFloat(key, (float) value);
		} else if (value instanceof Timestamp) {
			callableStatement.setTimestamp(key, (Timestamp)value);
		} else if (value instanceof Time) {
			callableStatement.setTime(key, (Time)value);
		} else if (value instanceof java.sql.Date) {
			callableStatement.setDate(key, (java.sql.Date)value);
		} else if (value instanceof java.util.Date) {
			callableStatement.setDate(key, (java.sql.Date)value);
		} else {
			callableStatement.setObject(key, value);
		}
	}
}
