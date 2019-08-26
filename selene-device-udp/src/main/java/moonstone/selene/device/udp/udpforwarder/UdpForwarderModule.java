package moonstone.selene.device.udp.udpforwarder;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import org.apache.commons.lang3.StringUtils;

import moonstone.selene.device.udp.UdpModuleAbstract;
import moonstone.selene.engine.service.NetworkService;
import moonstone.selene.model.StatusModel;

public class UdpForwarderModule extends UdpModuleAbstract<UdpForwarderInfo, UdpForwarderProperties,
		UdpForwarderStates, UdpForwarderData> {
	@Override
	public UdpForwarderData createUdpData() {
		return new UdpForwarderData();
	}

	@Override
	protected UdpForwarderProperties createProperties() {
		return new UdpForwarderProperties();
	}

	@Override
	protected UdpForwarderInfo createInfo() {
		return new UdpForwarderInfo();
	}

	@Override
	protected UdpForwarderStates createStates() {
		return new UdpForwarderStates();
	}

	@Override
	public StatusModel performCommand(byte... bytes) {
		sendData(bytes);
		return StatusModel.OK;
	}

	public StatusModel sendData(byte[] data) {
		String method = "sendData";
		StatusModel result = StatusModel.OK;
		String address = getProperties().getDeviceAddress();
		if (StringUtils.isBlank(address)) {
			logError(method, "device address is unknown yet, cannot send the data");
			result.withStatus("ERROR").withMessage("device address is unknown yet, cannot send the data");
		} else {
			try (DatagramSocket socket = new DatagramSocket()) {
				socket.setBroadcast(true);
				int port = getProperties().getDevicePort();
				InetSocketAddress socketAddress = NetworkService.getInstance().resolveAddress(address, port);
				if (socketAddress != null) {
					socket.send(new DatagramPacket(data, data.length, socketAddress));
					logDebug(method, "sending data to device at %s:%d ...", address, port);
				}
			} catch (Exception e) {
				logError(method, "error sending data ...", e);
				result.withStatus("ERROR").withMessage(String.format("error sending data ...\n %s", e));
			}
		}
		return result;
	}
}
