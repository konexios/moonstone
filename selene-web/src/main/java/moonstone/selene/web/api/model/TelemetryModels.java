package moonstone.selene.web.api.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;

public class TelemetryModels {

	public static class TelemetryTypeOption extends BaseModels.EnumOption<TelemetryItemType> {
		private static final long serialVersionUID = 160167382756073541L;

		public TelemetryTypeOption(TelemetryItemType telemetryType) {
			super(telemetryType);
		}
	}

	public static class TelemetryList extends BaseModels.EntityAbstract {
		private static final long serialVersionUID = -893134556573886735L;

		public String name;
		public String value;
		public Long timestamp;

		public TelemetryList(Telemetry telemetry) {
			super(telemetry.getId());
			this.name = telemetry.getName();
			this.value = telemetry.value().toString();
			this.timestamp = telemetry.getTimestamp();
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public Long getTimestamp() {
			return timestamp;
		}
	}

	public static class TelemetryModel extends BaseModels.EntityAbstract {
		private static final long serialVersionUID = -8422809925642320825L;

		private TelemetryItemType type;
		private long deviceId;
		private long timestamp;
		private String name;
		private String strValue;
		private Long intValue;
		private Double floatValue;
		private Boolean boolValue;
		private LocalDate dateValue;
		private LocalDateTime dateTimeValue;

		public TelemetryModel(Telemetry telemetry) {
			super(telemetry.getId());
			this.type = telemetry.getType();
			this.deviceId = telemetry.getDeviceId();
			this.timestamp = telemetry.getTimestamp();
			this.name = telemetry.getName();
			this.strValue = telemetry.getStrValue();
			this.intValue = telemetry.getIntValue();
			this.floatValue = telemetry.getFloatValue();
			this.boolValue = telemetry.getBoolValue();
			this.dateValue = telemetry.getDateValue();
			this.dateTimeValue = telemetry.getDatetimeValue();
		}

		public TelemetryItemType getType() {
			return type;
		}

		public long getDeviceId() {
			return deviceId;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public String getName() {
			return name;
		}

		public String getStrValue() {
			return strValue;
		}

		public Long getIntValue() {
			return intValue;
		}

		public Double getFloatValue() {
			return floatValue;
		}

		public Boolean getBoolValue() {
			return boolValue;
		}

		public LocalDate getDateValue() {
			return dateValue;
		}

		public LocalDateTime getDateTimeValue() {
			return dateTimeValue;
		}
	}

	public static class TelemetryUpsert implements Serializable {
		private static final long serialVersionUID = -5187746370224539L;

		private TelemetryModel telemetry;

		public TelemetryUpsert(TelemetryModel telemetry) {
			this.telemetry = telemetry;
		}

		public TelemetryModel getTelemetry() {
			return telemetry;
		}
	}
}
