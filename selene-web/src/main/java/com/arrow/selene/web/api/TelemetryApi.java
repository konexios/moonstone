package com.arrow.selene.web.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.selene.data.Telemetry;
import com.arrow.selene.web.api.model.TelemetryModels;

@RestController
@RequestMapping("/api/selene/telemetries")
public class TelemetryApi extends BaseApi {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	private static SimpMessagingTemplate staticSimpMessagingTemplate;

	@PostConstruct     
	private void initStaticSimpMessagingTemplate () {
		staticSimpMessagingTemplate = this.simpMessagingTemplate;
	}
	
	//Getter of SimpMessagingTemplate, as it will be utilized by TelemetryService(which will be created upon gateway-startup)
	public static SimpMessagingTemplate getSimpMessagingTemplate() {
		return staticSimpMessagingTemplate;
	}

	@RequestMapping(value = "/{deviceId}/device", method = RequestMethod.GET)
	public List<TelemetryModels.TelemetryList> deviceTelemetry(@PathVariable int deviceId) {

		List<TelemetryModels.TelemetryList> telemetries = new ArrayList<>();

		for (Telemetry telemetry : getTelemetryService().findTelemetryByDeviceId(deviceId))
			telemetries.add(new TelemetryModels.TelemetryList(telemetry));

		if (!telemetries.isEmpty())
			Collections.sort(telemetries, new Comparator<TelemetryModels.TelemetryList>() {
				@Override
				public int compare(TelemetryModels.TelemetryList o1, TelemetryModels.TelemetryList o2) {
					return o1.getTimestamp().compareTo(o2.getTimestamp());
				}
			});

		return telemetries;
	}
}
