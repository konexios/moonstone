package moonstone.selene.device.udp;

import moonstone.selene.engine.DeviceModule;

public interface UdpModule<Info extends UdpInfo, Prop extends UdpProperties, State extends UdpStates, Data extends
        UdpData>
		extends DeviceModule<Info, Prop, State, Data> {
	Data createUdpData();
}
