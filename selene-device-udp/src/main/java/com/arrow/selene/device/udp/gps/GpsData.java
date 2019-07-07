package com.arrow.selene.device.udp.gps;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.acs.AcsUtils;
import com.arrow.selene.SeleneException;
import com.arrow.selene.data.Telemetry;
import com.arrow.selene.device.udp.UdpDataAbstract;
import com.arrow.selene.engine.EngineConstants;
import com.arrow.selene.engine.Utils;

/*
    $GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47
    $GPGGA,215456.00,3259.28475,N,09638.02352,W,1,05,3.96,164.8,M,-25.3,M,,*67

    Where:
         GGA          Global Positioning System Fix Data
         123519       Fix taken at 12:35:19 UTC
         4807.038,N   Latitude 48 deg 07.038' N
         01131.000,E  Longitude 11 deg 31.000' E
         1            Fix quality: 0 = invalid
                                   1 = GPS fix (SPS)
                                   2 = DGPS fix
                                   3 = PPS fix
                       4 = Real Time Kinematic
                       5 = Float RTK
                                   6 = estimated (dead reckoning) (2.3 feature)
                       7 = Manual input mode
                       8 = Simulation mode
         08           Number of satellites being tracked
         0.9          Horizontal dilution of position
         545.4,M      Altitude, Meters, above mean sea level
         46.9,M       Height of geoid (mean sea level) above WGS84
                          ellipsoid
         (empty field) time in seconds since last DGPS update
         (empty field) DGPS station ID number
         *47          the checksum data, always begins with *
*/
public class GpsData extends UdpDataAbstract {

    public enum Quality {
        INVALID,
        GPS_FIX,
        DGPS_FIX,
        PPS_FIX,
        REAL_TIME_KINEMATIC,
        FLOAT_REAL_TIME_KINEMATIC,
        ESTIMATED,
        MANUAL_INPUT_MODE,
        SIMULATION_MODDE
    }

    private String time;
    private double latitude;
    private double longitude;
    private Quality quality;
    private int numSatellites;
    private double dilution;
    private double altitude;
    private double geoidHeight;
    private String placeHolder;
    private String checkSum;

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public IotParameters writeIoTParameters() {
        IotParameters result = new IotParameters();
        if (quality != Quality.INVALID) {
            result.setString("time", time);
            result.setDouble("latitude", latitude, EngineConstants.FORMAT_DECIMAL_8);
            result.setDouble("longitude", longitude, EngineConstants.FORMAT_DECIMAL_8);
            result.setString("quality", quality.name());
            result.setInteger("numSatellites", numSatellites);
            result.setDouble("dilution", dilution, EngineConstants.FORMAT_DECIMAL_2);
            result.setDouble("altitude", altitude, EngineConstants.FORMAT_DECIMAL_2);
            result.setDouble("geoidHeight", geoidHeight, EngineConstants.FORMAT_DECIMAL_2);
        }
        return result;
    }

    @Override
    public List<Telemetry> writeTelemetries() {
        List<Telemetry> result = new ArrayList<>();
        if (quality != Quality.INVALID) {
            result.add(writeStringTelemetry(TelemetryItemType.String, "time", time));
            result.add(writeFloatTelemetry("latitude", Utils.trim8Decimals(latitude)));
            result.add(writeFloatTelemetry("longitude", Utils.trim8Decimals(longitude)));
            result.add(writeStringTelemetry(TelemetryItemType.String, "quality", quality.name()));
            result.add(writeIntTelemetry("numSatellites", (long) numSatellites));
            result.add(writeFloatTelemetry("dilution", Utils.trim2Decimals(dilution)));
            result.add(writeFloatTelemetry("altitude", Utils.trim2Decimals(altitude)));
            result.add(writeFloatTelemetry("geoidHeight", Utils.trim2Decimals(geoidHeight)));
        }
        return result;
    }

    @Override
    public boolean parseRawData(byte[] data) {
        String method = "parseRawData";
        boolean result = false;
        try {
            String gga = null;
            List<String> sentences = AcsUtils.streamToLines(new ByteArrayInputStream(data), StandardCharsets.UTF_8);
            for (String sentence : sentences) {
                if (sentence.startsWith("$GPGGA")) {
                    gga = sentence;
                    break;
                }
            }
            if (StringUtils.isNotEmpty(gga)) {
                logInfo(method, "found GGA sentence: %s", gga);
                String[] tokens = gga.trim().split(",", -1);
                if (tokens.length == 15) {
                    setTime(tokens[1]);
                    setLatitude(parseLat(tokens[2], tokens[3]));
                    setLongitude(parseLon(tokens[4], tokens[5]));
                    setQuality(Quality.values()[Integer.parseInt(tokens[6])]);
                    setNumSatellites(Integer.parseInt(tokens[7]));
                    setDilution(Double.parseDouble(tokens[8]));
                    setAltitude(parseHeight(tokens[9], tokens[10]));
                    setGeoidHeight(parseHeight(tokens[11], tokens[12]));
                    setPlaceHolder(tokens[13]);
                    setCheckSum(tokens[14]);
                    result = true;
                } else {
                    logError(method, "ERROR: expecting 15 tokens, found %d: ", tokens.length, gga);
                }
            } else {
                logError(method, "ERROR: no GGA sentence found, ignoring input data ...");
            }
        } catch (Throwable t) {
            logError(method, "ERROR: while parsing GPS NMEA sentences!", t);
        }
        return result;
    }

    private double parseLat(String field, String hemisphere) {
        int deg = Integer.parseInt(field.substring(0, 2));
        double min = Double.parseDouble(field.substring(2));
        double result = deg + min / 60.0;
        if (StringUtils.equalsIgnoreCase(hemisphere, "S")) {
            result = -result;
        } else if (!StringUtils.equalsIgnoreCase(hemisphere, "N")) {
            throw new SeleneException("invalid hemisphere value for latitude: " + hemisphere);
        }
        return result;
    }

    private double parseLon(String field, String hemisphere) {
        int deg = Integer.parseInt(field.substring(0, 3));
        double min = Double.parseDouble(field.substring(3));
        double result = deg + min / 60.0;
        if (StringUtils.equalsIgnoreCase(hemisphere, "W")) {
            result = -result;
        } else if (!StringUtils.equalsIgnoreCase(hemisphere, "E")) {
            throw new SeleneException("invalid hemisphere value for longitude: " + hemisphere);
        }
        return result;
    }

    private double parseHeight(String field, String unit) {
        double result = Double.parseDouble(field);
        if (StringUtils.equalsIgnoreCase(unit, "M")) {
            result *= 3.28084;
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

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public int getNumSatellites() {
        return numSatellites;
    }

    public void setNumSatellites(int numSatellites) {
        this.numSatellites = numSatellites;
    }

    public double getDilution() {
        return dilution;
    }

    public void setDilution(double dilution) {
        this.dilution = dilution;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getGeoidHeight() {
        return geoidHeight;
    }

    public void setGeoidHeight(double geoidHeight) {
        this.geoidHeight = geoidHeight;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }
}
