package moonstone.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import moonstone.selene.device.harting.rfid.command.Inventory.AntennaSelector;
import moonstone.selene.device.harting.rfid.command.ReadBufferResponse.Dataset.AntennaExtended;
import moonstone.selene.engine.Utils;

public class ReadBufferResponse extends HostCommandResponse<ReadBufferResponse> {
	private static final long serialVersionUID = -4784025267674644502L;
	private int status;
	private List<Dataset> datasets;

	public ReadBufferResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public List<Dataset> getDatasets() {
		return datasets;
	}

	public ReadBufferResponse withDatasets(List<Dataset> datasets) {
		this.datasets = datasets;
		return this;
	}

	public ReadBufferResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int status = Byte.toUnsignedInt(buffer.get(2));
		ReadBufferResponse result = new ReadBufferResponse();
		result.withStatus(status);
		if (status == 0) {
			result.withDatasets(parseDatasets(buffer));
		}
		return result;
	}

	private static List<Dataset> parseDatasets(ByteBuffer buffer) {
		byte trData1 = buffer.get(3);
		boolean exFlag = (trData1 & 0b10000000) != 0;
		boolean timer = (trData1 & 0b00100000) != 0;
		boolean antennaNumber = (trData1 & 0b00010000) != 0;
		boolean lsbFirst = (trData1 & 0b00001000) != 0;
		boolean db = (trData1 & 0b00000010) != 0;
		boolean idd = (trData1 & 0b00000001) != 0;
		byte trData2 = exFlag ? buffer.get(4) : 0;
		boolean antExt = (trData2 & 0b00010000) != 0;
		boolean in = (trData2 & 0b00000001) != 0;
		int shift = exFlag ? 1 : 0;
		int dataSetsLength = Short.toUnsignedInt(buffer.getShort(4 + shift));
		List<Dataset> result = new ArrayList<>(dataSetsLength);
		for (int i = 0; i < dataSetsLength; i++) {
			Dataset dataset = getShift(buffer, timer, antennaNumber, lsbFirst, idd, antExt, in, shift);
			result.add(dataset);
			shift += dataset.getLength();
		}
		return result;
	}

	private static Dataset getShift(ByteBuffer buffer, boolean timer, boolean antennaNumber, boolean lsbFirst,
	                                boolean idd, boolean antExt, boolean in, int shift) {
		if (lsbFirst) {
			buffer.order(ByteOrder.LITTLE_ENDIAN);
		}
		int length = Short.toUnsignedInt(buffer.getShort(6 + shift));
		shift += 2;
		int trType = 0;
		int iddt = 0;
		int idLength;
		byte[] id = Utils.EMPTY_BYTE_ARRAY;
		if (idd) {
			trType = Byte.toUnsignedInt(buffer.get(6 + shift));
			iddt = Byte.toUnsignedInt(buffer.get(7 + shift));
			idLength = Byte.toUnsignedInt(buffer.get(8 + shift));
			id = new byte[idLength];
			buffer.position(9 + shift);
			buffer.get(id);
			shift += idLength + 3;
		}
		LocalTime time = LocalTime.now();
		if (timer) {
			int hour = Byte.toUnsignedInt(buffer.get(6 + shift));
			int minute = Byte.toUnsignedInt(buffer.get(7 + shift));
			int millisecond = Short.toUnsignedInt(buffer.getShort(8 + shift));
			time = LocalTime.of(hour, minute, millisecond / 1000, 1000 * 1000 * (millisecond % 1000));
			shift += 4;
		}
		Set<AntennaExtended> antennas = new HashSet<>();
		if (antennaNumber) {
			Set<AntennaSelector> antennaSelectors = AntennaSelector.extract(Byte.toUnsignedInt(buffer.get(6 + shift)));
			for (AntennaSelector selector : antennaSelectors) {
				antennas.add(new AntennaExtended().withAntennaSelector(selector));
			}
			shift++;
		}
		int input = 0;
		int state = 0;
		if (in) {
			input = Byte.toUnsignedInt(buffer.get(6 + shift));
			state = Byte.toUnsignedInt(buffer.get(7 + shift));
			shift += 2;
		}
		if (antExt) {
			int numberOfAntennas = Byte.toUnsignedInt(buffer.get(6 + shift));
			shift++;
			for (int i = 0; i < numberOfAntennas; i++) {
				int antenna = Byte.toUnsignedInt(buffer.get(6 + shift));
				int rssi = -Byte.toUnsignedInt(buffer.get(7 + shift));
				int phaseAngle = Short.toUnsignedInt(buffer.getShort(8 + shift)) * 360 / 4096;
				antennas.add(new AntennaExtended().withAntennaSelector(AntennaSelector.values()[antenna - 1])
						.withRssi(rssi).withPhaseAngle(phaseAngle));
				shift += 6;
			}
		}
		return new Dataset().withTrType(trType).withIddt(iddt).withId(id).withTime(time).withAntennas(antennas)
				.withInput(input).withState(state).withLength(length);
	}

	public static class Dataset {
		int trType;
		int iddt;
		byte[] id;
		LocalTime time = LocalTime.now();
		Set<AntennaExtended> antennas;
		int input;
		int state;
		int length;

		public int getTrType() {
			return trType;
		}

		public Dataset withTrType(int trType) {
			this.trType = trType;
			return this;
		}

		public int getIddt() {
			return iddt;
		}

		public Dataset withIddt(int iddt) {
			this.iddt = iddt;
			return this;
		}

		public byte[] getId() {
			return id;
		}

		public Dataset withId(byte[] id) {
			this.id = id;
			return this;
		}

		public LocalTime getTime() {
			return time;
		}

		public Dataset withTime(LocalTime time) {
			this.time = time;
			return this;
		}

		public Set<AntennaExtended> getAntennas() {
			return antennas;
		}

		public Dataset withAntennas(Set<AntennaExtended> antennas) {
			this.antennas = antennas;
			return this;
		}

		public int getInput() {
			return input;
		}

		public Dataset withInput(int input) {
			this.input = input;
			return this;
		}

		public int getState() {
			return state;
		}

		public Dataset withState(int state) {
			this.state = state;
			return this;
		}

		public int getLength() {
			return length;
		}

		public Dataset withLength(int length) {
			this.length = length;
			return this;
		}

		public static class AntennaExtended {
			private AntennaSelector antennaSelector;
			private int rssi;
			private double phaseAngle;

			public AntennaSelector getAntennaSelector() {
				return antennaSelector;
			}

			public AntennaExtended withAntennaSelector(AntennaSelector antennaSelector) {
				this.antennaSelector = antennaSelector;
				return this;
			}

			public int getRssi() {
				return rssi;
			}

			public AntennaExtended withRssi(int rssi) {
				this.rssi = rssi;
				return this;
			}

			public double getPhaseAngle() {
				return phaseAngle;
			}

			public AntennaExtended withPhaseAngle(double phaseAngle) {
				this.phaseAngle = phaseAngle;
				return this;
			}

			@Override
			public String toString() {
				return "AntennaExtended{" + "antennaSelector=" + antennaSelector + ", rssi=" + rssi + ", phaseAngle=" +
						phaseAngle + '}';
			}
		}

		@Override
		public String toString() {
			return "Dataset{" + "trType=" + trType + ", iddt=" + iddt + ", id=" + Arrays.toString(id) + ", time=" +
					time + ", antennas=" + antennas + ", input=" + input + ", state=" + state + '}';
		}
	}
}
