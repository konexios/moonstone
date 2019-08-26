package moonstone.selene.device.libelium;

import java.util.Map;

import moonstone.selene.engine.DeviceProperties;

public class MeshliumProperties extends DeviceProperties {
    private static final long serialVersionUID = -4868360309672063474L;

    public final static String DEFAULT_DB_CONFIG_FILE = "/mnt/lib/cfg/localDB.ini";
    public final static String DEFAULT_PLUGIN_CONFIG_FILE = "/mnt/lib/cfg/arrow/setup.ini";
    public final static Long DEFAULT_SYNC_INTERVAL_MS = 60000L;
    public final static Long DEFAULT_MASK = 1L;
    public final static Integer DEFAULT_LIMIT = 100;
    public final static String DEFAULT_CONNECTION_STRING = "jdbc:mysql://%s:%s/%s?useServerPrepStmts=true&user=%s&password=%s&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private String dbConfigFile = DEFAULT_DB_CONFIG_FILE;
    private String pluginConfigFile = DEFAULT_PLUGIN_CONFIG_FILE;
    private Long syncIntervalMs = DEFAULT_SYNC_INTERVAL_MS;
    private Long mask = DEFAULT_MASK;
    private Integer limit = DEFAULT_LIMIT;
    private String connectionString = DEFAULT_CONNECTION_STRING;

    @Override
    public MeshliumProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        dbConfigFile = map.getOrDefault("dbConfigFile", dbConfigFile);
        pluginConfigFile = map.getOrDefault("pluginConfigFile", pluginConfigFile);
        syncIntervalMs = Long.parseLong(map.getOrDefault("syncIntervalMs", Long.toString(syncIntervalMs)));
        mask = Long.parseLong(map.getOrDefault("mask", Long.toString(mask)));
        limit = Integer.parseInt(map.getOrDefault("limit", Integer.toString(limit)));
        connectionString = map.getOrDefault("connectionString", connectionString);
        return this;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getDbConfigFile() {
        return dbConfigFile;
    }

    public void setDbConfigFile(String dbConfigFile) {
        this.dbConfigFile = dbConfigFile;
    }

    public Long getMask() {
        return mask;
    }

    public void setMask(Long mask) {
        this.mask = mask;
    }

    public String getPluginConfigFile() {
        return pluginConfigFile;
    }

    public void setPluginConfigFile(String pluginConfigFile) {
        this.pluginConfigFile = pluginConfigFile;
    }

    public Long getSyncIntervalMs() {
        return syncIntervalMs;
    }

    public void setSyncIntervalMs(Long syncIntervalMs) {
        this.syncIntervalMs = syncIntervalMs;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
