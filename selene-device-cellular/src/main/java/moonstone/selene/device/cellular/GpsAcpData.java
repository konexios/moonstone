package moonstone.selene.device.cellular;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.acs.AcsUtils;
import moonstone.selene.SeleneException;
import moonstone.selene.data.Telemetry;
import moonstone.selene.engine.DeviceDataAbstract;
import moonstone.selene.engine.EngineConstants;
import moonstone.selene.engine.Utils;

/*
    AT$GPSACP AT command is used to get the GPS position.

    $GPSACP: <UTC>,<latitude>,<longitude>,<hdop>,<altitude>,<fix>,<cog>,<spkm>,<spkn>,<date>,<nsat>
    $GPSACP: 214151.529,3259.2750N,09638.0165W,1.00,156.0,3,180.06,0.40,0.22,201016,08
*/
public class GpsAcpData extends DeviceDataAbstract {

    private static final int NUM_TOKENS = 11;

    public enum Quality {
        INVALID, GPS_FIX, DGPS_FIX, PPS_FIX, REAL_TIME_KINEMATIC, FLOAT_REAL_TIME_KINEMATIC, ESTIMATED, MANUAL_INPUT_MODE, SIMULATION_MODDE
    }

    private String time;
    private Double latitude;
    private Double longitude;
    private Double hdop;
    private Double altitude;
    private Integer fix;
    private Double cog;
    private Double spkm;
    private Double spkn;
    private String date;
    private Integer numSatellites;
    private String error;

    @Override
    public IotParameters writeIoTParameters() {
        IotParameters result = new IotParameters();
        if (fix != null && fix > 0) {
            if (StringUtils.isNotEmpty(time)) {
                result.setString("time", time);
            }
            if (latitude != null) {
                result.setDouble("latitude", latitude, EngineConstants.FORMAT_DECIMAL_8);
            }
            if (longitude != null) {
                result.setDouble("longitude", longitude, EngineConstants.FORMAT_DECIMAL_8);
            }
            if (hdop != null) {
                result.setDouble("hdop", hdop, EngineConstants.FORMAT_DECIMAL_2);
            }
            if (altitude != null) {
                result.setDouble("altitude", altitude, EngineConstants.FORMAT_DECIMAL_2);
            }
            if (fix != null) {
                result.setInteger("fix", fix);
            }
            if (cog != null) {
                result.setDouble("cog", cog, EngineConstants.FORMAT_DECIMAL_2);
            }
            if (spkm != null) {
                result.setDouble("spkm", spkm, EngineConstants.FORMAT_DECIMAL_2);
            }
            if (spkn != null) {
                result.setDouble("spkn", spkn, EngineConstants.FORMAT_DECIMAL_2);
            }
            if (StringUtils.isNotEmpty(date)) {
                result.setString("date", date);
            }
            if (numSatellites != null) {
                result.setInteger("numSatellites", numSatellites);
            }
        } else {
            error = "No GPS";
        }
        if (error != null) {
            result.setString("error", error);
        }
        return result;
    }

    @Override
    public List<Telemetry> writeTelemetries() {
        List<Telemetry> result = new ArrayList<>();
        if (fix != null && fix > 0) {
            if (StringUtils.isNotEmpty(time)) {
                result.add(writeStringTelemetry(TelemetryItemType.String, "time", time));
            }
            if (latitude != null) {
                result.add(writeFloatTelemetry("latitude", Utils.trim8Decimals(latitude)));
            }
            if (longitude != null) {
                result.add(writeFloatTelemetry("longitude", Utils.trim8Decimals(longitude)));
            }
            if (hdop != null) {
                result.add(writeFloatTelemetry("hdop", Utils.trim2Decimals(hdop)));
            }
            if (altitude != null) {
                result.add(writeFloatTelemetry("altitude", Utils.trim2Decimals(altitude)));
            }
            if (fix != null) {
                result.add(writeIntTelemetry("fix", Long.valueOf(fix)));
            }
            if (cog != null) {
                result.add(writeFloatTelemetry("cog", Utils.trim2Decimals(cog)));
            }
            if (spkm != null) {
                result.add(writeFloatTelemetry("spkm", Utils.trim2Decimals(spkm)));
            }
            if (spkn != null) {
                result.add(writeFloatTelemetry("spkn", Utils.trim2Decimals(spkn)));
            }
            if (StringUtils.isNotEmpty(date)) {
                result.add(writeStringTelemetry(TelemetryItemType.String, "date", date));
            }
            if (numSatellites != null) {
                result.add(writeIntTelemetry("numSatellites", Long.valueOf(numSatellites)));
            }
        } else {
            error = "No GPS";
        }
        if (error != null) {
            result.add(writeStringTelemetry(TelemetryItemType.String, "error", error));
        }
        return result;
    }

    public boolean parseRawData(byte[] data) {
        String method = "parseRawData";
        boolean result = false;
        try {
            String gga = null;
            List<String> sentences = AcsUtils.streamToLines(new ByteArrayInputStream(data), StandardCharsets.UTF_8);
            for (String sentence : sentences) {
                if (sentence.startsWith("$GPSACP:")) {
                    gga = sentence;
                    break;
                }
            }
            if (StringUtils.isNotEmpty(gga)) {
                logInfo(method, "found GGA sentence: %s", gga);
                String[] tokens = gga.substring(8).trim().split(",", -1);
                if (tokens.length == NUM_TOKENS) {
                    setTime(tokens[0]);
                    if (StringUtils.isNotEmpty(tokens[1])) {
                        setLatitude(parseLat(tokens[1]));
                    }
                    if (StringUtils.isNotEmpty(tokens[2])) {
                        setLongitude(parseLon(tokens[2]));
                    }
                    if (StringUtils.isNotEmpty(tokens[3])) {
                        setHdop(Double.parseDouble(tokens[3]));
                    }
                    if (StringUtils.isNotEmpty(tokens[4])) {
                        setAltitude(Double.parseDouble(tokens[4]));
                    }
                    if (StringUtils.isNotEmpty(tokens[5])) {
                        setFix(Integer.parseInt(tokens[5]));
                    }
                    if (StringUtils.isNotEmpty(tokens[6])) {
                        setCog(Double.parseDouble(tokens[6]));
                    }
                    if (StringUtils.isNotEmpty(tokens[7])) {
                        setSpkm(Double.parseDouble(tokens[7]));
                    }
                    if (StringUtils.isNotEmpty(tokens[8])) {
                        setSpkn(Double.parseDouble(tokens[8]));
                    }
                    setDate(tokens[9]);
                    if (StringUtils.isNotEmpty(tokens[10])) {
                        setNumSatellites(Integer.parseInt(tokens[10]));
                    }
                    result = true;
                } else {
                    logError(method, "ERROR: expecting %d tokens, found %d: ", NUM_TOKENS, tokens.length);
                }
            } else {
                logError(method, "ERROR: no GGA sentence found, ignoring input data ...");
            }
        } catch (Throwable t) {
            logError(method, "ERROR: while parsing GPS NMEA sentences!", t);
        }
        return result;
    }

    public boolean containGpsData() {
        return this.longitude != null && this.latitude != null;
    }

    private double parseLat(String field) {
        int deg = Integer.parseInt(field.substring(0, 2));
        double min = Double.parseDouble(field.substring(2, field.length() - 2));
        double result = deg + min / 60.0;
        String hemisphere = field.substring(field.length() - 1);
        if (StringUtils.equalsIgnoreCase(hemisphere, "S")) {
            result = -result;
        } else if (!StringUtils.equalsIgnoreCase(hemisphere, "N")) {
            throw new SeleneException("invalid hemisphere value for latitude: " + hemisphere);
        }
        return result;
    }

    private double parseLon(String field) {
        int deg = Integer.parseInt(field.substring(0, 3));
        double min = Double.parseDouble(field.substring(3, field.length() - 2));
        double result = deg + min / 60.0;
        String hemisphere = field.substring(field.length() - 1);
        if (StringUtils.equalsIgnoreCase(hemisphere, "W")) {
            result = -result;
        } else if (!StringUtils.equalsIgnoreCase(hemisphere, "E")) {
            throw new SeleneException("invalid hemisphere value for longitude: " + hemisphere);
        }
        return result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getHdop() {
        return hdop;
    }

    public void setHdop(double hdop) {
        this.hdop = hdop;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public int getFix() {
        return fix;
    }

    public void setFix(int fix) {
        this.fix = fix;
    }

    public double getCog() {
        return cog;
    }

    public void setCog(double cog) {
        this.cog = cog;
    }

    public double getSpkm() {
        return spkm;
    }

    public void setSpkm(double spkm) {
        this.spkm = spkm;
    }

    public double getSpkn() {
        return spkn;
    }

    public void setSpkn(double spkn) {
        this.spkn = spkn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumSatellites() {
        return numSatellites;
    }

    public void setNumSatellites(int numSatellites) {
        this.numSatellites = numSatellites;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
