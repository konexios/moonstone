package moonstone.selene.device.udp.udpforwarder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.acs.AcsUtils;
import moonstone.acs.JsonUtils;
import moonstone.acs.KeyValuePair;
import moonstone.selene.data.Telemetry;
import moonstone.selene.device.udp.UdpDataAbstract;
import moonstone.selene.engine.EngineConstants;

public class UdpForwarderData extends UdpDataAbstract {
    private Map<String, String> values = new HashMap<>();

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public IotParameters writeIoTParameters() {
        IotParameters result = new IotParameters();
        result.putAll(values);
        result.setDirty(true);
        return result;
    }

    @Override
    public List<Telemetry> writeTelemetries() {
        List<Telemetry> result = new ArrayList<>(values.size());
        for (Entry<String, String> entry : values.entrySet()) {
            KeyValuePair<TelemetryItemType, String> pair = TelemetryItemType.parse(entry.getKey());
            result.add(writeStringTelemetry(pair.getKey(), pair.getValue(), entry.getValue()));
        }
        return result;
    }

    @Override
    public boolean parseRawData(byte[] data) {
        String method = "parseRawData";
        boolean result = true;
        try {
            String string = String.join("",
                    AcsUtils.streamToLines(new ByteArrayInputStream(data), StandardCharsets.UTF_8));
            Map<String, String> checkedValues = new HashMap<>();
            for (Entry<String, String> entry : JsonUtils.fromJson(string, EngineConstants.MAP_TYPE_REF).entrySet()) {
                if (TelemetryItemType.parse(entry.getKey()) == null) {
                    logWarn(method, "skipping telemetry: unknown field %s", entry.getKey());
                } else {
                    checkedValues.put(entry.getKey(), entry.getValue());
                }
            }
            setValues(checkedValues);
        } catch (IOException e) {
            logError(method, "error during packet parsing", e);
            result = false;
        }
        return result;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }
}
