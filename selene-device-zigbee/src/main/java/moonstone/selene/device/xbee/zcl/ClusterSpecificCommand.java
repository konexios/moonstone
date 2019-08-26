package moonstone.selene.device.xbee.zcl;

import java.nio.ByteBuffer;

import moonstone.selene.device.xbee.zcl.general.GeneralResponse;

public abstract class ClusterSpecificCommand<T extends GeneralResponse<?>> extends GeneralResponse<T> {
	public ClusterSpecificCommand() {
		header.setClusterSpecific(true);
		header.setManufacturerSpecific(false);
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

	public abstract byte[] toPayload();
}
