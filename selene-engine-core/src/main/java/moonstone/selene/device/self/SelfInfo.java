package moonstone.selene.device.self;

import java.util.Map;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.VersionModel;
import moonstone.selene.engine.DeviceInfo;

public class SelfInfo extends DeviceInfo {
    //public static final String DEFAULT_DEVICE_TYPE = "gateway";
	public static final String DEFAULT_DEVICE_TYPE = "self";
    private static final long serialVersionUID = -6586224013064209367L;

    private NetworkIface[] networks;
    private VersionModel[] versions;

    private String logLevel;

    public SelfInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }

    @Override
    public SelfInfo populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        String value = map.get("networks");
        if (value != null)
            networks = JsonUtils.fromJson(value, NetworkIface[].class);
        value = map.get("versions");
        if (value != null)
            versions = JsonUtils.fromJson(value, VersionModel[].class);
        logLevel = map.getOrDefault("logLevel", logLevel);
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (networks != null)
            map.put("networks", JsonUtils.toJson(networks));
        if (versions != null)
            map.put("versionModels", JsonUtils.toJson(versions));
        if (logLevel != null)
            map.put("logLevel", logLevel);
        return map;
    }

    public NetworkIface[] getNetworks() {
        return networks;
    }

    public void setNetworks(NetworkIface[] networks) {
        this.networks = networks;
    }

    public VersionModel[] getVersions() {
        return versions;
    }

    public void setVersions(VersionModel[] versions) {
        this.versions = versions;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
}
