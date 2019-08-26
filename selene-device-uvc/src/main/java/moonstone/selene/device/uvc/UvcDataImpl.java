package moonstone.selene.device.uvc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;
import moonstone.selene.engine.DeviceDataAbstract;

public class UvcDataImpl extends DeviceDataAbstract implements UvcData {

    public enum Type {
        STREAM_START, STREAM_DATA, STREAM_STOP, VIDEO_START, VIDEO_STOP
    }

    private String fileName;
    private Type type;
    private int frame;
    private byte[] image;

    @Override
    public IotParameters writeIoTParameters() {
        Validate.notNull(type, "type is missing");
        Validate.notBlank(fileName, "fileName is missing");

        IotParameters result = new IotParameters();
        result.setString("type", type.name());
        result.setString("fileName", fileName);
        if (type == Type.STREAM_DATA) {
            result.setInteger("frame", frame);
            result.setBinary("image", image);
        }
        return result;
    }

    @Override
    public List<Telemetry> writeTelemetries() {
        Validate.notNull(type, "type is missing");
        Validate.notBlank(fileName, "fileName is missing");

        List<Telemetry> result = new ArrayList<>();
        result.add(writeStringTelemetry(TelemetryItemType.String, "type", type.name()));
        result.add(writeStringTelemetry(TelemetryItemType.String, "fileName", fileName));
        if (type == Type.STREAM_DATA) {
            result.add(writeIntTelemetry("frame", (long) frame));
            // TODO currently do not save image to local database
            result.add(writeBinaryTelemetry("image", new byte[0]));
        }
        return result;
    }

    public UvcDataImpl withFileName(String fileName) {
        setFileName(fileName);
        return this;
    }

    public UvcDataImpl withType(Type type) {
        setType(type);
        return this;
    }

    public UvcDataImpl withFrame(int frame) {
        setFrame(frame);
        return this;
    }

    public UvcDataImpl withImage(byte[] image) {
        setImage(image);
        return this;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
