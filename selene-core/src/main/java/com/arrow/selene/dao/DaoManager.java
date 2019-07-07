package com.arrow.selene.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.AcsSystemException;
import com.arrow.acs.Loggable;
import com.arrow.selene.SeleneProperties;
import com.arrow.selene.service.ConfigService;

public class DaoManager extends Loggable {
	public static final String TABLE_TELEMETRY = "SELENE_TELEMETRY";
	public static final String TABLE_MESSAGE = "SELENE_MESSAGE";
	public static final String TABLE_IBM = "SELENE_IBM";
	public static final String TABLE_GATEWAY = "SELENE_GATEWAY";
	public static final String TABLE_DEVICE = "SELENE_DEVICE";
	public static final String TABLE_AZURE = "SELENE_AZURE";
	public static final String TABLE_AWS = "SELENE_AWS";
	public static final String TABLE_SEQUENCE = "SELENE_SEQUENCE";

	public static final String TABLE_INFO_TABLE_NAME = "TABLE_NAME";
	public static final String COLUMN_INFO_DATA_TYPE = "DATA_TYPE";
	public static final String COLUMN_INFO_COLUMN_NAME = "COLUMN_NAME";

	private static final String DEFAULT_H2_JDBC_DRIVER = "org.h2.Driver";
	private static final String DEFAULT_SQLITE_JDBC_DRIVER = "org.sqlite.JDBC";
	private static final String DEFAULT_MYSQL_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	private static final class SingletonHolder {
		static final DaoManager SINGLETON = new DaoManager();
	}

	public static DaoManager getInstance() {
		return SingletonHolder.SINGLETON;
	}

	public enum DatabaseType {
		H2, SQLITE, MYSQL, UNKNOWN
	}

	private final DatabaseType databaseType;

	private final SeleneProperties seleneProps;
	private Map<String, TableInfo> tables = new HashMap<>();
	private boolean freshDatabase;

	private String url;
	private String user;
	private String password;
	private String driver;

	private DaoManager() {
		String method = "DaoManager";
		seleneProps = ConfigService.getInstance().getSeleneProperties();

		url = seleneProps.getJdbcUrl();
		if (url.startsWith("jdbc:h2")) {
			databaseType = DatabaseType.H2;
		} else if (url.startsWith("jdbc:sqlite")) {
			databaseType = DatabaseType.SQLITE;
		} else if (url.startsWith("jdbc:mysql")) {
			databaseType = DatabaseType.MYSQL;
		} else {
			databaseType = DatabaseType.UNKNOWN;
		}
		logInfo(method, "jdbcUrl: %s", url);

		driver = seleneProps.getJdbcDriver();
		if (StringUtils.isEmpty(driver)) {
			if (databaseType == DatabaseType.H2) {
				driver = DEFAULT_H2_JDBC_DRIVER;
			} else if (databaseType == DatabaseType.SQLITE) {
				driver = DEFAULT_SQLITE_JDBC_DRIVER;
			} else if (databaseType == DatabaseType.MYSQL) {
				driver = DEFAULT_MYSQL_JDBC_DRIVER;
			} else {
				throw new AcsLogicalException("JDBC driver not defined for UNKNOWN database type");
			}
		}
		logInfo(method, "databaseType: %s, driver: %s", databaseType, driver);

		if (StringUtils.isNotEmpty(driver)) {
			try {
				logInfo(method, "loading JDBC driver: %s", driver);
				Class.forName(driver);
			} catch (Throwable t) {
				logError(method, "ERROR LOADING JDBC DRIVER", t);
			}
		}

		user = seleneProps.getJdbcUser();
		if (StringUtils.isEmpty(user)) {
			// for backward compatible
			user = "sa";
		}
		logInfo(method, "jdbcUser: %s", user);
		password = seleneProps.getJdbcPassword();

		loadSchema();
		freshDatabase = !tables.containsKey(TABLE_GATEWAY);

		if (syncSchema()) {
			// reload if needed
			loadSchema();
		}
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	private void loadSchema() {
		String method = "loadSchema";
		logInfo(method, "...");
		tables.clear();
		execute((DbTask<Void>) connection -> {
			try (ResultSet rset = connection.getMetaData().getTables(null, null, "%", new String[] { "TABLE" })) {
				while (rset.next()) {
					TableInfo info = new TableInfo().populate(rset);
					tables.put(info.getName(), info);
					logDebug(method, "found table: %s", info.getName());
				}
				return null;
			}
		});
		for (TableInfo tableInfo : tables.values()) {
			execute((DbTask<Void>) connection -> {
				try (ResultSet rset = connection.getMetaData().getColumns(null, null, tableInfo.getName(), "%")) {
					while (rset.next()) {
						ColumnInfo columnInfo = new ColumnInfo().populate(rset);
						tableInfo.getColumns().put(columnInfo.getName(), columnInfo);
						logDebug(method, "--> table: %s, column: %s", tableInfo.getName(), columnInfo.getName());
					}
					return null;
				}
			});
		}
	}

	private boolean syncSchema() {
		String method = "syncSchema";
		logInfo(method, "...");
		boolean result = syncTable(TABLE_SEQUENCE, SequenceDao.getInstance());
		result |= syncTable(TABLE_AWS, AwsDao.getInstance());
		result |= syncTable(TABLE_AZURE, AzureDao.getInstance());
		result |= syncTable(TABLE_DEVICE, DeviceDao.getInstance());
		result |= syncTable(TABLE_GATEWAY, GatewayDao.getInstance());
		result |= syncTable(TABLE_IBM, IbmDao.getInstance());
		result |= syncTable(TABLE_MESSAGE, MessageDao.getInstance());
		result |= syncTable(TABLE_TELEMETRY, TelemetryDao.getInstance());
		return result;
	}

	private boolean syncTable(String tableName, DaoAbstract<?> dao) {
		String method = "syncTable";
		boolean updated = false;

		TableInfo tableInfo = tables.get(tableName);
		if (tableInfo == null) {
			// table does not exist - could be new database
			execute((DbTask<Void>) connection -> {
				try (Statement stmt = connection.createStatement()) {
					logInfo(method, "creating new table: %s", tableName);

					String prefix = String.format("create.%s", this.databaseType.toString().toLowerCase());
					String query = dao.getQueries().getProperty(prefix);
					if (StringUtils.isEmpty(query)) {
						prefix = "create";
						query = dao.getQueries().getProperty(prefix);
					}
					stmt.executeUpdate(query);

					// initialize table if required
					for (int i = 1; i <= 10; i++) {
						query = dao.getQueries().getProperty(String.format("create.%d", i));
						if (StringUtils.isNotEmpty(query)) {
							logInfo(method, "initializing table: %s, step: %d", tableName, i);
							stmt.executeUpdate(query);
						} else {
							break;
						}
					}
					return null;
				}
			});
			updated = true;
		} else {
			Map<String, ColumnInfo> existingColumns = tableInfo.getColumns();
			Map<String, String> addColumns = dao.getAddColumnMap();
			for (String column : addColumns.keySet()) {
				if (!existingColumns.containsKey(column)) {
					String command = addColumns.get(column);
					execute((DbTask<Void>) connection -> {
						try (Statement stmt = connection.createStatement()) {
							logInfo(method, "executing DDL command: %s", command);
							stmt.executeUpdate(command);
							return null;
						}
					});
					updated = true;
				}
			}
		}
		return updated;
	}

	public <R> R execute(DbTask<R> task) {
		String method = "execute";
		Validate.notNull(task, "task is null");

		try (Connection connection = getConnection()) {
			return task.execute(connection);
		} catch (Throwable t) {
			logError(method, "execute error", t);
			throw new AcsSystemException("error database execution", t);
		}
	}

	public boolean isFreshDatabase() {
		return freshDatabase;
	}

	static class TableInfo {
		private String name;
		private Map<String, ColumnInfo> columns = new HashMap<>();

		public TableInfo populate(ResultSet rset) throws SQLException {
			setName(rset.getString(TABLE_INFO_TABLE_NAME));
			return this;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Map<String, ColumnInfo> getColumns() {
			return columns;
		}

		public void setColumns(Map<String, ColumnInfo> columns) {
			this.columns = columns;
		}
	}

	static class ColumnInfo {
		private String name;
		private String type;

		public ColumnInfo populate(ResultSet rset) throws SQLException {
			setName(rset.getString(COLUMN_INFO_COLUMN_NAME));
			setType(rset.getString(COLUMN_INFO_DATA_TYPE));
			return this;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
}
