package com.arrow.kronos.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.TelemetryItem;

import moonstone.acn.client.model.TelemetryItemModel;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.Loggable;

public class TelemetryItemConverter {
	static Loggable LOGGER = new Loggable(TelemetryItemConverter.class.getName()) {
	};

	public static TelemetryItem toData(TelemetryItemType type, String value) {
		String method = "convert";
		TelemetryItem result = new TelemetryItem();
		result.setType(type);
		try {
			switch (type) {
			case Boolean:
				result.setBoolValue(Boolean.parseBoolean(value));
				break;
			case Date:
				result.setDateValue(LocalDate.parse(value));
				break;
			case DateTime:
				result.setDatetimeValue(LocalDateTime.parse(value));
				break;
			case Float:
				result.setFloatValue(Double.parseDouble(value));
				break;
			case FloatCube:
				result.setFloatCubeValue(value);
				break;
			case FloatSquare:
				result.setFloatSqrValue(value);
				break;
			case Integer:
				result.setIntValue(Long.parseLong(value));
				break;
			case IntegerCube:
				result.setIntCubeValue(value);
				break;
			case IntegerSquare:
				result.setIntSqrValue(value);
				break;
			case String:
				result.setStrValue(value);
				break;
			case System:
				result.setStrValue(value);
				break;
			case Binary:
				result.setBinaryValue(value);
				break;
			default:
				throw new AcsLogicalException("unsupported type: " + type);
			}
		} catch (Exception e) {
			LOGGER.logError(method, "ERROR converting type: %s, value: %s, error: %s", type, value, e.getMessage());
			result = null;
		}
		return result;
	}

	public static TelemetryItemModel toModel(TelemetryItem item, Device device) {
		TelemetryItemModel model = new TelemetryItemModel();
		model.setName(item.getName());
		model.setType(item.getType());
		model.setTimestamp(item.getTimestamp());
		model.setStrValue(item.getStrValue());
		model.setIntValue(item.getIntValue());
		model.setFloatValue(item.getFloatValue());
		model.setBoolValue(item.getBoolValue());
		model.setDateValue(item.getDateValue() == null ? null : item.getDateValue().toString());
		model.setDatetimeValue(item.getDatetimeValue() == null ? null : item.getDatetimeValue().toString());
		model.setIntSqrValue(item.getIntSqrValue());
		model.setFloatSqrValue(item.getFloatSqrValue());
		model.setFloatCubeValue(item.getFloatCubeValue());
		model.setIntCubeValue(item.getIntCubeValue());
		model.setFloatCubeValue(item.getFloatCubeValue());
		model.setDeviceHid(device.getHid());
		return model;
	}
}
