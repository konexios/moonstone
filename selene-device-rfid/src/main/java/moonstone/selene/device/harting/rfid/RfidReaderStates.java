package moonstone.selene.device.harting.rfid;

import java.util.HashMap;
import java.util.Map;

import moonstone.selene.device.harting.rfid.config.AntennaMultiplexing;
import moonstone.selene.device.harting.rfid.config.Anticollision;
import moonstone.selene.device.harting.rfid.config.ConfigParameter;
import moonstone.selene.device.harting.rfid.config.InputsOutputs;
import moonstone.selene.device.harting.rfid.config.InputsOutputs2;
import moonstone.selene.device.harting.rfid.config.InterfaceAndMode;
import moonstone.selene.device.harting.rfid.config.LanSettings1;
import moonstone.selene.device.harting.rfid.config.LanSettings2;
import moonstone.selene.device.harting.rfid.config.NotificationChannel1;
import moonstone.selene.device.harting.rfid.config.Passwords;
import moonstone.selene.device.harting.rfid.config.PersistenceReset;
import moonstone.selene.device.harting.rfid.config.ReadModeFilter;
import moonstone.selene.device.harting.rfid.config.ReadModeReadData;
import moonstone.selene.device.harting.rfid.config.RfInterface;
import moonstone.selene.device.harting.rfid.config.RfParameter;
import moonstone.selene.device.harting.rfid.config.ScanMode;
import moonstone.selene.device.harting.rfid.config.TransponderParameters;
import moonstone.selene.device.harting.rfid.config.Trigger;
import moonstone.selene.device.harting.rfid.config.UserParameter;
import moonstone.selene.engine.state.DeviceStates;

public class RfidReaderStates extends DeviceStates {
	private static final long serialVersionUID = -6183248241034303271L;
	@SuppressWarnings("rawtypes")
	private Map<Class<? extends ConfigParameter>, ConfigParameter<?>> params = new HashMap<>();
	@SuppressWarnings("rawtypes")
	private Map<String, Class<? extends ConfigParameter>> handlers = new HashMap<>();

	@SuppressWarnings("rawtypes")
	public Map<Class<? extends ConfigParameter>, ConfigParameter<?>> getParams() {
		return params;
	}

	public void initParams() {
		params.put(RfInterface.class, new RfInterface());
		params.put(InterfaceAndMode.class, new InterfaceAndMode());
		params.put(AntennaMultiplexing.class, new AntennaMultiplexing());
		params.put(RfParameter.class, new RfParameter());
		params.put(UserParameter.class, new UserParameter());
		params.put(Anticollision.class, new Anticollision());
		params.put(InputsOutputs.class, new InputsOutputs());
		params.put(InputsOutputs2.class, new InputsOutputs2());
		params.put(LanSettings1.class, new LanSettings1());
		params.put(LanSettings2.class, new LanSettings2());
		params.put(NotificationChannel1.class, new NotificationChannel1());
		params.put(Passwords.class, new Passwords());
		params.put(PersistenceReset.class, new PersistenceReset());
		params.put(ReadModeFilter.class, new ReadModeFilter());
		params.put(ReadModeReadData.class, new ReadModeReadData());
		params.put(ScanMode.class, new ScanMode());
		params.put(TransponderParameters.class, new TransponderParameters());
		params.put(Trigger.class, new Trigger());

		params.forEach((key, value) -> value.getParams().forEach(item -> handlers.put(item, key)));
	}

	@SuppressWarnings("unchecked")
	public <T extends ConfigParameter<T>> T getParam(Class<T> key) {
		return (T) params.get(key);
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Class<? extends ConfigParameter>> getHandlers() {
		return handlers;
	}
}
