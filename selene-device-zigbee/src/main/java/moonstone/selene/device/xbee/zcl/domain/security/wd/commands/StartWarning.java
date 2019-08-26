package moonstone.selene.device.xbee.zcl.domain.security.wd.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.security.wd.data.SirenLevel;
import moonstone.selene.device.xbee.zcl.domain.security.wd.data.StrobeLevel;
import moonstone.selene.device.xbee.zcl.domain.security.wd.data.WarningMode;

public class StartWarning extends ClusterSpecificCommand<StartWarning> {
	private WarningMode warningMode;
	private boolean strobe;
	private SirenLevel sirenLevel;
	private int warningDuration;
	private int strobeDutyCycle;
	private StrobeLevel strobeLevel;

	public WarningMode getWarningMode() {
		return warningMode;
	}

	public StartWarning withWarningMode(WarningMode warningMode) {
		this.warningMode = warningMode;
		return this;
	}

	public boolean isStrobe() {
		return strobe;
	}

	public StartWarning withStrobe(boolean strobe) {
		this.strobe = strobe;
		return this;
	}

	public SirenLevel getSirenLevel() {
		return sirenLevel;
	}

	public StartWarning withSirenLevel(SirenLevel sirenLevel) {
		this.sirenLevel = sirenLevel;
		return this;
	}

	public int getWarningDuration() {
		return warningDuration;
	}

	public StartWarning withWarningDuration(int warningDuration) {
		this.warningDuration = warningDuration;
		return this;
	}

	public int getStrobeDutyCycle() {
		return strobeDutyCycle;
	}

	public StartWarning withStrobeDutyCycle(int strobeDutyCycle) {
		this.strobeDutyCycle = strobeDutyCycle;
		return this;
	}

	public StrobeLevel getStrobeLevel() {
		return strobeLevel;
	}

	public StartWarning withStrobeLevel(StrobeLevel strobeLevel) {
		this.strobeLevel = strobeLevel;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityWdClusterCommands.START_WARNING_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int mode = warningMode.ordinal();
		if (strobe) {
			mode |= 0b00_010000;
		}
		mode |= sirenLevel.ordinal() << 6;
		buffer.put((byte) mode);
		buffer.putShort((short) warningDuration);
		buffer.put((byte) strobeDutyCycle);
		buffer.put((byte) strobeLevel.ordinal());
		return buffer.array();
	}

	@Override
	public StartWarning fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		byte mode = buffer.get();
		warningMode = WarningMode.values()[mode & 0b00_001111];
		strobe = (mode & 0b00_010000) != 0;
		sirenLevel = SirenLevel.values()[mode >> 6];
		warningDuration = Short.toUnsignedInt(buffer.getShort());
		strobeDutyCycle = Byte.toUnsignedInt(buffer.get());
		strobeLevel = StrobeLevel.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
