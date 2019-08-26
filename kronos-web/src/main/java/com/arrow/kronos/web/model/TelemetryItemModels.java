package com.arrow.kronos.web.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.springframework.http.MediaType;

import com.arrow.kronos.data.EsTelemetryItem;
import com.arrow.kronos.data.LastTelemetryItem;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

import moonstone.acn.client.model.TelemetryItemType;
import moonstone.acs.JsonUtils;

public class TelemetryItemModels {

	public static class TelemetryItemModel extends CoreDocumentModel {

		private static final long serialVersionUID = 7063229888268241834L;

		private String name;
		private long timestamp;
		private TelemetryItemType type;
		private String value;

		public TelemetryItemModel() {
			super(null, null);
		}

		public TelemetryItemModel(TelemetryItem telemetryItem) {
			super(telemetryItem.getId(), telemetryItem.getHid());
			this.name = telemetryItem.getName();
			this.timestamp = telemetryItem.getTimestamp();
			this.type = telemetryItem.getType();
			this.value = telemetryItem.value().toString();
		}

		public TelemetryItemModel(LastTelemetryItem lastTelemetryItem) {
			super(lastTelemetryItem.getId(), lastTelemetryItem.getHid());
			this.name = lastTelemetryItem.getName();
			this.timestamp = lastTelemetryItem.getTimestamp();
			this.type = lastTelemetryItem.getType();
			if (lastTelemetryItem.value() != null)
				this.value = lastTelemetryItem.value().toString();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public TelemetryItemType getType() {
			return type;
		}

		public void setType(TelemetryItemType type) {
			this.type = type;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	public static class TelemetryItemChartModel implements Serializable {
		private static final long serialVersionUID = -8080328115231419233L;

		private long t;
		private String v;

		public static TelemetryItemChartModel[] getTelemetryItemChartModels(List<EsTelemetryItem> telemetryItems) {
			TelemetryItemChartModel[] result = new TelemetryItemChartModel[telemetryItems.size()];
			int index = 0;

			for (EsTelemetryItem telemetryItem : telemetryItems) {
				result[index++] = new TelemetryItemChartModel(telemetryItem);
			}
			return result;
		}

		public TelemetryItemChartModel(EsTelemetryItem telemetryItem) {
			this.t = telemetryItem.getTimestamp();
			this.v = telemetryItem.value().toString();
		}

		public TelemetryItemChartModel(TelemetryItem telemetryItem) {
			this.t = telemetryItem.getTimestamp();
			this.v = telemetryItem.value().toString();
		}

		public long getT() {
			return t;
		}

		public void setT(long t) {
			this.t = t;
		}

		public String getV() {
			return v;
		}

		public void setV(String v) {
			this.v = v;
		}

	}

	public static class TelemetryItemExporter {
		public static final String MEDIA_TYPE_TEXT_CSV = "text/csv";
		public static final String[] CSV_HEADER = new String[] { "deviceId", "telemetryId", "name", "type", "value",
				"timestamp" };
		public static final String JSON_RECORD_DELIMITER = ",";
		public static final String JSON_HEADER = "[";
		public static final String JSON_FOOTER = "]";

		public enum ExportType {
			CSV, JSON
		};

		private ExportType type;
		private boolean firstRecord;
		private CSVFormat csvFormat;

		public TelemetryItemExporter(String type) {
			// json is the default type
			this(ExportType.CSV.name().equalsIgnoreCase(type) ? ExportType.CSV : ExportType.JSON);
		}

		public TelemetryItemExporter(ExportType type) {
			this.type = type;
			this.firstRecord = true;
			this.csvFormat = CSVFormat.DEFAULT;
		}

		public boolean isCSV() {
			return type == ExportType.CSV;
		}

		public boolean isJSON() {
			return type == ExportType.CSV;
		}

		public String contentType() {
			return type == ExportType.CSV ? MEDIA_TYPE_TEXT_CSV : MediaType.APPLICATION_JSON_VALUE;
		}

		public String fileExtension() {
			return type.name().toLowerCase();
		}

		public String header() throws IOException {
			if (type == ExportType.CSV) {
				StringBuilder sb = new StringBuilder();
				csvFormat.printRecord(sb, (Object[]) CSV_HEADER);
				return sb.toString();
			} else {
				return JSON_HEADER;
			}
		}

		public String footer() {
			return type == ExportType.JSON ? JSON_FOOTER : "";
		}

		public String serialize(TelemetryItem item) throws IOException {
			StringBuilder sb = new StringBuilder();
			if (firstRecord) {
				firstRecord = false;
			} else if (type == ExportType.JSON) {
				sb.append(JSON_RECORD_DELIMITER);
			}
			TelemetryItemExportModel model = new TelemetryItemExportModel(item);
			if (type == ExportType.CSV) {
				model.toCSV(csvFormat, sb);
			} else {
				model.toJSON(sb);
			}

			return sb.toString();
		}
	}

	public static class TelemetryItemExportModel implements Serializable {
		private static final long serialVersionUID = -6138812116864013588L;

		private final String deviceId;
		private final String telemetryId;
		private final String name;
		private final String type;
		private final String value;
		private final long timestamp;

		public TelemetryItemExportModel(TelemetryItem item) {
			this.deviceId = item.getDeviceId();
			this.telemetryId = item.getTelemetryId();
			this.name = item.getName();
			this.type = item.getType().name();
			this.value = item.value().toString();
			this.timestamp = item.getTimestamp();
		}

		public String getDeviceId() {
			return deviceId;
		}

		public String getTelemetryId() {
			return telemetryId;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		public String getValue() {
			return value;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void toJSON(StringBuilder sb) {
			sb.append(JsonUtils.toJson(this));
		}

		public void toCSV(CSVFormat csvFormat, StringBuilder sb) throws IOException {
			csvFormat.printRecord(sb, deviceId, telemetryId, name, type, value, timestamp);
		}
	}

	public static class TelemetryItemsDelete {

		private List<String> lastTelemetryIds;
		private boolean removeTelemetryDefinition;

		public List<String> getLastTelemetryIds() {
			return lastTelemetryIds;
		}

		public void setLastTelemetryIds(List<String> lastTelemetryIds) {
			this.lastTelemetryIds = lastTelemetryIds;
		}

		public boolean isRemoveTelemetryDefinition() {
			return removeTelemetryDefinition;
		}

		public void setRemoveTelemetryDefinition(boolean removeTelemetryDefinition) {
			this.removeTelemetryDefinition = removeTelemetryDefinition;
		}
	}
}
