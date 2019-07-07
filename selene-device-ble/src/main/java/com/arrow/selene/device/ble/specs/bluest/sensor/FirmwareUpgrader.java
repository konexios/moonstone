package com.arrow.selene.device.ble.specs.bluest.sensor;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

import com.arrow.acn.client.utils.Utils;
import com.arrow.selene.SeleneException;
import com.arrow.selene.device.ble.fwmanagement.FwFileDescriptor;
import com.arrow.selene.device.ble.fwmanagement.ScannableDeviceUtils;
import com.arrow.selene.device.ble.fwmanagement.UpgradeState;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.simba.GattConstants;
import com.arrow.selene.device.ble.specs.BlueST;
import com.arrow.selene.device.sensor.StringSensorData;

public class FirmwareUpgrader extends BleSensorAbstract<FirmwareUpgraderProperties, StringSensorData> {
	public static final String FW = "FirmwareUpgrade";
	public static final int MAX_STRING_SIZE_TO_SENT = 16;
	private long crc;
	private byte[] fwFileFromPortal;
	Boolean isCrcChecked = false;
	boolean rebootMessageReceived = false;
	CheckRebootThread checkRebootThread;
	private UpgradeState upgradeState;

	private static final long DELAY_BEFORE_CHECKING_UPGRADE_STATE = 200;
	private static final long DELAY_BEFORE_CHECK_REBOOT_MESSAGE = 10002;
	private static final String SIMBA_PRO_COMPLETE_MSG_FROM_BOARD = "The Board will resta";
	private static final byte SIMBA_PRO_COMPLETE_BYTE_FROM_BOARD = 0x00;

	private static String UUID = BlueST.UUID_FIRMWARE_UPGRADE;

	public FirmwareUpgrader() {
		super(FW);
	}

	@Override
	public void enable() {
		String method = "enable";
		logInfo(method, "enabling this characteristics for upgrading");
		getBluetoothGatt().enableNotification(UUID, GattConstants.ENABLE_NOTIFICATION);
		super.enable();
	}

	@Override
	public void disable() {
		String method = "disable";
		logInfo(method, "disabling this characteristics after upgrading");
		getBluetoothGatt().disableNotification(UUID, GattConstants.DISABLE_NOTIFICATION);
		super.disable();
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<StringSensorData> parseData(byte[] bytes) {
		List<StringSensorData> data = Collections.singletonList(new StringSensorData(FW, Arrays.toString(bytes)));
		return data;
	}

	@Override
	protected FirmwareUpgraderProperties createProperties() {
		return new FirmwareUpgraderProperties();
	}

	@Override
	public void setTelemetry(String value) {
		String method = "setTelemetry";
		try {
			getBluetoothGatt().controlSensor(UUID, value);
		} catch (Exception e) {
			logError(method, "Error:: %s", e.toString());
			throw new SeleneException("Failed to write upgrade file due to: ", e);
		}
	}

	@Override
	public String[] getUUIDs() {
		return new String[] { UUID };
	}

	@Override
	public boolean isPassive() {
		return false;
	}

	public void upgradeFwFromLoadedFile(File file) {
		String method = "upgradeFwFromLoadedFile";
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			upgradeState = UpgradeState.IN_PROGRESS;
			isCrcChecked = false;
			fwFileFromPortal = new byte[(int) file.length()];
			fileInputStream.read(fwFileFromPortal);
			FwFileDescriptor fileDescriptor = new FwFileDescriptor(fwFileFromPortal);
			long byteToSend = fileDescriptor.getLength();
			crc = ScannableDeviceUtils.computeCrc32(fileDescriptor);
			if (crc != (long) -1) {
				byte[] firmwareCmd = ScannableDeviceUtils.prepareLoadCommand(byteToSend, crc);
				logInfo(method, "firmware load command --- " + Arrays.toString(firmwareCmd));
				setTelemetry(Hex.encodeHexString(firmwareCmd));
			} else {
				logError(method, "Unable to compute crc");
				upgradeState = UpgradeState.FAILED;
			}
		} catch (Exception e) {
			logError(method, "Upgrade failed due to: ", e);
			upgradeState = UpgradeState.FAILED;
		}
	}

	private class CheckRebootThread extends Thread {
		public void run() {
			String method = "CheckRebootThread.run";
			logInfo(method, "Started thread to check reboot message is received");
			Utils.sleep(DELAY_BEFORE_CHECK_REBOOT_MESSAGE);
			if (!rebootMessageReceived) {
				logWarn(method, "Upgrade time out.");
				upgradeState = UpgradeState.TIMEOUT;
			}
		}
	}

	public void onFirmwareCharacteristicsChanged(byte[] bytes) {
		String method = "onFirmwareCharacteristicsChanged";
		if (isCrcChecked != null)
			isCrcChecked = ScannableDeviceUtils.checkCrc(bytes, crc);
		logInfo(method, "crc checked: " + String.valueOf(isCrcChecked));
		String encoded = ScannableDeviceUtils.encodeMessageString(bytes);
		logDebug(method, "encoded message string: " + encoded);
		if ((bytes[0] == SIMBA_PRO_COMPLETE_BYTE_FROM_BOARD) || encoded.contains(SIMBA_PRO_COMPLETE_MSG_FROM_BOARD)) {
			rebootMessageReceived = true;
			logInfo(method, "Upgrade Successful");
			upgradeState = UpgradeState.DONE;
		}
		if (isCrcChecked != null) {
			if (isCrcChecked) {
				isCrcChecked = null;
				try {
					write(fwFileFromPortal, 0, fwFileFromPortal.length);
					checkRebootThread = new CheckRebootThread();
					checkRebootThread.start();
				} catch (Exception e) {
					logError(method, "Upgrade failed due to :", e);
					upgradeState = UpgradeState.FAILED;
				}
			} else {
				logError(method, "wrong crc checksum received");
				upgradeState = UpgradeState.FAILED;
			}
		}
	}

	private int write(byte[] data, int offset, int byteToSend) {
		String method = "write";
		logDebug(method, "write called");
		int byteSend = offset;
		logDebug(method, "byteToSend----" + byteToSend);
		// write the message with chunk of MAX_STRING_SIZE_TO_SENT bytes
		while ((byteToSend - byteSend) > MAX_STRING_SIZE_TO_SENT) {
			byte[] data1 = (Arrays.copyOfRange(data, byteSend, byteSend + MAX_STRING_SIZE_TO_SENT));
			setTelemetry(Hex.encodeHexString(data1));
			byteSend += MAX_STRING_SIZE_TO_SENT;
		}
		// send the remaining data
		if (byteSend != byteToSend) {
			setTelemetry(Hex.encodeHexString(Arrays.copyOfRange(data, byteSend, byteToSend)));
		}
		return byteToSend;
	}

	public void waitForUpgrade() {

		while (upgradeState == UpgradeState.IN_PROGRESS) {
			Utils.sleep(DELAY_BEFORE_CHECKING_UPGRADE_STATE);
		}

		if (upgradeState == UpgradeState.FAILED)
			throw new SeleneException("Upgrade failed!");
		else if (upgradeState == UpgradeState.TIMEOUT)
			throw new SeleneException("Data transmission timeout occurred during upgrade!");
	}

}