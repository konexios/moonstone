package com.arrow.selene.device.conduit;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.arrow.acs.JsonUtils;
import com.arrow.selene.engine.DeviceModuleAbstract;
import com.arrow.selene.model.StatusModel;
import com.fasterxml.jackson.core.type.TypeReference;

public abstract class LoraDeviceModuleAbstract<Info extends LoraDeviceInfo, Props extends LoraDeviceProperties, States extends LoraDeviceStates, Data extends LoraDeviceData>
        extends DeviceModuleAbstract<Info, Props, States, Data> implements LoraDeviceModule {
	private static TypeReference<Map<String, String>> mapTypeRef;
	private LoraServerModule server;

	@Override
	public void processPayload(String payload) {
		queueDataForSending(JsonUtils.fromJson(payload, LoraDeviceData.class));
	}

	@Override
	public StatusModel performCommand(byte... bytes) {
		String method = "performCommand";
		StatusModel result = StatusModel.OK;
		if (server == null) {
			logError(method, "ERROR: LoRa server was not assigned!");
			result.withStatus("ERROR").withMessage("ERROR: LoRa server was not assigned!");
		} else {
			Map<String, String> params = JsonUtils.fromJsonBytes(bytes, getMapTypeRef());
			String data = params.get("data");
			if (StringUtils.isEmpty(data)) {
				logError(method, "payload is missing required field: data!");
				result.withStatus("ERROR").withMessage("payload is missing required field: data!");
			} else {
				getServer().sendDownlinkMessage(getInfo().getUid(), data);
			}
		}
		return result;
	}

	public LoraServerModule getServer() {
		return server;
	}

	public void setServer(LoraServerModule server) {
		this.server = server;
	}

	private static TypeReference<Map<String, String>> getMapTypeRef() {
		return mapTypeRef != null ? mapTypeRef : (mapTypeRef = new TypeReference<Map<String, String>>() {
		});
	}
}
