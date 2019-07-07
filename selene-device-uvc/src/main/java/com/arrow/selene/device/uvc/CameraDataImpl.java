package com.arrow.selene.device.uvc;

import java.util.ArrayList;
import java.util.List;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.selene.data.Telemetry;
import com.arrow.selene.engine.DeviceDataAbstract;

public class CameraDataImpl extends DeviceDataAbstract implements CameraData {

    private long width;
    private long height;
    private long size;
    private String format;
    private byte[] image;
    private Boolean faceDetected;

    @Override
    public IotParameters writeIoTParameters() {
        IotParameters result = new IotParameters();
        result.setLong("width", width);
        result.setLong("height", height);
        result.setLong("size", size);
        if (format != null)
            result.setString("format", format);
        if (image != null)
            result.setBinary("image", image);
        if (faceDetected != null)
            result.setBoolean("faceDetected", faceDetected);
        return result;
    }

    @Override
    public List<Telemetry> writeTelemetries() {
        List<Telemetry> result = new ArrayList<>();
        result.add(writeIntTelemetry("width", width));
        result.add(writeIntTelemetry("height", height));
        result.add(writeIntTelemetry("size", size));
        if (format != null)
            result.add(writeStringTelemetry(TelemetryItemType.String, "format", format));
        if (image != null)
            result.add(writeBinaryTelemetry("image", image));
        if (faceDetected != null)
            result.add(writeBooleanTelemetry("faceDetected", faceDetected));
        return result;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isFaceDetected() {
        return faceDetected;
    }

    public void setFaceDetected(boolean faceDetected) {
        this.faceDetected = faceDetected;
    }
}
