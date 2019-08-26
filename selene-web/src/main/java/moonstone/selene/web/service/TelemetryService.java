package moonstone.selene.web.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import moonstone.selene.databus.DatabusListenerAbstract;
import moonstone.selene.service.DatabusService;
import moonstone.selene.web.api.TelemetryApi;

public class TelemetryService extends moonstone.selene.service.TelemetryService {

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
