package moonstone.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;

import moonstone.selene.device.xbee.zcl.ZclClusters;
import moonstone.selene.device.xbee.zcl.ZclFrame;

public abstract class GeneralRequest extends ZclFrame {
	public GeneralRequest(int manufacturerCode) {
		header.setClusterSpecific(false);
		header.setManufacturerCode(manufacturerCode);
		header.setFromServer(false);
		header.setDisableDefaultResponse(false);
		header.setCommandId(getId());
	}

	public final byte[] build(int sequence, int clusterId) {
		byte[] headerArray = header.buildHeader(sequence, ZclClusters.getCluster(clusterId) == null);
		byte[] payload = toPayload();
		ByteBuffer os = ByteBuffer.allocate(headerArray.length + (payload == null ? 0 : payload.length));
		os.put(headerArray);
		if (payload != null && payload.length > 0) {
			os.put(payload);
		}
		return os.array();
	}

	protected abstract byte[] toPayload();
}
