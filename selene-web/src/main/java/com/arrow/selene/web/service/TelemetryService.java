package com.arrow.selene.web.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.arrow.selene.databus.DatabusListenerAbstract;
import com.arrow.selene.service.DatabusService;
import com.arrow.selene.web.api.TelemetryApi;

public class TelemetryService extends com.arrow.selene.service.TelemetryService {

	private static class SingletonHolder {
		private static final TelemetryService SINGLETON = new TelemetryService();
	}

	public static TelemetryService getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private TelemetryService() {
		super();			
		DatabusService.getInstance().registerListener(new TelemetryListener(getClass().getName()), "selene-telemetry");
	}
	
	public class TelemetryListener extends DatabusListenerAbstract {
		
		private SimpMessagingTemplate simpMessagingTemplate;
		
		TelemetryListener(String name) {
			super(name);
			simpMessagingTemplate = TelemetryApi.getSimpMessagingTemplate();
		}

		@Override
		public void receive(String queue, byte[] message) {

			Map<String, String> params = SerializationUtils.deserialize(message);
			Map<String, String> updatedMap = new HashMap<String, String>();

			params.forEach((key, value) -> updatedMap.put(key.substring(2), value));
			updatedMap.remove("size");
			simpMessagingTemplate.convertAndSend("/topic/telemetry", updatedMap);
		}
	}
}
