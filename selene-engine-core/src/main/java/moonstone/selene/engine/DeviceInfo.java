package moonstone.selene.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import moonstone.acs.JsonUtils;
import moonstone.selene.SeleneException;

public class DeviceInfo implements Serializable {
    private static final long serialVersionUID = -6339858783115576156L;
    private static final String INFO = "info";

    private String name;
    private String uid;
    private String type;
    private String deviceClass;
    private Map<String, String> info = new LinkedHashMap<>();

    public DeviceInfo populateFrom(Map<String, String> map) {
        name = map.getOrDefault("name", name);
        uid = map.getOrDefault("uid", uid);
        type = map.getOrDefault("type", type);
        deviceClass = map.getOrDefault("deviceClass", deviceClass);
        String value = map.get("info");
        if (StringUtils.isNotEmpty(value)) {
            info = JsonUtils.fromJson(value, Utils.GENERIC_MAP_TYPE_REF);
        }
        return this;
    }

    public Map<String, String> populateTo(Map<String, String> map) {
        if (name != null)
            map.put("name", name);
        if (uid != null)
            map.put("uid", uid);
        if (type != null)
            map.put("type", type);
        if (deviceClass != null)
            map.put("deviceClass", deviceClass);
        if (info != null && info.size() > 0)
            map.put("info", JsonUtils.toJson(info));
        return map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceClass() {
        return deviceClass;
    }

    public void setDeviceClass(String deviceClass) {
        this.deviceClass = deviceClass;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    public Map<String, String> exportInfo() {
        try {
            Map<String, String> map = populateTo(new HashMap<>());
            map.remove(INFO);
            map.putAll(info);
            return map;
        } catch (Exception e) {
            throw new SeleneException("Error exporting info", e);
        }
    }

    public void importInfo(Map<String, String> map) {
        populateFrom(map);
        Map<String, String> copy = populateTo(new HashMap<>());
        copy.keySet().forEach(key -> map.remove(key));
        info.putAll(map);
    }
}
