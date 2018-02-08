package nkn6294.java.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

public class DatabasePool {

	public static Connection makeDBConnection() {
		Connection connection = null;
		try {
			System.out.println("makeDBConnection");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					getUrl() + "?useUnicode=true&characterEncoding=UTF-8",
					getMySqlUserName(), getMySqlPassword());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}

	// Phương thức khởi tạo số connections theo biến INI_CONNECTIONS:
	public static void build() {
		Connection connection = null;
		release();
		for (int index = 0; index < getThreadNumber(); index++) {
			connection = makeDBConnection();
			if (connection != null) {
				poolLists.addLast(connection);
			}
		}
	}

	public static Connection getConnection() {
		Connection connection = null;
		try {
			synchronized (poolLists) {
				connection = (Connection) poolLists.removeFirst();
			}
			if (connection == null) {
				connection = makeDBConnection();
			}
			try {
				connection.setAutoCommit(true);
			} catch (Exception ex) {
			}
		} catch (Exception e) {
			try {
				connection = makeDBConnection();
				connection.setAutoCommit(true);
			} catch (SQLException e1) {

			}
		}
		return connection;
	}

	public static void release() {
		synchronized (poolLists) {
			for (Iterator<Connection> iterator = poolLists.iterator(); iterator.hasNext();) {
				Connection connection = (Connection) iterator.next();
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
			poolLists.clear();
		}
	}

	// Phương thức đóng một kết nối có trong pool:
	public static void putConnection(Connection connection) {
		try { // Ignore closed connection
			if (connection == null || connection.isClosed()) {
				return;
			}
			if (poolLists.size() >= getThreadNumber()) {
				connection.close();
				return;
			}
		} catch (SQLException ex) {
		}
		synchronized (poolLists) {
			poolLists.addLast(connection);
			poolLists.notify();
			System.out.println("Size" + poolLists.size());
		}
	}

	public static void releaseConnection(Connection connection, PreparedStatement preparedStatement) {
		System.out.println("releaseConnection");
		putConnection(connection);
		try {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {

		}
	}

	public static void releaseConnection(Connection connection, PreparedStatement preparedStatement,
			ResultSet resultSet) {
		System.out.println("releaseConnection");
		releaseConnection(connection, preparedStatement);
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException e) {
		}
	}

	private static LinkedList<Connection> poolLists = new LinkedList<Connection>();
	private static int INI_CONNECTIONS = 15;
	private static String mysqlUserName = "mysql_username";
	private static String mysqlPassword = "mysql_password";
	
	private static int getThreadNumber() {
		return INI_CONNECTIONS;
	}
	
	private static String getUrl() {
		return "";
	}
	
	private static String getMySqlUserName() {
		return mysqlUserName;
	}
	
	private static String getMySqlPassword() {
		return mysqlPassword;
	}
}