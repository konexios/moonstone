package moonstone.selene.device.harting.rfid.config.r500;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import moonstone.selene.device.harting.rfid.config.ConfigParameter;

public class LinuxSystemTools implements ConfigParameter<LinuxSystemTools> {
	private static final long serialVersionUID = -7536906210542125655L;

	private Set<BasicTools> basicTools;

	private static final List<String> ALL = new ArrayList<>();

	public LinuxSystemTools(Set<BasicTools> basicTools) {
		this.basicTools = basicTools;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(30);
		buffer.put(0, (byte) BasicTools.build(basicTools));
		return buffer.array();
	}

	@Override
	public LinuxSystemTools parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		Set<BasicTools> basicTools = BasicTools.extract(buffer.get(0));
		return new LinuxSystemTools(basicTools);
	}

	@Override
	public int getId() {
		return 0;
	}

	enum BasicTools {
		TELNET,
		SSH,
		WEB_SERVER,
		FTP_SERVER;

		public static int build(Iterable<BasicTools> value) {
			int result = 0;
			for (BasicTools item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}

		public static Set<BasicTools> extract(int value) {
			Set<BasicTools> result = EnumSet.noneOf(BasicTools.class);
			for (BasicTools item : values()) {
				if ((value >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}
	}

	@Override
	public boolean updateState(String name, String value) {
		return false;
	}

	@Override
	public Map<String, String> getStates() {
		return Collections.emptyMap();
	}

	@Override
	public List<String> getParams() {
		return ALL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LinuxSystemTools that = (LinuxSystemTools) o;
		return Objects.equals(basicTools, that.basicTools);
	}

	@Override
	public int hashCode() {

		return Objects.hash(basicTools);
	}
}
