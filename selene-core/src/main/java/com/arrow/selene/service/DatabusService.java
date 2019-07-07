package com.arrow.selene.service;

import java.util.Locale;

import com.arrow.selene.SeleneProperties;
import com.arrow.selene.databus.Databus;
import com.arrow.selene.databus.DatabusFactory;
import com.arrow.selene.databus.DatabusListener;

public class DatabusService extends ServiceAbstract {
	private Databus databus;

	private static class DatabusHolder extends ServiceAbstract {
		static final DatabusService SINGLETON = new DatabusService();
	}

	public static DatabusService getInstance() {
		return DatabusHolder.SINGLETON;
	}

	private DatabusService() {
		String method = "DatabusService";
		SeleneProperties seleneProperties = ConfigService.getInstance().getSeleneProperties();
		databus = DatabusFactory.createDatabus(seleneProperties.getDatabus().toLowerCase(Locale.getDefault()));
		databus.setMaxBuffer(seleneProperties.getDatabusMaxBuffer());
		logInfo(method, "databus: %s, maxBuffer: %d", databus, databus.getMaxBuffer());
		databus.start();
	}

	public void registerListener(DatabusListener listener, String... queues) {
		databus.registerListener(listener, queues);
	}

	public void send(String queue, byte[] message) {
		databus.send(queue, message);
	}
}
