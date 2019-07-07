package com.arrow.selene.device.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.engine.DeviceModuleAbstract;
import com.arrow.selene.engine.EngineConstants;

public abstract class UdpModuleAbstract<Info extends UdpInfo, Prop extends UdpProperties, State extends UdpStates, Data extends UdpData>
        extends DeviceModuleAbstract<Info, Prop, State, Data> implements UdpModule<Info, Prop, State, Data> {

	private DatagramSocket socket;
	private DatagramPacket packet;
	private byte[] buffer;
	private Thread listeningThread;

	@Override
	protected void startDevice() {
		super.startDevice();
		String method = "UdpModuleAbstract.sendCommand";
		startUdpListener();
		logInfo(method, "started");
	}

	@Override
	public void stop() {
		super.stop();
		String method = "UdpModuleAbstract.stop";
		logInfo(method, "stopping ...");
		if (socket != null) {
			try {
				logInfo(method, "closing socket ...");
				socket.close();
				listeningThread = null;
			} catch (Exception e) {
			}
			socket = null;
		}
		logInfo(method, "shutdown complete");
	}

	private void startUdpListener() {
		String method = "UdpModuleAbstract.startUdpListener";
		try {
			String address = getProperties().getAddress();
			if (address.isEmpty()) {
				address = UdpProperties.ALL_INTERFACES_ADDRESS;
			}
			int port = getProperties().getPort();
			logInfo(method, "listening to UDP Port: %s:%d", address, port);
			socket = new DatagramSocket(port, InetAddress.getByName(address));
			socket.setBroadcast(true);
			buffer = new byte[EngineConstants.UDP_PACKET_BUFFER_SIZE];
			packet = new DatagramPacket(buffer, buffer.length);
		} catch (Exception e) {
			logError(method, "unable to start listener " + getName(), e);
			return;
		}

		logInfo(method, "socket ready!");
		logInfo(method, "starting listening thread...");
		listeningThread = new Thread(() -> {
			long counter = 0L;
			while (!isShuttingDown()) {
				try {
					if (socket == null || getService() == null) {
						break;
					}

					// polling socket
					socket.receive(packet);
					counter++;
					logDebug(method, "packet %d received, length = %d", counter, packet.getLength());

				// extract data from packet
				final byte[] received = Arrays.copyOfRange(packet.getData(), packet.getOffset(), packet.getLength());

					if (getService() != null) {
						getService().submit(() -> {
							try {
								processData(received);
							} catch (Throwable e) {
								logError(method, e);
							}
						});
					} else {
						logError(method, "service is not available");
					}
				} catch (Exception e) {
					if (!isShuttingDown()) {
						logError(method, "error handling packet", e);
					}
				}
			}
		}, "listeningThread");
		listeningThread.start();
	}

	protected void processData(byte[] received) {
		String method = "UdpModuleAbstract.processData";
		Validate.notNull(getDevice(), "device is null");
		Data data = createUdpData();

		if (data.parseRawData(received)) {
			queueDataForSending(data);
		} else {
			logWarn(method, "unable to parse data!");
		}
	}
}
