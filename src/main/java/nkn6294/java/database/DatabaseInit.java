package nkn6294.java.database;

import java.sql.Statement;
import nkn6294.java.lib.util.StringUtils;

public class DatabaseInit {
	public static boolean createTableIfNeed() {
		int errors = 0;
		if (!createTestTable()) {
			errors++;
		}
		return errors == 0;
	}
	
	public static boolean createTestTable() {
		return createTable(getCreateTestTableQuery());
	}
	public static boolean updateTable(String query) {
		if (StringUtils.isStringNullOrEmpty(query)) {
			return false;
		}
		Statement statement = null;
		try {
			statement = DatabaseManager.getStatement();
			if (statement == null) {
				throw new Exception("CREATE_STATEMENT_ERROR");
			}
			int result = statement.executeUpdate(query);
			return result >= 0;
		} catch(Exception ex) {
			return false;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception ex) {}
			}
		}
	}
	public static boolean createTable(String query) {
		if (StringUtils.isStringNullOrEmpty(query)) {
			return false;
		}
		Statement statement = null;
		try {
			statement = DatabaseManager.getStatement();
			if (statement == null) {
				throw new Exception("CREATE_STATEMENT_ERROR");
			}
			int result = statement.executeUpdate(query);
			return result >= 0;
		} catch(Exception ex) {
			return false;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception ex) {}
			}
		}
	}
	/*
		CREATE TABLE `test`.`users` ( `id` VARCHAR(32) NOT NULL , `name` VARCHAR(32) NOT NULL DEFAULT 'NO NAME' , `birthDay` TIMESTAMP NULL DEFAULT NULL , PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
	*/
	private static String getCreateTestTableQuery() {
		return "";
	}
}
