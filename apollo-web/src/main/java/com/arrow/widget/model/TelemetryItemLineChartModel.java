package com.arrow.widget.model;

import java.io.Serializable;
import java.util.List;

import com.arrow.kronos.data.EsTelemetryItem;
import com.arrow.kronos.data.TelemetryItem;

public class TelemetryItemLineChartModel implements Serializable {
	private static final long serialVersionUID = 3621168729740808449L;

	protected long t;
	protected String v;

	public static TelemetryItemLineChartModel[] getTelemetryItemChartModels(List<EsTelemetryItem> telemetryItems) {
		TelemetryItemLineChartModel[] result = new TelemetryItemLineChartModel[telemetryItems.size()];
		int index = 0;

		for (EsTelemetryItem telemetryItem : telemetryItems) {
			result[index++] = new TelemetryItemLineChartModel(telemetryItem);
		}
		return result;
	}

	public TelemetryItemLineChartModel(EsTelemetryItem telemetryItem) {
		this.t = telemetryItem.getTimestamp().toEpochMilli();
		this.v = telemetryItem.value().toString();
	}

	public TelemetryItemLineChartModel(TelemetryItem telemetryItem) {
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
