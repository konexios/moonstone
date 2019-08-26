package moonstone.selene.device.xbee.zcl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import moonstone.selene.device.xbee.zcl.domain.closures.door.attributes.DoorLockClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.closures.door.commands.DoorLockClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.closures.shade.attributes.ShadeClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.closures.shade.commands.ShadeClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.closures.window.attributes.WindowCoveringClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.closures.window.commands.WindowCoveringClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.alarms.attributes.AlarmClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.alarms.commands.AlarmsClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.appliance.attributes.ApplianceControlClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.appliance.commands.ApplianceControlClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.basic.attributes.BasicAnalogInputClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.basic.attributes.BasicAnalogOutputClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.basic.attributes.BasicAnalogValueClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.basic.attributes.BasicBinaryInputClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.basic.attributes.BasicBinaryOutputClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.basic.attributes.BasicBinaryValueClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.basic.attributes.BasicClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.basic.attributes.BasicMultistateInputClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.basic.attributes.BasicMultistateOutputClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.basic.attributes.BasicMultistateValueClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.basic.commands.BasicAnalogInputClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.basic.commands.BasicAnalogOutputClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.basic.commands.BasicAnalogValueClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.basic.commands.BasicBinaryInputClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.basic.commands.BasicBinaryOutputClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.basic.commands.BasicBinaryValueClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.basic.commands.BasicClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.basic.commands.BasicMultistateInputClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.basic.commands.BasicMultistateOutputClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.basic.commands.BasicMultistateValueClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.commissioning.attributes.CommissioningClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.commissioning.commands.CommissioningClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.groups.attributes.GroupsClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.groups.commands.GroupsClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.identify.attributes.IdentifyClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.identify.commands.IdentifyClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.levelcontrol.attributes.LevelControlClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.levelcontrol.commands.LevelControlClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.onoff.attributes.OnOffClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.onoff.commands.OnOffClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.otabootload.attributes.OtaBootloadClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.otabootload.commands.OtaBootloadClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.partition.attributes.PartitionClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.partition.commands.PartitionClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.pollcontrol.attributes.PollControlClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.pollcontrol.commands.PollControlClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.power.attributes.PowerConfigClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.power.commands.PowerClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.powerprofile.attributes.PowerProfileClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.powerprofile.commands.PowerProfileClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.rssilocation.attributes.RssiLocationClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.rssilocation.commands.RssiLocationClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.scenes.attributes.ScenesClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.scenes.commands.ScenesClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.switchconfig.attributes.SwitchConfigClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.switchconfig.commands.SwitchConfigClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.temperature.attributes.TemperatureClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.temperature.commands.TemperatureClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.general.time.attributes.TimeClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.general.time.commands.TimeClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.ha.diagnostics.attributes.DiagnosticsClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.ha.diagnostics.commands.DiagnosticsClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.ha.events.attributes.ApplianceEventsAndAlertClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.ha.events.commands.ApplianceEventsAndAlertClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.ha.identification.attributes.ApplianceIdentificationClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.ha.identification.attributes.MeterIdentificationClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.ha.identification.commands.ApplianceIdentificationClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.ha.identification.commands.MeterIdentificationClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.ha.measurement.attributes.ElectricalMeasurementClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.ha.measurement.commands.ElectricalMeasurementClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.ha.statistics.attributes.ApplianceStatisticsClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.ha.statistics.commands.ApplianceStatisticsClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.hvac.dehumidification.attributes.DehumidificationClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.hvac.dehumidification.commands.DehumidificationClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.hvac.fan.attributes.HvacFanClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.hvac.fan.commands.HvacFanClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.hvac.pump.attributes.HvacPumpClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.hvac.pump.commands.HvacPumpClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.attributes.HvacThermostatClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.commands.HvacThermostatClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.hvac.ui.attributes.HvacUiClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.hvac.ui.commands.HvacUiClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.lighting.ballast.attributes.LightingBallastClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.lighting.ballast.commands.LightingBallastClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.lighting.color.attributes.LightingColorClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.lighting.color.commands.LightingColorClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.measurement.flow.attributes.FlowMeasurementClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.measurement.flow.commands.FlowMeasurementClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.measurement.humidity.attributes.HumidityMeasurementClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.measurement.humidity.commands.HumidityMeasurementClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.measurement.illuminance.attributes.IlluminanceMeasurementClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.measurement.illuminance.commands.IlluminanceMeasurementClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.measurement.levelsensing.attributes.LevelSensingMeasurementClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.measurement.levelsensing.commands.LevelSensingMeasurementClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.measurement.occupancy.attributes.OccupancyMeasurementClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.measurement.occupancy.commands.OccupancyMeasurementClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.measurement.pressure.attributes.PressureMeasurementClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.measurement.pressure.commands.PressureMeasurementClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.measurement.temperature.attributes.TemperatureMeasurementClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.measurement.temperature.commands.TemperatureMeasurementClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.protocol.bacnet.commands.BacnetProtocolTunnelClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.protocol.generic.attributes.GenericTunnelClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.protocol.generic.commands.GenericTunnelClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.protocol.iso11073.attributes.Iso11073ProtocolTunnelClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.protocol.iso11073.commands.Iso11073ProtocolTunnelClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.protocol.iso7816.attributes.Iso7816ProtocolTunnelClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.protocol.iso7816.commands.Iso7816ProtocolTunnelClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.se.demand.attributes.DemandResponseAndLoadControlClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.se.demand.commands.DemandResponseAndLoadControlClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.se.metering.attributes.SimpleMeteringClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.se.metering.commands.SimpleMeteringClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.se.price.attributes.PriceClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.se.price.commands.PriceClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.security.ace.attributes.SecurityAceClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.security.ace.commands.SecurityAceClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.security.wd.attributes.SecurityWdClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.security.wd.commands.SecurityWdClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.security.zone.attributes.SecurityZoneClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.security.zone.commands.SecurityZoneClusterCommands;
import moonstone.selene.device.xbee.zcl.domain.zll.commissioning.attributes.ZllCommissioningClusterAttributes;
import moonstone.selene.device.xbee.zcl.domain.zll.commissioning.commands.ZllCommissioningClusterCommands;

public final class ZclClusters {
	public static final int BASIC = 0x0000;
	public static final int POWER_CONFIG = 0x0001;
	public static final int DEVICE_TEMPERATURE = 0x0002;
	public static final int IDENTIFY = 0x0003;
	public static final int GROUPS = 0x0004;
	public static final int SCENES = 0x0005;
	public static final int ON_OFF = 0x0006;
	public static final int ON_OFF_SWITCH_CONFIG = 0x0007;
	public static final int LEVEL_CONTROL = 0x0008;
	public static final int ALARMS = 0x0009;
	public static final int TIME = 0x000A;
	public static final int RSSI_LOCATION = 0x000B;
	public static final int ANALOG_INPUT_BASIC = 0x000C;
	public static final int ANALOG_OUTPUT_BASIC = 0x000D;
	public static final int ANALOG_VALUE_BASIC = 0x000E;
	public static final int BINARY_INPUT_BASIC = 0x000F;
	public static final int BINARY_OUTPUT_BASIC = 0x0010;
	public static final int BINARY_VALUE_BASIC = 0x0011;
	public static final int MULTISTATE_INPUT_BASIC = 0x0012;
	public static final int MULTISTATE_OUTPUT_BASIC = 0x0013;
	public static final int MULTISTATE_VALUE_BASIC = 0x0014;
	public static final int COMMISSIONING = 0x0015;
	public static final int PARTITION = 0x0016;
	public static final int OTA_BOOTLOAD = 0x0019;
	public static final int POWER_PROFILE = 0x001A;
	public static final int APPLIANCE_CONTROL = 0x001B;
	public static final int POLL_CONTROL = 0x0020;
	public static final int SHADE_CONFIG = 0x0100;
	public static final int DOOR_LOCK = 0x0101;
	public static final int WINDOW_COVERING = 0x0102;
	public static final int PUMP_CONFIG_CONTROL = 0x0200;
	public static final int THERMOSTAT = 0x0201;
	public static final int FAN_CONTROL = 0x0202;
	public static final int DEHUMIDIFICATION_CONTROL = 0x0203;
	public static final int THERMOSTAT_UI_CONFIG = 0x0204;
	public static final int COLOR_CONTROL = 0x0300;
	public static final int BALLAST_CONFIGURATION = 0x0301;
	public static final int ILLUMINANCE_MEASUREMENT = 0x0400;
	public static final int ILLUMINANCE_LEVEL_SENSING = 0x0401;
	public static final int TEMPERATURE_MEASUREMENT = 0x0402;
	public static final int PRESSURE_MEASUREMENT = 0x0403;
	public static final int FLOW_MEASUREMENT = 0x0404;
	public static final int RELATIVE_HUMIDITY_MEASUREMENT = 0x0405;
	public static final int OCCUPANCY_SENSING = 0x0406;
	public static final int IAS_ZONE = 0x0500;
	public static final int IAS_ACE = 0x0501;
	public static final int IAS_WD = 0x0502;
	public static final int GENERIC_TUNNEL = 0x0600;
	public static final int BACNET_PROTOCOL_TUNNEL = 0x0601;
	public static final int ISO11073_PROTOCOL_TUNNEL = 0x0614;
	public static final int ISO7816_PROTOCOL_TUNNEL = 0x0615;
	public static final int PRICE = 0x0700;
	public static final int DEMAND_RESPONSE_LOAD_CONTROL = 0x0701;
	public static final int SIMPLE_METERING = 0x0702;
	public static final int MESSAGING = 0x0703;
	public static final int TUNNELING = 0x0704;
	public static final int PREPAYMENT = 0x0705;
	public static final int ENERGY_MANAGEMENT = 0x0706;
	public static final int TOU_CALENDAR = 0x0707;
	public static final int DEVICE_MANAGEMENT = 0x0708;
	public static final int EVENTS = 0x0709;
	public static final int MDU_PAIRING = 0x070A;
	public static final int KEY_ESTABLISHMENT = 0x0800;
	public static final int INFORMATION = 0x0900;
	public static final int DATA_SHARING = 0x0901;
	public static final int GAMING = 0x0902;
	public static final int DATA_RATE_CONTROL = 0x0903;
	public static final int VOICE_OVER_ZIGBEE = 0x0904;
	public static final int CHATTING = 0x0905;
	public static final int PAYMENT = 0x0A01;
	public static final int BILLING = 0x0A02;
	public static final int APPLIANCE_IDENTIFICATION = 0x0B00;
	public static final int METER_IDENTIFICATION = 0x0B01;
	public static final int APPLIANCE_EVENTS_AND_ALERT = 0x0B02;
	public static final int APPLIANCE_STATISTICS = 0x0B03;
	public static final int ELECTRICAL_MEASUREMENT = 0x0B04;
	public static final int DIAGNOSTICS = 0x0B05;
	public static final int ZLL_COMMISSIONING = 0x1000;

	private static final Map<Integer, ZclClusterInfo> ALL = new HashMap<>();

	static {
		ALL.put(BASIC, new ZclClusterInfo().withId(BASIC).withName("Basic").withAttributes(BasicClusterAttributes.ALL)
				.withCommandsReceived(BasicClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(BasicClusterCommands.ALL_GENERATED));

		ALL.put(POWER_CONFIG, new ZclClusterInfo().withId(POWER_CONFIG).withName("Power Configuration").withAttributes(
				PowerConfigClusterAttributes.ALL).withCommandsReceived(PowerClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(PowerClusterCommands.ALL_GENERATED));

		ALL.put(DEVICE_TEMPERATURE, new ZclClusterInfo().withId(DEVICE_TEMPERATURE).withName("Device Temperature")
				.withAttributes(TemperatureClusterAttributes.ALL)
				.withCommandsReceived(TemperatureClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(TemperatureClusterCommands.ALL_GENERATED));

		ALL.put(IDENTIFY, new ZclClusterInfo().withId(IDENTIFY).withName("Identify").withAttributes(
				IdentifyClusterAttributes.ALL).withCommandsReceived(IdentifyClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(IdentifyClusterCommands.ALL_GENERATED));

		ALL.put(GROUPS, new ZclClusterInfo().withId(GROUPS).withName("Groups").withAttributes(
				GroupsClusterAttributes.ALL).withCommandsReceived(GroupsClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(GroupsClusterCommands.ALL_GENERATED));

		ALL.put(SCENES, new ZclClusterInfo().withId(SCENES).withName("Scenes").withAttributes(
				ScenesClusterAttributes.ALL).withCommandsReceived(ScenesClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(ScenesClusterCommands.ALL_GENERATED));

		ALL.put(ON_OFF, new ZclClusterInfo().withId(ON_OFF).withName("On/Off").withAttributes(
				OnOffClusterAttributes.ALL).withCommandsReceived(OnOffClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(OnOffClusterCommands.ALL_GENERATED));

		ALL.put(ON_OFF_SWITCH_CONFIG, new ZclClusterInfo().withId(ON_OFF_SWITCH_CONFIG).withName(
				"On/Off Switch Configuration").withAttributes(SwitchConfigClusterAttributes.ALL)
				.withCommandsReceived(SwitchConfigClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(SwitchConfigClusterCommands.ALL_GENERATED));

		ALL.put(LEVEL_CONTROL, new ZclClusterInfo().withId(LEVEL_CONTROL).withName("Level Control").withAttributes(
				LevelControlClusterAttributes.ALL).withCommandsReceived(LevelControlClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(LevelControlClusterCommands.ALL_GENERATED));

		ALL.put(ALARMS, new ZclClusterInfo().withId(ALARMS).withName("Alarms").withAttributes(
				AlarmClusterAttributes.ALL).withCommandsReceived(AlarmsClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(AlarmsClusterCommands.ALL_GENERATED));

		ALL.put(TIME, new ZclClusterInfo().withId(TIME).withName("Time").withAttributes(TimeClusterAttributes.ALL)
				.withCommandsReceived(TimeClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(TimeClusterCommands.ALL_GENERATED));

		ALL.put(ANALOG_INPUT_BASIC, new ZclClusterInfo().withId(ANALOG_INPUT_BASIC).withName("Analog Input (Basic)")
				.withAttributes(BasicAnalogInputClusterAttributes.ALL)
				.withCommandsReceived(BasicAnalogInputClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(BasicAnalogInputClusterCommands.ALL_GENERATED));

		ALL.put(ANALOG_OUTPUT_BASIC, new ZclClusterInfo().withId(ANALOG_OUTPUT_BASIC).withName("Analog Output (Basic)")
				.withAttributes(BasicAnalogOutputClusterAttributes.ALL)
				.withCommandsReceived(BasicAnalogOutputClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(BasicAnalogOutputClusterCommands.ALL_GENERATED));

		ALL.put(ANALOG_VALUE_BASIC, new ZclClusterInfo().withId(ANALOG_VALUE_BASIC).withName("Analog Value (Basic)")
				.withAttributes(BasicAnalogValueClusterAttributes.ALL)
				.withCommandsReceived(BasicAnalogValueClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(BasicAnalogValueClusterCommands.ALL_GENERATED));

		ALL.put(BINARY_INPUT_BASIC, new ZclClusterInfo().withId(BINARY_INPUT_BASIC).withName("Binary Input (Basic)")
				.withAttributes(BasicBinaryInputClusterAttributes.ALL)
				.withCommandsReceived(BasicBinaryInputClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(BasicBinaryInputClusterCommands.ALL_GENERATED));

		ALL.put(BINARY_OUTPUT_BASIC, new ZclClusterInfo().withId(BINARY_OUTPUT_BASIC).withName("Binary Output (Basic)")
				.withAttributes(BasicBinaryOutputClusterAttributes.ALL)
				.withCommandsReceived(BasicBinaryOutputClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(BasicBinaryOutputClusterCommands.ALL_GENERATED));

		ALL.put(BINARY_VALUE_BASIC, new ZclClusterInfo().withId(BINARY_VALUE_BASIC).withName("Binary Value (Basic)")
				.withAttributes(BasicBinaryValueClusterAttributes.ALL)
				.withCommandsReceived(BasicBinaryValueClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(BasicBinaryValueClusterCommands.ALL_GENERATED));

		ALL.put(MULTISTATE_INPUT_BASIC, new ZclClusterInfo().withId(MULTISTATE_INPUT_BASIC).withName(
				"Multistate Input (Basic)").withAttributes(BasicMultistateInputClusterAttributes.ALL)
				.withCommandsReceived(BasicMultistateInputClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(BasicMultistateInputClusterCommands.ALL_GENERATED));

		ALL.put(MULTISTATE_OUTPUT_BASIC, new ZclClusterInfo().withId(MULTISTATE_OUTPUT_BASIC).withName(
				"Multistate Output (Basic)").withAttributes(BasicMultistateOutputClusterAttributes.ALL)
				.withCommandsReceived(BasicMultistateOutputClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(BasicMultistateOutputClusterCommands.ALL_GENERATED));

		ALL.put(MULTISTATE_VALUE_BASIC, new ZclClusterInfo().withId(MULTISTATE_VALUE_BASIC).withName(
				"Multistate Value (Basic)").withAttributes(BasicMultistateValueClusterAttributes.ALL)
				.withCommandsReceived(BasicMultistateValueClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(BasicMultistateValueClusterCommands.ALL_GENERATED));

		ALL.put(RSSI_LOCATION, new ZclClusterInfo().withId(RSSI_LOCATION).withName("RSSI Location").withAttributes(
				RssiLocationClusterAttributes.ALL).withCommandsReceived(RssiLocationClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(RssiLocationClusterCommands.ALL_GENERATED));

		ALL.put(COMMISSIONING, new ZclClusterInfo().withId(COMMISSIONING).withName("Commissioning").withAttributes(
				CommissioningClusterAttributes.ALL).withCommandsReceived(CommissioningClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(CommissioningClusterCommands.ALL_GENERATED));

		ALL.put(PARTITION, new ZclClusterInfo().withId(PARTITION).withName("Partition").withAttributes(
				PartitionClusterAttributes.ALL).withCommandsReceived(PartitionClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(PartitionClusterCommands.ALL_GENERATED));

		ALL.put(OTA_BOOTLOAD, new ZclClusterInfo().withId(OTA_BOOTLOAD).withName("OTA Bootload").withAttributes(
				OtaBootloadClusterAttributes.ALL).withCommandsReceived(OtaBootloadClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(OtaBootloadClusterCommands.ALL_GENERATED));

		ALL.put(POWER_PROFILE, new ZclClusterInfo().withId(POWER_PROFILE).withName("Power Profile").withAttributes(
				PowerProfileClusterAttributes.ALL).withCommandsReceived(PowerProfileClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(PowerProfileClusterCommands.ALL_GENERATED));

		ALL.put(APPLIANCE_CONTROL, new ZclClusterInfo().withId(APPLIANCE_CONTROL).withName("Appliance Control")
				.withAttributes(ApplianceControlClusterAttributes.ALL)
				.withCommandsReceived(ApplianceControlClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(ApplianceControlClusterCommands.ALL_GENERATED));

		ALL.put(POLL_CONTROL, new ZclClusterInfo().withId(POLL_CONTROL).withName("Poll Control").withAttributes(
				PollControlClusterAttributes.ALL).withCommandsReceived(PollControlClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(PollControlClusterCommands.ALL_GENERATED));

		ALL.put(SHADE_CONFIG, new ZclClusterInfo().withId(SHADE_CONFIG).withName("Shade Configuration").withAttributes(
				ShadeClusterAttributes.ALL).withCommandsReceived(ShadeClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(ShadeClusterCommands.ALL_GENERATED));

		ALL.put(DOOR_LOCK, new ZclClusterInfo().withId(DOOR_LOCK).withName("Door Lock").withAttributes(
				DoorLockClusterAttributes.ALL).withCommandsReceived(DoorLockClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(DoorLockClusterCommands.ALL_GENERATED));

		ALL.put(WINDOW_COVERING, new ZclClusterInfo().withId(WINDOW_COVERING).withName("Window Covering")
				.withAttributes(WindowCoveringClusterAttributes.ALL)
				.withCommandsReceived(WindowCoveringClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(WindowCoveringClusterCommands.ALL_GENERATED));

		ALL.put(PUMP_CONFIG_CONTROL, new ZclClusterInfo().withId(PUMP_CONFIG_CONTROL).withName(
				"Pump Configuration and Control").withAttributes(HvacPumpClusterAttributes.ALL)
				.withCommandsReceived(HvacPumpClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(HvacPumpClusterCommands.ALL_GENERATED));

		ALL.put(THERMOSTAT, new ZclClusterInfo().withId(THERMOSTAT).withName("Thermostat").withAttributes(
				HvacThermostatClusterAttributes.ALL).withCommandsReceived(HvacThermostatClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(HvacThermostatClusterCommands.ALL_GENERATED));

		ALL.put(FAN_CONTROL, new ZclClusterInfo().withId(FAN_CONTROL).withName("Fan Control").withAttributes(
				HvacFanClusterAttributes.ALL).withCommandsReceived(HvacFanClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(HvacFanClusterCommands.ALL_GENERATED));

		ALL.put(DEHUMIDIFICATION_CONTROL, new ZclClusterInfo().withId(DEHUMIDIFICATION_CONTROL).withName(
				"Dehumidification Control").withAttributes(DehumidificationClusterAttributes.ALL)
				.withCommandsReceived(DehumidificationClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(DehumidificationClusterCommands.ALL_GENERATED));

		ALL.put(THERMOSTAT_UI_CONFIG, new ZclClusterInfo().withId(THERMOSTAT_UI_CONFIG).withName(
				"Thermostat User Interface").withAttributes(HvacUiClusterAttributes.ALL)
				.withCommandsReceived(HvacUiClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(HvacUiClusterCommands.ALL_GENERATED));

		ALL.put(COLOR_CONTROL, new ZclClusterInfo().withId(COLOR_CONTROL).withName("Color Control").withAttributes(
				LightingColorClusterAttributes.ALL).withCommandsReceived(LightingColorClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(LightingColorClusterCommands.ALL_GENERATED));

		ALL.put(BALLAST_CONFIGURATION, new ZclClusterInfo().withId(BALLAST_CONFIGURATION).withName(
				"Ballast Configuration").withAttributes(LightingBallastClusterAttributes.ALL)
				.withCommandsReceived(LightingBallastClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(LightingBallastClusterCommands.ALL_GENERATED));

		ALL.put(ILLUMINANCE_MEASUREMENT, new ZclClusterInfo().withId(ILLUMINANCE_MEASUREMENT).withName(
				"Illuminance Measurement").withAttributes(IlluminanceMeasurementClusterAttributes.ALL)
				.withCommandsReceived(IlluminanceMeasurementClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(IlluminanceMeasurementClusterCommands.ALL_GENERATED));

		ALL.put(ILLUMINANCE_LEVEL_SENSING, new ZclClusterInfo().withId(ILLUMINANCE_LEVEL_SENSING).withName(
				"Illuminance Level Sensing").withAttributes(LevelSensingMeasurementClusterAttributes.ALL)
				.withCommandsReceived(LevelSensingMeasurementClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(LevelSensingMeasurementClusterCommands.ALL_GENERATED));

		ALL.put(TEMPERATURE_MEASUREMENT, new ZclClusterInfo().withId(TEMPERATURE_MEASUREMENT).withName(
				"Temperature Measurement").withAttributes(TemperatureMeasurementClusterAttributes.ALL)
				.withCommandsReceived(TemperatureMeasurementClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(TemperatureMeasurementClusterCommands.ALL_GENERATED));

		ALL.put(PRESSURE_MEASUREMENT, new ZclClusterInfo().withId(PRESSURE_MEASUREMENT).withName(
				"Pressure " + "Measurement").withAttributes(PressureMeasurementClusterAttributes.ALL)
				.withCommandsReceived(PressureMeasurementClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(PressureMeasurementClusterCommands.ALL_GENERATED));

		ALL.put(FLOW_MEASUREMENT, new ZclClusterInfo().withId(FLOW_MEASUREMENT).withName("Flow Measurement")
				.withAttributes(FlowMeasurementClusterAttributes.ALL)
				.withCommandsReceived(FlowMeasurementClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(FlowMeasurementClusterCommands.ALL_GENERATED));

		ALL.put(RELATIVE_HUMIDITY_MEASUREMENT, new ZclClusterInfo().withId(RELATIVE_HUMIDITY_MEASUREMENT).withName(
				"Relative Humidity Measurement").withAttributes(HumidityMeasurementClusterAttributes.ALL)
				.withCommandsReceived(HumidityMeasurementClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(HumidityMeasurementClusterCommands.ALL_GENERATED));

		ALL.put(OCCUPANCY_SENSING, new ZclClusterInfo().withId(OCCUPANCY_SENSING).withName("Occupancy Sensing")
				.withAttributes(OccupancyMeasurementClusterAttributes.ALL)
				.withCommandsReceived(OccupancyMeasurementClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(OccupancyMeasurementClusterCommands.ALL_GENERATED));

		ALL.put(IAS_ZONE, new ZclClusterInfo().withId(IAS_ZONE).withName("IAS Zone").withAttributes(
				SecurityZoneClusterAttributes.ALL).withCommandsReceived(SecurityZoneClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(SecurityZoneClusterCommands.ALL_GENERATED));

		ALL.put(IAS_ACE, new ZclClusterInfo().withId(IAS_ACE).withName("IAS Ancillary Control Equipment")
				.withAttributes(SecurityAceClusterAttributes.ALL)
				.withCommandsReceived(SecurityAceClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(SecurityAceClusterCommands.ALL_GENERATED));

		ALL.put(IAS_WD, new ZclClusterInfo().withId(IAS_WD).withName("IAS Warning Device").withAttributes(
				SecurityWdClusterAttributes.ALL).withCommandsReceived(SecurityWdClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(SecurityWdClusterCommands.ALL_GENERATED));

		ALL.put(GENERIC_TUNNEL, new ZclClusterInfo().withId(GENERIC_TUNNEL).withName("Generic Tunnel").withAttributes(
				GenericTunnelClusterAttributes.ALL).withCommandsReceived(GenericTunnelClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(GenericTunnelClusterCommands.ALL_GENERATED));

		ALL.put(BACNET_PROTOCOL_TUNNEL, new ZclClusterInfo().withId(BACNET_PROTOCOL_TUNNEL).withName(
				"BACnet Protocol Tunnel").withAttributes(Collections.emptyMap())
				.withCommandsReceived(BacnetProtocolTunnelClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(BacnetProtocolTunnelClusterCommands.ALL_GENERATED));

		ALL.put(ISO11073_PROTOCOL_TUNNEL, new ZclClusterInfo().withId(ISO11073_PROTOCOL_TUNNEL).withName(
				"Iso11073 Protocol Tunnel").withAttributes(Iso11073ProtocolTunnelClusterAttributes.ALL)
				.withCommandsReceived(Iso11073ProtocolTunnelClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(Iso11073ProtocolTunnelClusterCommands.ALL_GENERATED));

		ALL.put(ISO7816_PROTOCOL_TUNNEL, new ZclClusterInfo().withId(ISO7816_PROTOCOL_TUNNEL).withName(
				"Iso7816 Protocol Tunnel").withAttributes(Iso7816ProtocolTunnelClusterAttributes.ALL)
				.withCommandsReceived(Iso7816ProtocolTunnelClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(Iso7816ProtocolTunnelClusterCommands.ALL_GENERATED));

		ALL.put(PRICE, new ZclClusterInfo().withId(PRICE).withName("Price").withAttributes(PriceClusterAttributes.ALL)
				.withCommandsReceived(PriceClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(PriceClusterCommands.ALL_GENERATED));

		ALL.put(DEMAND_RESPONSE_LOAD_CONTROL, new ZclClusterInfo().withId(DEMAND_RESPONSE_LOAD_CONTROL).withName(
				"Demand Response Load Control").withAttributes(DemandResponseAndLoadControlClusterAttributes.ALL)
				.withCommandsReceived(DemandResponseAndLoadControlClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(DemandResponseAndLoadControlClusterCommands.ALL_GENERATED));

		ALL.put(SIMPLE_METERING, new ZclClusterInfo().withId(SIMPLE_METERING).withName("Simple Metering")
				.withAttributes(SimpleMeteringClusterAttributes.ALL)
				.withCommandsReceived(SimpleMeteringClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(SimpleMeteringClusterCommands.ALL_GENERATED));

		// not a part of HA
		//		ALL.put(MESSAGING, new ZclClusterInfo().withId(MESSAGING).withName("Messaging").withAttributes(
		//				Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(TUNNELING, new ZclClusterInfo().withId(TUNNELING).withName("Tunneling").withAttributes(
		//				Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(PREPAYMENT, new ZclClusterInfo().withId(PREPAYMENT).withName("Prepayment").withAttributes(
		//				Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(ENERGY_MANAGEMENT, new ZclClusterInfo().withId(ENERGY_MANAGEMENT).withName("Energy Management")
		//				.withAttributes(Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(TOU_CALENDAR, new ZclClusterInfo().withId(TOU_CALENDAR).withName("Time-Of-Use Calendar")
		//				.withAttributes(
		//				Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(DEVICE_MANAGEMENT, new ZclClusterInfo().withId(DEVICE_MANAGEMENT).withName("Device Management")
		//				.withAttributes(Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(EVENTS, new ZclClusterInfo().withId(EVENTS).withName("Events").withAttributes(Collections
		// .emptyMap())
		//				.withCommandsReceived(Collections.emptyMap()).withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(MDU_PAIRING, new ZclClusterInfo().withId(MDU_PAIRING).withName("MDU Pairing").withAttributes(
		//				Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(KEY_ESTABLISHMENT, new ZclClusterInfo().withId(KEY_ESTABLISHMENT).withName("Key Establishment")
		//				.withAttributes(Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(INFORMATION, new ZclClusterInfo().withId(INFORMATION).withName("Information").withAttributes(
		//				Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(DATA_SHARING, new ZclClusterInfo().withId(DATA_SHARING).withName("Data Sharing")
		// .withAttributes(
		//				Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(GAMING, new ZclClusterInfo().withId(GAMING).withName("Gaming").withAttributes(Collections
		// .emptyMap())
		//				.withCommandsReceived(Collections.emptyMap()).withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(DATA_RATE_CONTROL, new ZclClusterInfo().withId(DATA_RATE_CONTROL).withName("Data Rate Control")
		//				.withAttributes(Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(VOICE_OVER_ZIGBEE, new ZclClusterInfo().withId(VOICE_OVER_ZIGBEE).withName("Voice Over ZigBee")
		//				.withAttributes(Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(CHATTING, new ZclClusterInfo().withId(CHATTING).withName("Chating").withAttributes(
		//				Collections.emptyMap()).withCommandsReceived(Collections.emptyMap())
		//				.withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(PAYMENT, new ZclClusterInfo().withId(PAYMENT).withName("Payment").withAttributes(Collections
		//				.emptyMap())
		//				.withCommandsReceived(Collections.emptyMap()).withCommandsGenerated(Collections.emptyMap()));
		//
		//		ALL.put(BILLING, new ZclClusterInfo().withId(BILLING).withName("Billing").withAttributes(Collections
		//				.emptyMap())
		//				.withCommandsReceived(Collections.emptyMap()).withCommandsGenerated(Collections.emptyMap()));
		//
		ALL.put(APPLIANCE_IDENTIFICATION, new ZclClusterInfo().withId(APPLIANCE_IDENTIFICATION).withName(
				"Appliance Identification").withAttributes(ApplianceIdentificationClusterAttributes.ALL)
				.withCommandsReceived(ApplianceIdentificationClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(ApplianceIdentificationClusterCommands.ALL_GENERATED));

		ALL.put(METER_IDENTIFICATION, new ZclClusterInfo().withId(METER_IDENTIFICATION).withName(
				"Meter " + "Identification").withAttributes(MeterIdentificationClusterAttributes.ALL)
				.withCommandsReceived(MeterIdentificationClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(MeterIdentificationClusterCommands.ALL_GENERATED));

		ALL.put(APPLIANCE_EVENTS_AND_ALERT, new ZclClusterInfo().withId(APPLIANCE_EVENTS_AND_ALERT).withName(
				"Appliance Events And Alert").withAttributes(ApplianceEventsAndAlertClusterAttributes.ALL)
				.withCommandsReceived(ApplianceEventsAndAlertClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(ApplianceEventsAndAlertClusterCommands.ALL_GENERATED));

		ALL.put(APPLIANCE_STATISTICS, new ZclClusterInfo().withId(APPLIANCE_STATISTICS).withName(
				"Appliance " + "Statistics").withAttributes(ApplianceStatisticsClusterAttributes.ALL)
				.withCommandsReceived(ApplianceStatisticsClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(ApplianceStatisticsClusterCommands.ALL_GENERATED));

		ALL.put(ELECTRICAL_MEASUREMENT, new ZclClusterInfo().withId(ELECTRICAL_MEASUREMENT).withName(
				"Electrical Measurement").withAttributes(ElectricalMeasurementClusterAttributes.ALL)
				.withCommandsReceived(ElectricalMeasurementClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(ElectricalMeasurementClusterCommands.ALL_GENERATED));

		ALL.put(DIAGNOSTICS, new ZclClusterInfo().withId(DIAGNOSTICS).withName("Diagnostics").withAttributes(
				DiagnosticsClusterAttributes.ALL).withCommandsReceived(DiagnosticsClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(DiagnosticsClusterCommands.ALL_GENERATED));

		ALL.put(ZLL_COMMISSIONING, new ZclClusterInfo().withId(ZLL_COMMISSIONING).withName("ZLL commissioning")
				.withAttributes(ZllCommissioningClusterAttributes.ALL)
				.withCommandsReceived(ZllCommissioningClusterCommands.ALL_RECEIVED)
				.withCommandsGenerated(ZllCommissioningClusterCommands.ALL_GENERATED));
	}

	public static ZclClusterInfo getCluster(Integer id) {
		return ALL.get(id);
	}

	public static String getName(Integer id) {
		ZclClusterInfo info = ALL.get(id);
		return info == null ? "Unknown Cluster" : info.getName();
	}
}
