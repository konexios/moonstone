package com.arrow.widget.model;

import java.util.List;

import com.arrow.kronos.data.EsTelemetryItem;
import com.arrow.kronos.data.TelemetryItem;

public class SquareTelemetryItemLineChartModel extends TelemetryItemLineChartModel {
    private static final long serialVersionUID = 3832444365106524715L;

    protected String v2;

    public static SquareTelemetryItemLineChartModel[] getTelemetryItemChartModels(
            List<EsTelemetryItem> telemetryItems) {
        SquareTelemetryItemLineChartModel[] result = new SquareTelemetryItemLineChartModel[telemetryItems.size()];
        int index = 0;

        for (EsTelemetryItem telemetryItem : telemetryItems) {
            result[index++] = new SquareTelemetryItemLineChartModel(telemetryItem);
        }
        return result;
    }

    public SquareTelemetryItemLineChartModel(EsTelemetryItem telemetryItem) {
        super(telemetryItem);

        String[] values = telemetryItem.value().toString().split("\\|");
        super.t = telemetryItem.getTimestamp();
        super.v = values[0];
        this.v2 = values[1];
    }

    public SquareTelemetryItemLineChartModel(TelemetryItem telemetryItem) {
        super(telemetryItem);

        String[] values = telemetryItem.value().toString().split("\\|");
        super.t = telemetryItem.getTimestamp();
        super.v = values[0];
        this.v2 = values[1];
    }

    public String getV2() {
        return v2;
    }

    public void setV2(String v2) {
        this.v2 = v2;
    }
}
