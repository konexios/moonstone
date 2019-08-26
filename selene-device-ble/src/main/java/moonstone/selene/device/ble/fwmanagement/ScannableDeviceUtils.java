package moonstone.selene.device.ble.fwmanagement;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.Checksum;

import moonstone.selene.Loggable;
import moonstone.selene.device.ble.Utils;

public class ScannableDeviceUtils extends Loggable {
	private static final Loggable LOGGER = new Loggable();
	public static final int MAX_STRING_SIZE_TO_SENT = 20;
	public static final int MAC_ADDRESS_LENGTH = 6;
	static private final byte[] UPLOAD_BOARD_FW = { 'u', 'p', 'g', 'r', 'a', 'd', 'e', 'F', 'w' };

	/**
	 * merge the file size and crc to create the command that will start the
	 * upload on the board
	 *
	 * @param fileSize
	 *            number of file to send
	 * @param fileCrc
	 *            file crc
	 * @return command to send to the board
	 */
	public static byte[] prepareLoadCommand(long fileSize, long fileCrc) {
		byte[] command;
		int offset;
		offset = UPLOAD_BOARD_FW.length;
		command = new byte[offset + 8];
		System.arraycopy(UPLOAD_BOARD_FW, 0, command, 0, offset);

		byte temp[] = Utils.LittleEndian.uint32ToBytes(fileSize);
		System.arraycopy(temp, 0, command, offset, temp.length);
		offset += temp.length;
		temp = Utils.LittleEndian.uint32ToBytes(fileCrc);
		System.arraycopy(temp, 0, command, offset, temp.length);

		return command;
	}

	public static long computeCrc32(FwFileDescriptor file) {
		String method = "computeCrc32";
		Checksum crc = new Stm32Crc32();
		byte buffer[] = new byte[4];
		BufferedInputStream inputStream = new BufferedInputStream(file.openFile());
		// the file must be multiple of 32bit,
		long fileSize = file.getLength() - file.getLength() % 4;
		try {
			for (long i = 0; i < fileSize; i += 4) {
				if (inputStream.read(buffer) == buffer.length)
					crc.update(buffer, 0, buffer.length);
			}
		} catch (IOException e) {
			LOGGER.logError(method, "Failed to compute crc");
			return -1;
		}
		return crc.getValue();
	}

	public static String encodeMessageString(byte[] value) {
		return new String(value, StandardCharsets.ISO_8859_1);
	}

	public static boolean checkCrc(byte[] message, long sourceCrc) {
		byte myCrc[] = Utils.LittleEndian.uint32ToBytes(sourceCrc);
		return Arrays.equals(message, myCrc);
	}
}
