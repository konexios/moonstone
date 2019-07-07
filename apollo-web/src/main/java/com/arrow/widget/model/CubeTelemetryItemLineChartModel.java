package com.arrow.widget.model;

import java.util.List;

import com.arrow.kronos.data.EsTelemetryItem;
import com.arrow.kronos.data.TelemetryItem;

public class CubeTelemetryItemLineChartModel extends SquareTelemetryItemLineChartModel {
    private static final long serialVersionUID = 6252610654627492763L;

    private String v3;

    public static CubeTelemetryItemLineChartModel[] getTelemetryItemChartModels(List<EsTelemetryItem> telemetryItems) {
        CubeTelemetryItemLineChartModel[] result = new CubeTelemetryItemLineChartModel[telemetryItems.size()];
        int index = 0;

        for (EsTelemetryItem telemetryItem : telemetryItems) {
            result[index++] = new CubeTelemetryItemLineChartModel(telemetryItem);
        }
        return result;
    }

    public CubeTelemetryItemLineChartModel(EsTelemetryItem telemetryItem) {
        super(telemetryItem);

        String[] values = telemetryItem.value().toString().split("\\|");
        super.t = telemetryItem.getTimestamp();
        super.v = values[0];
        this.v2 = values[1];
        this.v3 = values[2];
    }

    public CubeTelemetryItemLineChartModel(TelemetryItem telemetryItem) {
        super(telemetryItem);

        String[] values = telemetryItem.value().toString().split("\\|");
        super.t = telemetryItem.getTimestamp();
        super.v = values[0];
        super.v2 = values[1];
        this.v3 = values[2];
    }

    public String getV3() {
        return v3;
    }

    public void setV3(String v3) {
        this.v3 = v3;
    }
}
