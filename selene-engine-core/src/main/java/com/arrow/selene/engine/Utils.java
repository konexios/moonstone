package com.arrow.selene.engine;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.lang3.StringUtils;

import com.arrow.acn.client.IotParameters;
import com.arrow.acs.AcsUtils;
import com.arrow.acs.JsonUtils;
import com.arrow.selene.Loggable;
import com.arrow.selene.SeleneException;
import com.arrow.selene.TelemetryUtils;
import com.arrow.selene.data.Device;
import com.arrow.selene.data.Telemetry;
import com.arrow.selene.engine.state.State;
import com.fasterxml.jackson.core.type.TypeReference;

public class Utils {
    private static final Loggable LOGGER = new Loggable();

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final TypeReference<Map<String, String>> GENERIC_MAP_TYPE_REF = new TypeReference<Map<String, String>>() {
    };

    public static Double trim2Decimals(Double value) {
        return Double.parseDouble(createDecimalFormatter("#.##").format(value));
    }

    public static Double trim2Decimals(float value) {
        return Double.parseDouble(createDecimalFormatter("#.##").format(value));
    }

    public static Double trim8Decimals(Double value) {
        return Double.parseDouble(createDecimalFormatter("#.########").format(value));
    }

    public static Double trim8Decimals(float value) {
        return Double.parseDouble(createDecimalFormatter("#.########").format(value));
    }

    private static DecimalFormat createDecimalFormatter(String pattern) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        formatter.applyPattern(pattern);
        return formatter;
    }

    public static String toBinaryString(byte... bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(Integer.toBinaryString(256 + b));
        }
        return sb.toString();
    }

    public static String getRequiredProperty(Properties props, String property) {
        String result = props.getProperty(property);
        if (StringUtils.isEmpty(result)) {
            throw new SeleneException("required property not found: " + property);
        }
        return result;
    }

    public static float celsiusToFahrenheit(float value) {
        return value * 9.0F / 5.0F + 32.0F;
    }

    public static double celsiusToFahrenheit(double value) {
        return value * 9.0D / 5.0D + 32.0D;
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Throwable ignored) {
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (Throwable ignored) {
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Throwable ignored) {
            }
        }
    }

    public static Map<String, String> readIniFile(String file, String section, Map<String, String> map) {
        FileReader reader = null;
        try {
            INIConfiguration cfg = new INIConfiguration();
            reader = new FileReader(file);
            cfg.read(reader);
            SubnodeConfiguration node = cfg.getSection(section);
            node.getKeys().forEachRemaining(p -> map.put(p, node.getString(p)));
        } catch (Throwable t) {
            throw new SeleneException("Unable to read ini file: " + file, t);
        } finally {
            AcsUtils.close(reader);
        }
        return map;
    }

    public static Map<String, String> readPropertyFile(String file, Map<String, String> map) {
        if (StringUtils.isNotEmpty(file) && map != null) {
            Properties props = readPropertyFile(file, new Properties());
            props.stringPropertyNames().forEach(p -> map.put(p, props.getProperty(p)));
        }
        return map;
    }

    public static Properties readPropertyFile(String file, Properties props) {
        if (StringUtils.isNotEmpty(file) && props != null) {
            try (FileInputStream fis = new FileInputStream(new File(file))) {
                props.load(fis);
            } catch (Throwable t) {
                throw new SeleneException("Unable to read property file: " + file, t);
            }
        }
        return props;
    }

    public static String byteToHexString(byte b) {
        return Hex.encodeHexString(new byte[] { b });
    }

    public static void populateDeviceData(DeviceDataAbstract data, Map<String, String> map) {
        String method = "populateDeviceData";
        LOGGER.logInfo(method, "map size: %d", map.size());
        IotParameters iotParameters = new IotParameters();
        iotParameters.putAll(map);
        iotParameters.setDirty(true);
        data.setParsedIotParameters(iotParameters);

        List<Telemetry> list = new ArrayList<>(map.size());
        for (Entry<String, String> entry : map.entrySet()) {
            list.add(TelemetryUtils.withRaw(entry.getKey(), entry.getValue(), data.getTimestamp()));
        }
        data.setParsedTelemetries(list);
    }

    public static Map<String, String> convertStatesToMap(Map<String, State> states) {
        Map<String, String> result = new HashMap<>();
        if (states != null) {
            states.forEach((name, value) -> result.put(name, value.getValue()));
        }
        return result;
    }

    public static Map<String, State> convertMapToStates(Map<String, String> states) {
        Map<String, State> result = new HashMap<>();
        if (states != null) {
            states.forEach((name, value) -> result.put(name, new State().withValue(value)));
        }
        return result;
    }

    public static Properties getProperties(Device device) {
        Properties props = new Properties();
        Map<String, String> info = JsonUtils.fromJson(device.getInfo(), EngineConstants.MAP_TYPE_REF);
        info.entrySet().stream().filter(entry -> entry.getValue() != null)
                .forEach(entry -> props.setProperty(entry.getKey(), entry.getValue()));
        Map<String, String> properties = JsonUtils.fromJson(device.getProperties(), EngineConstants.MAP_TYPE_REF);
        properties.entrySet().stream().filter(entry -> entry.getValue() != null)
                .forEach(entry -> props.setProperty(entry.getKey(), entry.getValue()));
        Map<String, String> states = JsonUtils.fromJson(device.getStates(), EngineConstants.MAP_TYPE_REF);
        states.entrySet().stream().filter(entry -> entry.getValue() != null)
                .forEach(entry -> props.setProperty(entry.getKey(), entry.getValue()));
        return props;
    }

    public static short readSwappedShort(final byte[] data, final int offset) {
        return (short) (((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8));
    }

    public static short readSwappedShort(final InputStream input) throws IOException {
        return (short) (((read(input) & 0xff) << 0) + ((read(input) & 0xff) << 8));
    }

    private static int read(final InputStream input) throws IOException {
        final int value = input.read();
        if (value < 0) {
            throw new EOFException("Unexpected EOF reached");
        }
        return value;
    }
}
