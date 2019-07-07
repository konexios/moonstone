package com.arrow.selene.device.xbee.zcl.domain.se.metering.data;

import java.util.EnumSet;
import java.util.Set;

public enum SnapshotCause {
	GENERAL,
	END_OF_BILLING_PERIOD,
	END_OF_BLOCK_PERIOD,
	CHANGE_OF_TARIFF_INFORMATION,
	CHANGE_OF_PRICE_MATRIX,
	CHANGE_OF_BLOCK_THRESHOLDS,
	CHANGE_OF_CV,
	CHANGE_OF_CF,
	CHANGE_OF_CALENDAR,
	CRITICAL_PEAK_PRICING,
	MANUALLY_TRIGGERED_FROM_CLIENT,
	END_OF_RESOLVE_PERIOD,
	CHANGE_OF_TENANCY,
	CHANGE_OF_SUPPLIER,
	CHANGE_OF_METER_MODE,
	DEBT_PAYMENT,
	SCHEDULED_SNAPSHOT,
	OTA_FIRMWARE_DOWNLOAD,
	RESERVED_FOR_PREPAYMENT_CLUSTER;

	public static Set<SnapshotCause> getByValue(int value) {
		Set<SnapshotCause> result = EnumSet.noneOf(SnapshotCause.class);
		for (SnapshotCause item : values()) {
			if ((value >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getByValue(Set<SnapshotCause> items) {
		int result = 0;
		for (SnapshotCause item : items) {
			result |= 1 << item.ordinal();
		}
		return result;
	}
}
