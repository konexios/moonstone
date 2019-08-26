package moonstone.selene.device.monnit;

public class GenericSensorInfo extends SensorInfoAbstract {
    private static final long serialVersionUID = 5148999032219884796L;
    public static final String DEFAULT_DEVICE_TYPE = "generic-monnit-sensor";

    public GenericSensorInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }

    public GenericSensorInfo(long sensorId, int profileId, String profileName, String version, String platform,
            double reportInterval) {
        this();
        setSensorId(sensorId);
        setProfileId(profileId);
        setProfileName(profileName);
        setVersion(version);
        setPlatform(platform);
        setReportInterval(reportInterval);
    }
}
