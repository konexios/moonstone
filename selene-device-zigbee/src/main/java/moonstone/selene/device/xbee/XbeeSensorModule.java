package moonstone.selene.device.xbee;

import java.time.Instant;

import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.XBeeNetwork;
import com.digi.xbee.api.io.IOLine;
import com.digi.xbee.api.io.IOSample;
import com.digi.xbee.api.listeners.IIOSampleReceiveListener;

import moonstone.selene.SeleneException;
import moonstone.selene.engine.DeviceModuleAbstract;
import moonstone.selene.engine.Utils;

public class XbeeSensorModule extends DeviceModuleAbstract<XbeeSensorInfo, XbeeSensorProperties, XbeeSensorStates,
        XbeeSensorData> {

    private XBeeDevice device;
    private Thread discoveryThread;
    private Receiver receiver;

    @Override
    protected void startDevice() {
        super.startDevice();
        String method = "sendCommand";
        try {
            logInfo(method, "opening xbee %s : %d", getInfo().getPort(), getInfo().getBaudRate());
            device = new XBeeDevice(getInfo().getPort(), getInfo().getBaudRate());
            device.open();
            logInfo(method, "xbee port opened!");

            receiver = new Receiver();
            device.addIOSampleListener(receiver);
            logInfo(method, "added receiver to xbee coordinator");

            discoveryThread = new Thread(receiver, "discoveryThread");
            logInfo(method, "starting discovery thread ...");
            discoveryThread.start();

            logInfo(method, "started");
        } catch (Exception e) {
            throw new SeleneException("unable to start xbee device", e);
        }
    }

    @Override
    public void stop() {
        super.stop();
        String method = "stop";
        moonstone.acn.client.utils.Utils.shutdownThread(discoveryThread);
        if (device != null) {
            logInfo(method, "closing device ...");
            device.close();
        }
    }

    @Override
    protected XbeeSensorProperties createProperties() {
        return new XbeeSensorProperties();
    }

    @Override
    protected XbeeSensorInfo createInfo() {
        return new XbeeSensorInfo();
    }

    @Override
    protected XbeeSensorStates createStates() {
        return new XbeeSensorStates();
    }

    private class Receiver implements IIOSampleReceiveListener, Runnable {

        private boolean deviceFound = false;
        private long lastReceived = 0;

        @Override
        public void run() {
            String method = "run";

            XBeeNetwork network = device.getNetwork();
            deviceFound = false;
            int monitorInterval = getProperties().getPollingInterval() * 3;
            while (!isShuttingDown()) {
                if (deviceFound) {
                    if (Instant.now().toEpochMilli() - lastReceived > monitorInterval) {
                        logInfo(method, "no data has been received recently, let's search for it ...");
                        deviceFound = false;
                    }
                }

                if (!deviceFound) {
                    try {
                        logInfo(method, "searching for remote sensor %s", getInfo().getNodeId());
                        RemoteXBeeDevice remote = network.discoverDevice(getInfo().getNodeId());
                        if (remote != null) {
                            logInfo(method, "found remote sensor: %s / %s", remote.getNodeID(),
                                    remote.get64BitAddress().toString());
                            remote.setDestinationAddress(device.get64BitAddress());
                            remote.setIOSamplingRate(getProperties().getPollingInterval());
                            deviceFound = true;
                        }
                    } catch (Exception e) {
                        logError(method, "error connecting to remote sensor", e);
                    }
                }
                if (!deviceFound) {
                    logError(method, "remote sensor not found, retrying in %d ms", getProperties().getRetryInterval());
                    try {
                        Thread.sleep(getProperties().getRetryInterval());
                    } catch (Exception x) {
                    }
                } else {
                    logError(method, "remote sensor connected, re-checking in %d ms", monitorInterval);
                    try {
                        Thread.sleep(monitorInterval);
                    } catch (Exception x) {
                    }
                }
            }
            logInfo(method, "complete");
        }

        @Override
        public void ioSampleReceived(RemoteXBeeDevice remoteDevice, IOSample ioSample) {
            String method = "ioSampleReceived";
            lastReceived = Instant.now().toEpochMilli();
            try {

                int a1 = ioSample.getAnalogValue(IOLine.DIO1_AD1);
                int a2 = ioSample.getAnalogValue(IOLine.DIO2_AD2);
                int a3 = ioSample.getAnalogValue(IOLine.DIO3_AD3);

                double mv = a2 / 1023.0 * 1200.0;
                double temperature = Utils.celsiusToFahrenheit((mv - 500.0) / 10.0);

                mv = a3 / 1023.0 * 1200.0;
                double humidity = (mv * 108.2 / 33.2 / 5000 - 0.16) / 0.0062;

                double brightness = a1 / 1023.0 * 1200.0;

                logInfo(method, "received: %.2f / %.2f / %.2f", temperature, humidity, brightness);

                queueDataForSending(new XbeeSensorDataImpl(temperature, humidity, brightness));
            } catch (Exception e) {
                logError(method, "Error processing sample data from remote sensor", e);
            }
        }
    }
}
