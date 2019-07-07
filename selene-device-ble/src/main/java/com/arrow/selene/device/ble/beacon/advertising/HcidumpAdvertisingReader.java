package com.arrow.selene.device.ble.beacon.advertising;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import com.arrow.selene.SeleneException;
import com.arrow.selene.device.ble.BleUtils;
import com.arrow.selene.device.ble.beacon.BeaconControllerModule;
import com.arrow.selene.device.ble.beacon.BeaconPacket;
import com.arrow.selene.device.ble.beacon.BeaconPacket.BeaconData;
import com.arrow.selene.engine.Utils;

public class HcidumpAdvertisingReader extends AdvertisingReader {

    private static final int BEACON_ERROR_COUNTER_THRESHOLD = 200;

    private Process hcitoolProcess;
    private Process hcidumpProcess;
    private Thread parserThread;

    private Map<String, Integer> beaconErrorCounter = new HashMap<>();

    private boolean isShuttingDown;

    public HcidumpAdvertisingReader(BeaconControllerModule controller) {
        super(controller);
    }

    @Override
    public void setShuttingDown(boolean shuttingDown) {
        isShuttingDown = shuttingDown;
    }

    @Override
    public void startScan(BeaconControllerModule controller) {
        String method = "startScan";
        try {
            logInfo(method, "starting hcitool process ...");
            ProcessBuilder builder = new ProcessBuilder("hcitool", "-i", controller.getInfo().getBleInterface(),
                    "lescan-passive", "--duplicates");
            hcitoolProcess = builder.start();
            logInfo(method, "hcitool process started");
        } catch (Exception e) {
            String error = "ERROR starting hcitool";
            logError(method, error);
            cleanup();
            throw new SeleneException(error, e);
        }

        // wait for a few seconds before starting hcidump
        try {
            Thread.sleep(2000);
        } catch (Throwable t) {
        }

        try {
            logInfo(method, "starting hcidump process ...");
            ProcessBuilder builder = new ProcessBuilder("hcidump", "-i", controller.getInfo().getBleInterface(),
                    "--raw");
            hcidumpProcess = builder.start();
            logInfo(method, "hcidump process started");
            parserThread = new Thread(new HcidumpParser(hcidumpProcess.getInputStream()), "hcidumpParser");
            parserThread.start();
            logInfo(method, "parserThread started");
        } catch (Exception e) {
            String error = "ERROR starting hcidump";
            logError(method, error);
            cleanup();
            throw new SeleneException(error, e);
        }
    }

    @Override
    public void cleanup() {
        com.arrow.acn.client.utils.Utils.shutdownProcess(hcitoolProcess, "hcitoolProcess");
        com.arrow.acn.client.utils.Utils.shutdownProcess(hcidumpProcess, "hcidumpProcess");
        com.arrow.acn.client.utils.Utils.shutdownThread(parserThread);
    }

    private class HcidumpParser implements Runnable {
        private final InputStream inputStream;

        private HcidumpParser(InputStream inputstream) {
            inputStream = inputstream;
        }

        @Override
        public void run() {
            String method = "parseOutput";
            logDebug(method, "byteorder: %s", ByteOrder.nativeOrder());
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                logDebug(method, "header 1: %s", br.readLine());
                logDebug(method, "header 2: %s", br.readLine());
                String firstLine = null;
                while (!isShuttingDown) {
                    String line;
                    try {
                        StringBuilder sb = new StringBuilder();
                        if (firstLine != null) {
                            sb.append(firstLine);
                        }
                        while (true) {
                            firstLine = br.readLine();
                            if (firstLine != null && firstLine.startsWith(" ")) {
                                sb.append(firstLine);
                            } else {
                                break;
                            }
                        }

                        line = sb.toString();
                        if (line.startsWith("<")) {
                            logDebug(method, "Skipping <: %s", line);
                            continue;
                        }
                        line = line.replace(">", "").replace(" ", "").trim();
                        logDebug(method, "line: %s", line);
                    } catch (IOException e) {
                        logError(method, e);
                        try {
                            Thread.sleep(3000L);
                        } catch (Exception x) {
                        }
                        continue;
                    }

                    if (!line.isEmpty()) {
                        byte[] rawData = Hex.decodeHex(line.toCharArray());
                        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(rawData))) {
                            BeaconPacket packet = new BeaconPacket();

                            packet.setPreamble(dis.readByte());

                            dis.read(packet.getAccessAddress());
                            logDebug(method, "accessAddress: %s", Utils.toBinaryString(packet.getAccessAddress()));

                            dis.read(packet.getPayloadHeader());
                            logDebug(method, "payloadHeader: %s", Utils.toBinaryString(packet.getPayloadHeader()));

                            byte[] address = new byte[6];
                            dis.read(address);
                            String macAddress = BleUtils.formatMacAddress(address);
                            if (ignoredMacSet.contains(macAddress)) {
                                logDebug(method, "ignored: %s", macAddress);
                                continue;
                            }
                            logDebug(method, "mac: %s", macAddress);
                            packet.setBroadcastAddress(macAddress);
                            packet.setAdvDataLength(dis.readByte() & 0xff);
                            logDebug(method, "advDataLength: %d", packet.getAdvDataLength());

                            int remaining = packet.getAdvDataLength();
                            while (!isShuttingDown && remaining > 0) {
                                BeaconData beacon = new BeaconData();
                                beacon.setLength(dis.readByte() & 0xff);
                                logDebug(method, "length: %d", beacon.getLength());
                                beacon.setType(dis.readByte());
                                logDebug(method, "type: %d", beacon.getType());
                                beacon.setData(new byte[beacon.getLength() - 1]);
                                dis.read(beacon.getData());
                                packet.getData().add(beacon);
                                remaining = remaining - beacon.getLength() - 1;
                                logDebug(method, "remaining: %d", remaining);
                            }

                            logDebug(method, "packet parsing complete, forwarding packet ...");
                            BeaconData beaconData = packet.findBeaconData(BeaconPacket.ADV_DATA_TYPE_MANUFACTURER);
                            if (beaconData != null) {
                                String companyId = String.format("0x%02X",
                                        Utils.readSwappedShort(beaconData.getData(), 0));
                                logDebug(method, "found companyId: %s", companyId);

                                String moduleClass = controller.getInfo().findMapping(companyId);
                                if (StringUtils.isNotEmpty(moduleClass)) {
                                    handlePacket(macAddress, packet, companyId, moduleClass);
                                } else {
                                    ignoredMacSet.add(macAddress);
                                    logDebug(method, "unsupported companyId: %s", companyId);
                                }
                            } else {
                                logDebug(method, "beaconData not found");
                                Integer counter = beaconErrorCounter.get(macAddress);
                                if (counter == null) {
                                    counter = 0;
                                }
                                if (counter > BEACON_ERROR_COUNTER_THRESHOLD) {
                                    ignoredMacSet.add(macAddress);
                                    logInfo(method, "added MAC address to ignored set due to many errors: %s",
                                            macAddress);
                                } else {
                                    beaconErrorCounter.put(macAddress, counter + 1);
                                }
                            }
                        } catch (Exception e) {
                            logError(method, e);
                            try {
                                Thread.sleep(3000L);
                            } catch (Exception x) {
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                logError(method, "ERROR", t);
            }
        }
    }
}
