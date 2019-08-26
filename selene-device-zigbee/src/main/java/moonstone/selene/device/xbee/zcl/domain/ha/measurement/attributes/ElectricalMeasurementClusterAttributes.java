package moonstone.selene.device.xbee.zcl.domain.ha.measurement.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class ElectricalMeasurementClusterAttributes {
	public static final int MEASUREMENT_TYPE_ATTRIBUTE_ID = 0x0000;
	public static final int DC_VOLTAGE_ATTRIBUTE_ID = 0x0100;
	public static final int DC_VOLTAGE_MIN_ATTRIBUTE_ID = 0x0101;
	public static final int DC_VOLTAGE_MAX_ATTRIBUTE_ID = 0x0102;
	public static final int DC_CURRENT_ATTRIBUTE_ID = 0x0103;
	public static final int DC_CURRENT_MIN_ATTRIBUTE_ID = 0x0104;
	public static final int DC_CURRENT_MAX_ATTRIBUTE_ID = 0x0105;
	public static final int DC_POWER_ATTRIBUTE_ID = 0x0106;
	public static final int DC_POWER_MIN_ATTRIBUTE_ID = 0x0107;
	public static final int DC_POWER_MAX_ATTRIBUTE_ID = 0x0108;
	public static final int DC_VOLTAGE_MULTIPLIER_ATTRIBUTE_ID = 0x0200;
	public static final int DC_VOLTAGE_DIVISOR_ATTRIBUTE_ID = 0x0201;
	public static final int DC_CURRENT_MULTIPLIER_ATTRIBUTE_ID = 0x0202;
	public static final int DC_CURRENT_DIVISOR_ATTRIBUTE_ID = 0x0203;
	public static final int DC_POWER_MULTIPLIER_ATTRIBUTE_ID = 0x0204;
	public static final int DC_POWER_DIVISOR_ATTRIBUTE_ID = 0x0205;
	public static final int AC_FREQUENCY_ATTRIBUTE_ID = 0x0300;
	public static final int AC_FREQUENCY_MIN_ATTRIBUTE_ID = 0x0301;
	public static final int AC_FREQUENCY_MAX_ATTRIBUTE_ID = 0x0302;
	public static final int NEUTRAL_CURRENT_ATTRIBUTE_ID = 0x0303;
	public static final int TOTAL_ACTIVE_POWER_ATTRIBUTE_ID = 0x0304;
	public static final int TOTAL_REACTIVE_POWER_ATTRIBUTE_ID = 0x0305;
	public static final int TOTAL_APPARENT_POWER_ATTRIBUTE_ID = 0x0306;
	public static final int MEASURED_1ST_HARMONIC_CURRENT_ATTRIBUTE_ID = 0x0307;
	public static final int MEASURED_3RD_HARMONIC_CURRENT_ATTRIBUTE_ID = 0x0308;
	public static final int MEASURED_5TH_HARMONIC_CURRENT_ATTRIBUTE_ID = 0x0309;
	public static final int MEASURED_7TH_HARMONIC_CURRENT_ATTRIBUTE_ID = 0x030A;
	public static final int MEASURED_9TH_HARMONIC_CURRENT_ATTRIBUTE_ID = 0x030B;
	public static final int MEASURED_11TH_HARMONIC_CURRENT_ATTRIBUTE_ID = 0x030C;
	public static final int MEASURED_PHASE_1ST_HARMONIC_CURRENT_ATTRIBUTE_ID = 0x030D;
	public static final int MEASURED_PHASE_3RD_HARMONIC_CURRENT_ATTRIBUTE_ID = 0x030E;
	public static final int MEASURED_PHASE_5TH_HARMONIC_CURRENT_ATTRIBUTE_ID = 0x030F;
	public static final int MEASURED_PHASE_7TH_HARMONIC_CURRENT_ATTRIBUTE_ID = 0x0310;
	public static final int MEASURED_PHASE_9TH_HARMONIC_CURRENT_ATTRIBUTE_ID = 0x0311;
	public static final int MEASURED_PHASE_11TH_HARMONIC_CURRENT_ATTRIBUTE_ID = 0x0312;
	public static final int AC_FREQUENCY_MULTIPLIER_ATTRIBUTE_ID = 0x0400;
	public static final int AC_FREQUENCY_DIVISOR_ATTRIBUTE_ID = 0x0401;
	public static final int POWER_MULTIPLIER_ATTRIBUTE_ID = 0x0402;
	public static final int POWER_DIVISOR_ATTRIBUTE_ID = 0x0403;
	public static final int HARMONIC_CURRENT_MULTIPLIER_ATTRIBUTE_ID = 0x0404;
	public static final int PHASE_HARMONIC_CURRENT_MULTIPLIER_ATTRIBUTE_ID = 0x0405;
	public static final int INSTANTANEOUS_VOLTAGE_ATTRIBUTE_ID = 0x0500;
	public static final int INSTANTANEOUS_LINE_CURRENT_ATTRIBUTE_ID = 0x0501;
	public static final int INSTANTANEOUS_ACTIVE_CURRENT_ATTRIBUTE_ID = 0x0502;
	public static final int INSTANTANEOUS_REACTIVE_CURRENT_ATTRIBUTE_ID = 0x0503;
	public static final int INSTANTANEOUS_POWER_ATTRIBUTE_ID = 0x0504;
	public static final int RMS_VOLTAGE_ATTRIBUTE_ID = 0x0505;
	public static final int RMS_VOLTAGE_MIN_ATTRIBUTE_ID = 0x0506;
	public static final int RMS_VOLTAGE_MAX_ATTRIBUTE_ID = 0x0507;
	public static final int RMS_CURRENT_ATTRIBUTE_ID = 0x0508;
	public static final int RMS_CURRENT_MIN_ATTRIBUTE_ID = 0x0509;
	public static final int RMS_CURRENT_MAX_ATTRIBUTE_ID = 0x050A;
	public static final int ACTIVE_POWER_ATTRIBUTE_ID = 0x050B;
	public static final int ACTIVE_POWER_MIN_ATTRIBUTE_ID = 0x050C;
	public static final int ACTIVE_POWER_MAX_ATTRIBUTE_ID = 0x050D;
	public static final int REACTIVE_POWER_ATTRIBUTE_ID = 0x050E;
	public static final int APPARENT_POWER_ATTRIBUTE_ID = 0x050F;
	public static final int POWER_FACTOR_ATTRIBUTE_ID = 0x0510;
	public static final int AVERAGE_RMS_VOLTAGE_MEASUREMENT_PERIOD_ATTRIBUTE_ID = 0x0511;
	public static final int AVERAGE_RMS_OVER_VOLTAGE_COUNTER_ATTRIBUTE_ID = 0x0512;
	public static final int AVERAGE_RMS_UNDER_VOLTAGE_COUNTER_ATTRIBUTE_ID = 0x0513;
	public static final int RMS_EXTREME_OVER_VOLTAGE_PERIOD_ATTRIBUTE_ID = 0x0514;
	public static final int RMS_EXTREME_UNDER_VOLTAGE_PERIOD_ATTRIBUTE_ID = 0x0515;
	public static final int RMS_VOLTAGE_SAG_PERIOD_ATTRIBUTE_ID = 0x0516;
	public static final int RMS_VOLTAGE_SWELL_PERIOD_ATTRIBUTE_ID = 0x0517;
	public static final int AC_VOLTAGE_MULTIPLIER_ATTRIBUTE_ID = 0x0600;
	public static final int AC_VOLTAGE_DIVISOR_ATTRIBUTE_ID = 0x0601;
	public static final int AC_CURRENT_MULTIPLIER_ATTRIBUTE_ID = 0x0602;
	public static final int AC_CURRENT_DIVISOR_ATTRIBUTE_ID = 0x0603;
	public static final int AC_POWER_MULTIPLIER_ATTRIBUTE_ID = 0x0604;
	public static final int AC_POWER_DIVISOR_ATTRIBUTE_ID = 0x0605;
	public static final int DC_OVERLOAD_ALARMS_MASK_ATTRIBUTE_ID = 0x0700;
	public static final int DC_VOLTAGE_OVERLOAD_ATTRIBUTE_ID = 0x0701;
	public static final int DC_CURRENT_OVERLOAD_ATTRIBUTE_ID = 0x0702;
	public static final int AC_ALARMS_MASK_ATTRIBUTE_ID = 0x0800;
	public static final int AC_VOLTAGE_OVERLOAD_ATTRIBUTE_ID = 0x0801;
	public static final int AC_CURRENT_OVERLOAD_ATTRIBUTE_ID = 0x0802;
	public static final int AC_POWER_OVERLOAD_ATTRIBUTE_ID = 0x0803;
	public static final int AC_REACTIVE_POWER_OVERLOAD_ATTRIBUTE_ID = 0x0804;
	public static final int AVERAGE_RMS_OVER_VOLTAGE_ATTRIBUTE_ID = 0x0805;
	public static final int AVERAGE_RMS_UNDER_VOLTAGE_ATTRIBUTE_ID = 0x0806;
	public static final int RMS_EXTREME_OVER_VOLTAGE_ATTRIBUTE_ID = 0x0807;
	public static final int RMS_EXTREME_UNDER_VOLTAGE_ATTRIBUTE_ID = 0x0808;
	public static final int RMS_VOLTAGE_SAG_ATTRIBUTE_ID = 0x0809;
	public static final int RMS_VOLTAGE_SWELL_ATTRIBUTE_ID = 0x080A;
	public static final int LINE_CURRENT_PHASE_B_ATTRIBUTE_ID = 0x0901;
	public static final int ACTIVE_CURRENT_PHASE_B_ATTRIBUTE_ID = 0x0902;
	public static final int REACTIVE_CURRENT_PHASE_B_ATTRIBUTE_ID = 0x0903;
	public static final int RMS_VOLTAGE_PHASE_B_ATTRIBUTE_ID = 0x0905;
	public static final int RMS_VOLTAGE_MIN_PHASE_B_ATTRIBUTE_ID = 0x0906;
	public static final int RMS_VOLTAGE_MAX_PHASE_B_ATTRIBUTE_ID = 0x0907;
	public static final int RMS_CURRENT_PHASE_B_ATTRIBUTE_ID = 0x0908;
	public static final int RMS_CURRENT_MIN_PHASE_B_ATTRIBUTE_ID = 0x0909;
	public static final int RMS_CURRENT_MAX_PHASE_B_ATTRIBUTE_ID = 0x090A;
	public static final int ACTIVE_POWER_PHASE_B_ATTRIBUTE_ID = 0x090B;
	public static final int ACTIVE_POWER_MIN_PHASE_B_ATTRIBUTE_ID = 0x090C;
	public static final int ACTIVE_POWER_MAX_PHASE_B_ATTRIBUTE_ID = 0x090D;
	public static final int REACTIVE_POWER_PHASE_B_ATTRIBUTE_ID = 0x090E;
	public static final int APPARENT_POWER_PHASE_B_ATTRIBUTE_ID = 0x090F;
	public static final int POWER_FACTOR_PHASE_B_ATTRIBUTE_ID = 0x0910;
	public static final int AVERAGE_RMS_VOLTAGE_MEASUREMENT_PERIOD_PHASE_B_ATTRIBUTE_ID = 0x0911;
	public static final int AVERAGE_RMS_OVER_VOLTAGE_COUNTER_PHASE_B_ATTRIBUTE_ID = 0x0912;
	public static final int AVERAGE_RMS_UNDER_VOLTAGE_COUNTER_PHASE_B_ATTRIBUTE_ID = 0x0913;
	public static final int RMS_EXTREME_OVER_VOLTAGE_PERIOD_PHASE_B_ATTRIBUTE_ID = 0x0914;
	public static final int RMS_EXTREME_UNDER_VOLTAGE_PERIOD_PHASE_B_ATTRIBUTE_ID = 0x0915;
	public static final int RMS_VOLTAGE_SAG_PERIOD_PHASE_B_ATTRIBUTE_ID = 0x0916;
	public static final int RMS_VOLTAGE_SWELL_PERIOD_PHASE_B_ATTRIBUTE_ID = 0x0917;
	public static final int LINE_CURRENT_PHASE_C_ATTRIBUTE_ID = 0x0A01;
	public static final int ACTIVE_CURRENT_PHASE_C_ATTRIBUTE_ID = 0x0A02;
	public static final int REACTIVE_CURRENT_PHASE_C_ATTRIBUTE_ID = 0x0A03;
	public static final int RMS_VOLTAGE_PHASE_C_ATTRIBUTE_ID = 0x0A05;
	public static final int RMS_VOLTAGE_MIN_PHASE_C_ATTRIBUTE_ID = 0x0A06;
	public static final int RMS_VOLTAGE_MAX_PHASE_C_ATTRIBUTE_ID = 0x0A07;
	public static final int RMS_CURRENT_PHASE_C_ATTRIBUTE_ID = 0x0A08;
	public static final int RMS_CURRENT_MIN_PHASE_C_ATTRIBUTE_ID = 0x0A09;
	public static final int RMS_CURRENT_MAX_PHASE_C_ATTRIBUTE_ID = 0x0A0A;
	public static final int ACTIVE_POWER_PHASE_C_ATTRIBUTE_ID = 0x0A0B;
	public static final int ACTIVE_POWER_MIN_PHASE_C_ATTRIBUTE_ID = 0x0A0C;
	public static final int ACTIVE_POWER_MAX_PHASE_C_ATTRIBUTE_ID = 0x0A0D;
	public static final int REACTIVE_POWER_PHASE_C_ATTRIBUTE_ID = 0x0A0E;
	public static final int APPARENT_POWER_PHASE_C_ATTRIBUTE_ID = 0x0A0F;
	public static final int POWER_FACTOR_PHASE_C_ATTRIBUTE_ID = 0x0A10;
	public static final int AVERAGE_RMS_VOLTAGE_MEASUREMENT_PERIOD_PHASE_C_ATTRIBUTE_ID = 0x0A11;
	public static final int AVERAGE_RMS_OVER_VOLTAGE_COUNTER_PHASE_C_ATTRIBUTE_ID = 0x0A12;
	public static final int AVERAGE_RMS_UNDER_VOLTAGE_COUNTER_PHASE_C_ATTRIBUTE_ID = 0x0A13;
	public static final int RMS_EXTREME_OVER_VOLTAGE_PERIOD_PHASE_C_ATTRIBUTE_ID = 0x0A14;
	public static final int RMS_EXTREME_UNDER_VOLTAGE_PERIOD_PHASE_C_ATTRIBUTE_ID = 0x0A15;
	public static final int RMS_VOLTAGE_SAG_PERIOD_PHASE_C_ATTRIBUTE_ID = 0x0A16;
	public static final int RMS_VOLTAGE_SWELL_PERIOD_PHASE_C_ATTRIBUTE_ID = 0x0A17;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(MEASUREMENT_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("Measurement Type", MeasurementType
				.DC_MEASUREMENT));
		ALL.put(DC_VOLTAGE_ATTRIBUTE_ID, new ImmutablePair<>("DC Voltage", null));
		ALL.put(DC_VOLTAGE_MIN_ATTRIBUTE_ID, new ImmutablePair<>("DC Voltage Minimum", null));
		ALL.put(DC_VOLTAGE_MAX_ATTRIBUTE_ID, new ImmutablePair<>("DC Voltage Maximum", null));
		ALL.put(DC_CURRENT_ATTRIBUTE_ID, new ImmutablePair<>("DC Current", null));
		ALL.put(DC_CURRENT_MIN_ATTRIBUTE_ID, new ImmutablePair<>("DC Current Minimum", null));
		ALL.put(DC_CURRENT_MAX_ATTRIBUTE_ID, new ImmutablePair<>("DC Current Maximum", null));
		ALL.put(DC_POWER_ATTRIBUTE_ID, new ImmutablePair<>("DC Power", null));
		ALL.put(DC_POWER_MIN_ATTRIBUTE_ID, new ImmutablePair<>("DC Power Minimum", null));
		ALL.put(DC_POWER_MAX_ATTRIBUTE_ID, new ImmutablePair<>("DC Power Maximum", null));
		ALL.put(DC_VOLTAGE_MULTIPLIER_ATTRIBUTE_ID, new ImmutablePair<>("DC Voltage Multiplier", null));
		ALL.put(DC_VOLTAGE_DIVISOR_ATTRIBUTE_ID, new ImmutablePair<>("DC Voltage Divisor", null));
		ALL.put(DC_CURRENT_MULTIPLIER_ATTRIBUTE_ID, new ImmutablePair<>("DC Current Multiplier", null));
		ALL.put(DC_CURRENT_DIVISOR_ATTRIBUTE_ID, new ImmutablePair<>("DC Current Divisor", null));
		ALL.put(DC_POWER_MULTIPLIER_ATTRIBUTE_ID, new ImmutablePair<>("DC Power Multiplier", null));
		ALL.put(DC_POWER_DIVISOR_ATTRIBUTE_ID, new ImmutablePair<>("DC Power Divisor", null));
		ALL.put(AC_FREQUENCY_ATTRIBUTE_ID, new ImmutablePair<>("AC Frequency", null));
		ALL.put(AC_FREQUENCY_MIN_ATTRIBUTE_ID, new ImmutablePair<>("AC Frequency Minimum", null));
		ALL.put(AC_FREQUENCY_MAX_ATTRIBUTE_ID, new ImmutablePair<>("AC Frequency Maximum", null));
		ALL.put(NEUTRAL_CURRENT_ATTRIBUTE_ID, new ImmutablePair<>("Neutral Current", null));
		ALL.put(TOTAL_ACTIVE_POWER_ATTRIBUTE_ID, new ImmutablePair<>("Total Active Power", null));
		ALL.put(TOTAL_REACTIVE_POWER_ATTRIBUTE_ID, new ImmutablePair<>("Total Reactive Power", null));
		ALL.put(TOTAL_APPARENT_POWER_ATTRIBUTE_ID, new ImmutablePair<>("Total Apparent Power", null));
		ALL.put(MEASURED_1ST_HARMONIC_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Measured 1-st Harmonic Current", null));
		ALL.put(MEASURED_3RD_HARMONIC_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Measured 3-rd Harmonic Current", null));
		ALL.put(MEASURED_5TH_HARMONIC_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Measured 5-th Harmonic Current", null));
		ALL.put(MEASURED_7TH_HARMONIC_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Measured 7-th Harmonic Current", null));
		ALL.put(MEASURED_9TH_HARMONIC_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Measured 9-th Harmonic Current", null));
		ALL.put(MEASURED_11TH_HARMONIC_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Measured 11-th Harmonic Current", null));
		ALL.put(MEASURED_PHASE_1ST_HARMONIC_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Measured Phase 1-st Harmonic Current", null));
		ALL.put(MEASURED_PHASE_3RD_HARMONIC_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Measured Phase 3-rd Harmonic Current", null));
		ALL.put(MEASURED_PHASE_5TH_HARMONIC_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Measured Phase 5-th Harmonic Current", null));
		ALL.put(MEASURED_PHASE_7TH_HARMONIC_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Measured Phase 7-th Harmonic Current", null));
		ALL.put(MEASURED_PHASE_9TH_HARMONIC_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Measured Phase 9-th Harmonic Current", null));
		ALL.put(MEASURED_PHASE_11TH_HARMONIC_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Measured Phase 11-th Harmonic Current", null));
		ALL.put(AC_FREQUENCY_MULTIPLIER_ATTRIBUTE_ID, new ImmutablePair<>("AC Frequency Multiplier", null));
		ALL.put(AC_FREQUENCY_DIVISOR_ATTRIBUTE_ID, new ImmutablePair<>("AC Frequency Divisor", null));
		ALL.put(POWER_MULTIPLIER_ATTRIBUTE_ID, new ImmutablePair<>("Power Multiplier", null));
		ALL.put(POWER_DIVISOR_ATTRIBUTE_ID, new ImmutablePair<>("Power Divisor", null));
		ALL.put(HARMONIC_CURRENT_MULTIPLIER_ATTRIBUTE_ID, new ImmutablePair<>("Harmonic Current Multiplier", null));
		ALL.put(PHASE_HARMONIC_CURRENT_MULTIPLIER_ATTRIBUTE_ID,
				new ImmutablePair<>("Phase Harmonic Current Multiplier", null));
		ALL.put(INSTANTANEOUS_VOLTAGE_ATTRIBUTE_ID, new ImmutablePair<>("Instantaneous Voltage", null));
		ALL.put(INSTANTANEOUS_LINE_CURRENT_ATTRIBUTE_ID, new ImmutablePair<>("Instantaneous Line Current", null));
		ALL.put(INSTANTANEOUS_ACTIVE_CURRENT_ATTRIBUTE_ID, new ImmutablePair<>("Instantaneous Active Current", null));
		ALL.put(INSTANTANEOUS_REACTIVE_CURRENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Instantaneous Reactive Current", null));
		ALL.put(INSTANTANEOUS_POWER_ATTRIBUTE_ID, new ImmutablePair<>("Instantaneous Power", null));
		ALL.put(RMS_VOLTAGE_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage", null));
		ALL.put(RMS_VOLTAGE_MIN_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage Minimum", null));
		ALL.put(RMS_VOLTAGE_MAX_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage Maximum", null));
		ALL.put(RMS_CURRENT_ATTRIBUTE_ID, new ImmutablePair<>("RMS Current", null));
		ALL.put(RMS_CURRENT_MIN_ATTRIBUTE_ID, new ImmutablePair<>("RMS Current Minimum", null));
		ALL.put(RMS_CURRENT_MAX_ATTRIBUTE_ID, new ImmutablePair<>("RMS Current Maximum", null));
		ALL.put(ACTIVE_POWER_ATTRIBUTE_ID, new ImmutablePair<>("Active Power", null));
		ALL.put(ACTIVE_POWER_MIN_ATTRIBUTE_ID, new ImmutablePair<>("Active Power Minimum", null));
		ALL.put(ACTIVE_POWER_MAX_ATTRIBUTE_ID, new ImmutablePair<>("Active Power Maximum", null));
		ALL.put(REACTIVE_POWER_ATTRIBUTE_ID, new ImmutablePair<>("Reactive Power", null));
		ALL.put(APPARENT_POWER_ATTRIBUTE_ID, new ImmutablePair<>("Apparent Power", null));
		ALL.put(POWER_FACTOR_ATTRIBUTE_ID, new ImmutablePair<>("AC Power Factor", new PowerFactor()));
		ALL.put(AVERAGE_RMS_VOLTAGE_MEASUREMENT_PERIOD_ATTRIBUTE_ID,
				new ImmutablePair<>("Average RMS Voltage Measurement Period", null));
		ALL.put(AVERAGE_RMS_OVER_VOLTAGE_COUNTER_ATTRIBUTE_ID,
				new ImmutablePair<>("Average RMS Over Voltage Counter", null));
		ALL.put(AVERAGE_RMS_UNDER_VOLTAGE_COUNTER_ATTRIBUTE_ID,
				new ImmutablePair<>("Average RMS Under Voltage Counter", null));
		ALL.put(RMS_EXTREME_OVER_VOLTAGE_PERIOD_ATTRIBUTE_ID,
				new ImmutablePair<>("RMS Extreme Over Voltage Period", null));
		ALL.put(RMS_EXTREME_UNDER_VOLTAGE_PERIOD_ATTRIBUTE_ID,
				new ImmutablePair<>("RMS Extreme Under Voltage Period", null));
		ALL.put(RMS_VOLTAGE_SAG_PERIOD_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage Sag Period", null));
		ALL.put(RMS_VOLTAGE_SWELL_PERIOD_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage Swell Period", null));
		ALL.put(AC_VOLTAGE_MULTIPLIER_ATTRIBUTE_ID, new ImmutablePair<>("AC Voltage Multiplier", null));
		ALL.put(AC_VOLTAGE_DIVISOR_ATTRIBUTE_ID, new ImmutablePair<>("AC Voltage Divisor", null));
		ALL.put(AC_CURRENT_MULTIPLIER_ATTRIBUTE_ID, new ImmutablePair<>("AC Current Multiplier", null));
		ALL.put(AC_CURRENT_DIVISOR_ATTRIBUTE_ID, new ImmutablePair<>("AC Current Divisor", null));
		ALL.put(AC_POWER_MULTIPLIER_ATTRIBUTE_ID, new ImmutablePair<>("AC Power Multiplier", null));
		ALL.put(AC_POWER_DIVISOR_ATTRIBUTE_ID, new ImmutablePair<>("AC Power Divisor", null));
		ALL.put(DC_OVERLOAD_ALARMS_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("DC Overload Alarms Mask", DcOverloadAlarmMask.CURRENT_OVERLOAD));
		ALL.put(DC_VOLTAGE_OVERLOAD_ATTRIBUTE_ID, new ImmutablePair<>("DC Voltage Overload", null));
		ALL.put(DC_CURRENT_OVERLOAD_ATTRIBUTE_ID, new ImmutablePair<>("DC Current Overload", null));
		ALL.put(AC_ALARMS_MASK_ATTRIBUTE_ID, new ImmutablePair<>("AC Alarms Mask", AcAlarmsMask.CURRENT_OVERLOAD));
		ALL.put(AC_VOLTAGE_OVERLOAD_ATTRIBUTE_ID, new ImmutablePair<>("AC Voltage Overload", null));
		ALL.put(AC_CURRENT_OVERLOAD_ATTRIBUTE_ID, new ImmutablePair<>("AC Current Overload", null));
		ALL.put(AC_POWER_OVERLOAD_ATTRIBUTE_ID, new ImmutablePair<>("AC Power Overload", null));
		ALL.put(AC_REACTIVE_POWER_OVERLOAD_ATTRIBUTE_ID, new ImmutablePair<>("AC Reactive Power Overload", null));
		ALL.put(AVERAGE_RMS_OVER_VOLTAGE_ATTRIBUTE_ID, new ImmutablePair<>("Average RMS Over Voltage", null));
		ALL.put(AVERAGE_RMS_UNDER_VOLTAGE_ATTRIBUTE_ID, new ImmutablePair<>("Average RMS Under Voltage", null));
		ALL.put(RMS_EXTREME_OVER_VOLTAGE_ATTRIBUTE_ID, new ImmutablePair<>("RMS Extreme Over Voltage", null));
		ALL.put(RMS_EXTREME_UNDER_VOLTAGE_ATTRIBUTE_ID, new ImmutablePair<>("RMS Extreme Under Voltage", null));
		ALL.put(RMS_VOLTAGE_SAG_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage Sag", null));
		ALL.put(RMS_VOLTAGE_SWELL_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage Swell", null));
		ALL.put(LINE_CURRENT_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("Line Current Phase B", null));
		ALL.put(ACTIVE_CURRENT_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("Active Current Phase B", null));
		ALL.put(REACTIVE_CURRENT_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("Reactive Current Phase B", null));
		ALL.put(RMS_VOLTAGE_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage Phase B", null));
		ALL.put(RMS_VOLTAGE_MIN_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage Minimum Phase B", null));
		ALL.put(RMS_VOLTAGE_MAX_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage Maximum Phase B", null));
		ALL.put(RMS_CURRENT_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("RMS Current Phase B", null));
		ALL.put(RMS_CURRENT_MIN_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("RMS Current Minimum Phase B", null));
		ALL.put(RMS_CURRENT_MAX_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("RMS Current Maximum Phase B", null));
		ALL.put(ACTIVE_POWER_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("Active Power Phase B", null));
		ALL.put(ACTIVE_POWER_MIN_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("Active Power Minimum Phase B", null));
		ALL.put(ACTIVE_POWER_MAX_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("Active Power Maximum Phase B", null));
		ALL.put(REACTIVE_POWER_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("Reactive Power Phase B", null));
		ALL.put(APPARENT_POWER_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("Apparent Power Phase B", null));
		ALL.put(POWER_FACTOR_PHASE_B_ATTRIBUTE_ID, new ImmutablePair<>("Power Factor Phase B", null));
		ALL.put(AVERAGE_RMS_VOLTAGE_MEASUREMENT_PERIOD_PHASE_B_ATTRIBUTE_ID,
				new ImmutablePair<>("Average Rms Voltage Measurement Period Phase B", null));
		ALL.put(AVERAGE_RMS_OVER_VOLTAGE_COUNTER_PHASE_B_ATTRIBUTE_ID,
				new ImmutablePair<>("Average RMS Over Voltage Counter Phase B", null));
		ALL.put(AVERAGE_RMS_UNDER_VOLTAGE_COUNTER_PHASE_B_ATTRIBUTE_ID,
				new ImmutablePair<>("Average RMS Under Voltage Counter Phase B", null));
		ALL.put(RMS_EXTREME_OVER_VOLTAGE_PERIOD_PHASE_B_ATTRIBUTE_ID,
				new ImmutablePair<>("RMS Extreme Over Voltage Period Phase B", null));
		ALL.put(RMS_EXTREME_UNDER_VOLTAGE_PERIOD_PHASE_B_ATTRIBUTE_ID,
				new ImmutablePair<>("RMS Extreme Under Voltage Period Phase B", null));
		ALL.put(RMS_VOLTAGE_SAG_PERIOD_PHASE_B_ATTRIBUTE_ID,
				new ImmutablePair<>("RMS Voltage Sag Period Phase B", null));
		ALL.put(RMS_VOLTAGE_SWELL_PERIOD_PHASE_B_ATTRIBUTE_ID,
				new ImmutablePair<>("RMS Voltage Swell Period Phase B", null));
		ALL.put(LINE_CURRENT_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("Line Current Phase C", null));
		ALL.put(ACTIVE_CURRENT_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("Active Current Phase C", null));
		ALL.put(REACTIVE_CURRENT_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("Reactive Current Phase C", null));
		ALL.put(RMS_VOLTAGE_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage Phase C", null));
		ALL.put(RMS_VOLTAGE_MIN_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage Minimum Phase C", null));
		ALL.put(RMS_VOLTAGE_MAX_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("RMS Voltage Maximum Phase C", null));
		ALL.put(RMS_CURRENT_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("RMS Current Phase C", null));
		ALL.put(RMS_CURRENT_MIN_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("RMS Current Minimum Phase C", null));
		ALL.put(RMS_CURRENT_MAX_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("RMS Current Maximum Phase C", null));
		ALL.put(ACTIVE_POWER_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("Active Power Phase C", null));
		ALL.put(ACTIVE_POWER_MIN_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("Active Power Minimum Phase C", null));
		ALL.put(ACTIVE_POWER_MAX_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("Active Power Maximum Phase C", null));
		ALL.put(REACTIVE_POWER_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("Reactive Power Phase C", null));
		ALL.put(APPARENT_POWER_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("Apparent Power Phase C", null));
		ALL.put(POWER_FACTOR_PHASE_C_ATTRIBUTE_ID, new ImmutablePair<>("Power Factor Phase C", null));
		ALL.put(AVERAGE_RMS_VOLTAGE_MEASUREMENT_PERIOD_PHASE_C_ATTRIBUTE_ID,
				new ImmutablePair<>("Average RMS Voltage Measurement Period Phase C", null));
		ALL.put(AVERAGE_RMS_OVER_VOLTAGE_COUNTER_PHASE_C_ATTRIBUTE_ID,
				new ImmutablePair<>("Average RMS Over Voltage Counter Phase C", null));
		ALL.put(AVERAGE_RMS_UNDER_VOLTAGE_COUNTER_PHASE_C_ATTRIBUTE_ID,
				new ImmutablePair<>("Average RMS Under Voltage Counter Phase C", null));
		ALL.put(RMS_EXTREME_OVER_VOLTAGE_PERIOD_PHASE_C_ATTRIBUTE_ID,
				new ImmutablePair<>("RMS Extreme Over Voltage Period Phase C", null));
		ALL.put(RMS_EXTREME_UNDER_VOLTAGE_PERIOD_PHASE_C_ATTRIBUTE_ID,
				new ImmutablePair<>("RMS Extreme Under Voltage Period Phase C", null));
		ALL.put(RMS_VOLTAGE_SAG_PERIOD_PHASE_C_ATTRIBUTE_ID,
				new ImmutablePair<>("RMS Voltage Sag Period Phase C", null));
		ALL.put(RMS_VOLTAGE_SWELL_PERIOD_PHASE_C_ATTRIBUTE_ID,
				new ImmutablePair<>("RMS Voltage Swell Period Phase C", null));
	}
}
