package nkn6294.java.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;
import org.json.simple.JSONObject;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import nkn6294.java.lib.util.StringUtils;

public class DatabaseManager_ {

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
		}
	}

	public static void init(JSONObject json) {
		DatabaseManager_.host = "localhost";
		DatabaseManager_.port = 3306;
		DatabaseManager_.database = "alexamanager";
		DatabaseManager_.user = "root";
		DatabaseManager_.password = "";
		DatabaseManager_.connectionWaitTimeout = 3600000;
		DatabaseManager_.numberConnectionPool = 5;
		DatabaseManager_.createDatabaseIfNotExited(DatabaseManager_.database);
		isChangeDatabaseConfig = true;
		updatePoolConnection();
	}

	public static Statement getStatement() {
		try {
			return DatabaseManager_.getConnection().createStatement();
		} catch (Exception e) {
			return null;
		}
	}

	public static CallableStatement getCallableStatement(String query, Object... params) {
		try {
			Connection connection = DatabaseManager_.getConnection();
			if (connection == null) {
				return null;
			}
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
				} else if (param instanceof Timestamp) {
					callableStatement.setTimestamp(index + 1, (Timestamp) param);
				} else if (param instanceof Time) {
					callableStatement.setTime(index + 1, (Time) param);
				} else if (param instanceof Date) {
					callableStatement.setDate(index + 1, (Date) param);
				} else if (param instanceof java.util.Date) {
					callableStatement.setDate(index + 1, (Date) param);
				} else {
					callableStatement.setObject(index + 1, param);
				}
			}
			return callableStatement;
		} catch (Exception e) {
			return null;
		}
	}

	public static CallableStatement getCallableStatement(String query, List<Object> params) {
		try {
			Connection connection = DatabaseManager_.getConnection();
			if (connection == null) {
				return null;
			}
			CallableStatement callableStatement = connection.prepareCall(query);
			if (callableStatement == null) {
				return null;
			}
			for (int index = 0; index < params.size(); index++) {
				Object param = params.get(index);
				if (param instanceof String) {
					callableStatement.setString(index + 1, (String) param);
				} else if (param instanceof Integer) {
					callableStatement.setInt(index + 1, (int) param);
				} else if (param instanceof Long) {
					callableStatement.setLong(index + 1, (long) param);
				} else if (param instanceof Float) {
					callableStatement.setFloat(index + 1, (float) param);
				} else if (param instanceof Timestamp) {
					callableStatement.setTimestamp(index + 1, (Timestamp) param);
				} else if (param instanceof Time) {
					callableStatement.setTime(index + 1, (Time) param);
				} else if (param instanceof Date) {
					callableStatement.setDate(index + 1, (Date) param);
				} else if (param instanceof java.util.Date) {
					callableStatement.setDate(index + 1, (Date) param);
				} else {
					callableStatement.setObject(index + 1, param);
				}
			}
			return callableStatement;
		} catch (Exception e) {
			return null;
		}
	}

	public static CallableStatement getCallableStatement(String query, Map<String, Object> params) {
		try {
			Connection connection = DatabaseManager_.getConnection();
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
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof String) {
					callableStatement.setString(key, (String) value);
				} else if (value instanceof Integer) {
					callableStatement.setInt(key, (int) value);
				} else if (value instanceof Float) {
					callableStatement.setFloat(key, (float) value);
				} else if (value instanceof Timestamp) {
					callableStatement.setTimestamp(key, (Timestamp) value);
				} else if (value instanceof Time) {
					callableStatement.setTime(key, (Time) value);
				} else if (value instanceof Date) {
					callableStatement.setDate(key, (Date) value);
				} else if (value instanceof java.util.Date) {
					callableStatement.setDate(key, (Date) value);
				} else {
					callableStatement.setObject(key, value);
				}
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
			String statementQuery = String.format(
					"CREATE DATABASE IF NOT EXISTS `%s` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;", databaseName);
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			return statement.executeUpdate(statementQuery) > 0;
		} catch (Exception e) {
			return false;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception ex) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ex) {
				}
			}
		}
	}

	public static void releasePool() {
		if (connections == null) {
			return;
		}
		for (ConnectionExpand item : connections) {
			try {
				item.getConnection().close();
			} catch (Exception ex) {
			}
		}
		connections.clear();
	}

	private static String host = "localhost";
	private static int port = 3306;
	private static String database = "testdb";
	private static String user = "root";
	private static String password = "";
	private static DataSource dataSource;
	private static boolean isChangeDatabaseConfig = false;
	private static long connectionWaitTimeout = 3600 * 1000;
	private static long numberConnectionPool = 5;
	private static LinkedList<ConnectionExpand> connections;
	private static int current_index = 0;
	private final static int MAX_COUNT_TRY_CREATE_CONNECTION = 10;

	private static Connection getConnection() {
		current_index++;
		if (current_index < 0 || current_index >= numberConnectionPool) {
			current_index = 0;
		}
		if (!updatePoolConnection()) {
			return null;
		}
		ConnectionExpand connectionExpand = connections.get(current_index);
		Connection connection = null;
		if (connectionExpand == null || !connectionExpand.isValid()) {
			connections.remove(current_index);
			if (connectionExpand != null) {
				connectionExpand.close();
			}
			connectionExpand = null;
			try {
				connection = createNewConnection();
			} catch (SQLException e) {
				return null;
			}
			connectionExpand = new ConnectionExpand(connection);
			connections.addLast(connectionExpand);
		}
		connectionExpand.updateLastActive();
		connection = connectionExpand.getConnection();
		return connection;
	}

	private static boolean updatePoolConnection() {
		if (connections == null) {
			connections = new LinkedList<>();
		}
		int currentSize = connections.size();
		int countTriedCreateConnection = MAX_COUNT_TRY_CREATE_CONNECTION;
		while (currentSize < numberConnectionPool) {
			try {
				Connection connection = createNewConnection();
				if (connection != null) {
					connections.addLast(new ConnectionExpand(connection));
					countTriedCreateConnection = MAX_COUNT_TRY_CREATE_CONNECTION;
				}
			} catch (Exception e) {
				if (--countTriedCreateConnection == 0) {
					return false;
				}
				;
			}
			currentSize = connections.size();
		}
		return true;
	}

	private static Connection createNewConnection() throws SQLException {
		return DatabaseManager_.configConnection(DatabaseManager_.getDataSource(isChangeDatabaseConfig).getConnection());
	}

	private static DataSource getDataSource(boolean isCreateNew) {
		if (dataSource != null) {
			if (!isCreateNew) {
				return dataSource;
			}
			dataSource = null;
		}
		dataSource = DatabaseManager_.createNewDataSource(true);
		return dataSource;
	}

	private static DataSource getDataSourceWithOutDatabaseName() {
		return DatabaseManager_.createNewDataSource(false);
	}

	private static Connection configConnection(Connection connection) {
		// TODO config connection
		return connection;
	}

	private static DataSource createNewDataSource(boolean withDatabase) {
		// "jdbc:mysql://%s:%s/%s?user=%s&password=%s&verifyServerCertificate=false&useSSL=true&autoReconnect=true",
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName(host);
		dataSource.setPort(port);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		dataSource.setUseUnicode(true);
		dataSource.setCharacterEncoding("UTF8");
		if (withDatabase) {
			dataSource.setDatabaseName(database);
		}
		// dataSource.setVerifyServerCertificate(false);
		// dataSource.setUseSSL(true);
		// dataSource.setAutoReconnect(true);
		return dataSource;
	}

	/***
	 * Wrapper Connection with time active and optional check valid.
	 */
	private static class ConnectionExpand {
		public ConnectionExpand(Connection connection) {
			this.connection = connection;
			this.lastActiveConnection = System.currentTimeMillis();
		}

		public Connection getConnection() {
			return this.connection;
		}

		public void updateLastActive() {
			this.lastActiveConnection = System.currentTimeMillis();
		}

		public boolean isTimeOut() {
			return (System.currentTimeMillis() - this.lastActiveConnection) > connectionWaitTimeout;
		}

		public boolean isValid() {
			try {
				if (this.connection == null) {
					return false;
				}
				if (this.connection.isClosed()) {
					return false;
				}
				if (this.isTimeOut()) {
					return this.connection.isValid(1);
				}
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		public void close() {
			try {
				this.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		private Connection connection;
		private long lastActiveConnection;
	}
}
