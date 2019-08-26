package moonstone.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class MatchDescriptorRequest {
	private int sequence;
	private XBee16BitAddress address;
	private int profileId;
	private int[] inputClusters;
	private int[] outputClusters;

	public MatchDescriptorRequest(int sequence, XBee16BitAddress address, int profileId, int[] inputClusters, int[]
			outputClusters) {
		this.sequence = sequence;
		this.address = address;
		this.profileId = profileId;
		this.inputClusters = inputClusters;
		this.outputClusters = outputClusters;
	}

	public int getSequence() {
		return sequence;
	}

	public XBee16BitAddress getAddress() {
		return address;
	}

	public int getProfileId() {
		return profileId;
	}

	public int[] getInputClusters() {
		return inputClusters;
	}

	public int[] getOutputClusters() {
		return outputClusters;
	}

	public static MatchDescriptorRequest fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int sequence = Byte.toUnsignedInt(buffer.get());
		byte[] array = new byte[2];
		buffer.get(array);
		XBee16BitAddress address = new XBee16BitAddress(ByteUtils.swapByteArray(array));
		int profileId = Short.toUnsignedInt(buffer.getShort());
		int number = Byte.toUnsignedInt(buffer.get());
		int[] inputClusters = new int[number];
		for (int i = 0; i < number; i++) {
			inputClusters[i] = Short.toUnsignedInt(buffer.getShort());
		}
		number = Byte.toUnsignedInt(buffer.get());
		int[] outputClusters = new int[number];
		for (int i = 0; i < number; i++) {
			outputClusters[i] = Short.toUnsignedInt(buffer.getShort());
		}
		return new MatchDescriptorRequest(sequence, address, profileId, inputClusters, outputClusters);
	}
}
