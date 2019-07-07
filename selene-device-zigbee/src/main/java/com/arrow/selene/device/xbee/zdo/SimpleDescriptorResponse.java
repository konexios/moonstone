package com.arrow.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.xbee.zcl.ZclStatus;

public class SimpleDescriptorResponse {
	private int status;
	private int endpoint;
	private int applicationProfile;
	private int deviceProfile;
	private int[] inputClusters;
	private int[] outputClusters;

	public SimpleDescriptorResponse(int status, int endpoint, int applicationProfile, int deviceProfile,
	                                int[] inputClusters, int[] outputClusters) {
		this.status = status;
		this.endpoint = endpoint;
		this.applicationProfile = applicationProfile;
		this.deviceProfile = deviceProfile;
		this.inputClusters = inputClusters;
		this.outputClusters = outputClusters;
	}

	public int getStatus() {
		return status;
	}

	public int getEndpoint() {
		return endpoint;
	}

	public int getApplicationProfile() {
		return applicationProfile;
	}

	public int getDeviceProfile() {
		return deviceProfile;
	}

	public int[] getInputClusters() {
		return inputClusters;
	}

	public int[] getOutputClusters() {
		return outputClusters;
	}

	public static SimpleDescriptorResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.get(); // sequence
		int status = Byte.toUnsignedInt(buffer.get());
		int endpoint = 0;
		int applicationProfile = 0;
		int deviceProfile = 0;
		int[] inputClusters = null;
		int[] outputClusters = null;
		if (status == ZclStatus.SUCCESS) {
			buffer.getShort(); // addr
			buffer.get(); // length
			endpoint = Byte.toUnsignedInt(buffer.get());
			applicationProfile = Short.toUnsignedInt(buffer.getShort());
			deviceProfile = Short.toUnsignedInt(buffer.getShort());
			buffer.get(); // version
			inputClusters = new int[buffer.get()];
			for (int i = 0; i < inputClusters.length; i++) {
				inputClusters[i] = Short.toUnsignedInt(buffer.getShort());
			}
			outputClusters = new int[buffer.get()];
			for (int i = 0; i < outputClusters.length; i++) {
				outputClusters[i] = Short.toUnsignedInt(buffer.getShort());
			}
		}
		return new SimpleDescriptorResponse(status, endpoint, applicationProfile, deviceProfile, inputClusters,
				outputClusters);
	}
}
