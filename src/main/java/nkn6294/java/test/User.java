package nkn6294.java.test;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import nkn6294.java.database.DatabaseInit;
import nkn6294.java.database.DatabaseManager;
import nkn6294.java.lib.util.StringUtils;

public class User {
	
	static {
		DEFAULT_SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
	}
	
	public static void run() {
		DatabaseManager.init("localhost", 3306, "test", "test", "test", 5);
		DatabaseInit.createTable(CREATE_USERS_QUERY);
	}

	public static void createSample() {
		for (int i = 0; i < 100; i++) {
			int day = StringUtils.getRandomInt(1, 30);
			int month = StringUtils.getRandomInt(1, 12);
			int year = StringUtils.getRandomInt(1990, 2016);
			String name = StringUtils.getRandomString(8);
			LocalDate date = LocalDate.of(year, month, day);
			User user = User.createNewUser(name, date);
			System.out.println(user.exportData());
		}
	}

	private final static String CREATE_USERS_QUERY = "CREATE TABLE IF NOT EXISTS `users` ( "
			+ "`id` VARCHAR(32) NOT NULL , " + "`name` VARCHAR(32) NOT NULL DEFAULT 'NO NAME' , "
			+ "`birthDay` DATE NULL DEFAULT NULL , " + "PRIMARY KEY (`id`)) "
			+ "ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
	
	public static final String[] FIELDS = {
		"id", "name", "birthday", 
	};
	
	public static Iterable<User> getModes() {
		try {
			CallableStatement statement = DatabaseManager.getCallableStatement(GET_USERS_QUERY);
			if (statement == null) {
				return null;
			}
			return getModelsByStatement(statement);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static User getModelByID(String id, User data) {
		if (!StringUtils.validateInputString(MAX_LENGTH_DATA, false, id)
				|| id.contains(" ") 
				|| id.contains(",")) {
			return null;
		}
		try {
			CallableStatement statement = DatabaseManager.getCallableStatement(GET_USER_BY_ID_QUERY, id);
			if (statement == null) {
				return null;
			}
			return getOAuthModelByStatement(statement, data);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static boolean deleteByID(String id) {
		if (!StringUtils.validateInputString(MAX_LENGTH_DATA, false, id)
				|| id.contains(" ") 
				|| id.contains(",")) {
			return false;
		}
		CallableStatement statement = DatabaseManager.getCallableStatement(DELETE_USER_BY_ID_QUERY, id);
		if (statement == null) {
			return false;
		}
		try {
			return deleteOrUpdateOrInsertByStatement(statement);
		} catch (Exception e) {
			return false;
		}
	}

	public static User createNewUser(String name, LocalDate birthDay) {
		return new User(StringUtils.getRandomString(MAX_LENGTH_DATA), name, birthDay)
				.save();
	}
	
	public static String exportData(Iterable<User> datas) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		if (datas != null) {
			User item;
			Iterator<User> iterator = datas.iterator();
			while (iterator.hasNext()) {
				item = iterator.next();
				if (item == null) {
					continue;
				}
				String exportItem = item.exportData();
				if (exportItem == null) {
					continue;
				}
				builder.append(exportItem);
				if (iterator.hasNext()) {
					builder.append(",");
				}
			}
		}
		builder.append("]");
		return builder.toString();
	}
	
	public User fetch() {
		User data = User.getModelByID(this.id, this);
		if (data == null) {
			return null;
		}
		this.isChanged = false;
		this.isFetched = true;
		return this;
	}
	
	public User delete() {
		this.isChanged = true;
		if (this.id != null) {
			if (User.deleteByID(this.id)) {
				this.isChanged = false;
				this.isFetched = false;
				return this;
			}
		}
		return null;
	}
	
	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String exportData() {
		if (!validate()) {
			return null;
		}
		return String.format("{\"id\":\"%s\", \"name\":\"%s\",\"birthDay\":\"%s\"}",
				this.id, this.name,
				this.birthDay == null ? "null" : DEFAULT_SIMPLE_DATE_FORMAT.format(this.birthDay));
	}
	protected User(String id, String name, LocalDate birthDay) {
		this.id = id;
		this.birthDay = Date.valueOf(birthDay);
		this.name = name;
		this.isChanged = true;
		this.isFetched = false;
	}
	protected User(String id, String name, Date birthDay) {
		this.id = id;
		this.birthDay = birthDay;
		this.name = name;
		this.isChanged = true;
		this.isFetched = false;
	}
	protected User save() {
		if (this.isFetched && this.isChanged) {
			return this.update();
		}
		User data = User.getModelByID(this.id, null);
		if (data != null) {
			return this.update();
		}
		return this.insert();
	}
	protected User update() {
		if (this.isFetched && !this.isChanged) {
			return this;
		}
		if (!validate()) {
			return null;
		}
		CallableStatement callStatement = DatabaseManager.getCallableStatement(UPDATE_USER_QUERY, this.id, this.name, this.birthDay);
		if (callStatement == null) {
			return null;
		}
		try {
			if (User.deleteOrUpdateOrInsertByStatement(callStatement)) {
				this.isFetched = true;
				this.isChanged = false;
				return this;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	protected User insert() {
		if (!validate()) {
			return null;
		}
		CallableStatement callStatement = DatabaseManager.getCallableStatement(INSERT_USER_QUERY, this.id, this.name, this.birthDay);
		if (callStatement == null) {
			return null;
		}
		try {
			if (deleteOrUpdateOrInsertByStatement(callStatement)) {
				this.isFetched = true;
				this.isChanged = false;
				return this;				
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	protected boolean validate() {
		return StringUtils.validateInputString(MAX_LENGTH_DATA, false, this.id, this.name);			
	}

	private String id;
	private String name;
	private Date birthDay;

	private boolean isFetched;
	private boolean isChanged;
	private static final int MAX_LENGTH_DATA = 32;
	private static final SimpleDateFormat DEFAULT_SIMPLE_DATE_FORMAT;
	
	private static final String GET_USERS_QUERY = "SELECT `id`, `name`, `birthday` FROM `users` ORDER BY `name` DESC";
	private static final String GET_USER_BY_ID_QUERY = "SELECT `id`, `name`, `birthday` FROM `users` WHERE `id` = ? LIMIT 1";
	private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM `users` WHERE `id` = ?";
	private static final String UPDATE_USER_QUERY = "UPDATE `users` SET `id`= ?,`name`= ?,`birthDay`= ? WHERE `authorization_code` = ?";
	private static final String INSERT_USER_QUERY = "INSERT INTO `users`(`id`, `name`, `birthDay`) VALUES (?,?,?)";
	
	private static User getOAuthModelByStatement(CallableStatement statement, User data) {
		ResultSet resultSet = null;
		try {
			if (statement == null) {
				return null;
			}
			if (!statement.execute()) {
				return null;
			}
			resultSet = statement.getResultSet();
			if (!resultSet.next()) {
				return null;
			}
			String id = resultSet.getString("id");
			String name = resultSet.getString("name");
			Date birthDay = resultSet.getDate("birthDay");
			if (data == null) {
				data = new User(id, name, birthDay);
			}
			data.id = id;
			data.birthDay = birthDay;
			return data;
		} catch (Exception e) {
			return null;
		} finally {
			DatabaseManager.closeResourceWithResultSet(resultSet);
		}
	}
	private static Iterable<User> getModelsByStatement(CallableStatement statement) {
		ResultSet resultSet = null;
		try {
			if (!statement.execute()) {
				return null;
			}
			resultSet = statement.getResultSet();
			List<User> datas = new ArrayList<>();
			while (resultSet.next()) {
				String id = resultSet.getString("id");
				String name = resultSet.getString("name");
				Date birthDay = resultSet.getDate("birthDay");
				User data = new User(id, name, birthDay);
				data.id = id;
				data.birthDay = birthDay;
				datas.add(data);
			}
			return datas;
		} catch (Exception e) {
			return null;
		} finally {
			DatabaseManager.closeResourceWithResultSet(resultSet);
		}
	}
	private static boolean deleteOrUpdateOrInsertByStatement(CallableStatement statement) {
		try {
			statement.execute();
			return statement.getUpdateCount() > 0;
		} catch (Exception e) {
			return false;
		} finally {
			DatabaseManager.closeResourceWithStatement(statement);
		}
	}
}
