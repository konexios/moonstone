package com.arrow.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InventoryResponse extends HostCommandResponse<InventoryResponse> {
	private static final long serialVersionUID = -939599337026750274L;

	private int status;
	private List<Dataset> datasets;

	public int getStatus() {
		return status;
	}

	public List<Dataset> getDatasets() {
		return datasets;
	}

	public InventoryResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public InventoryResponse withDatasets(List<Dataset> datasets) {
		this.datasets = datasets;
		return this;
	}

	public static class Dataset {
		private Flags flags = new Flags();
		private int trType;
		private int iddt;
		private byte[] id;
		private Set<Antenna> antennas;

		public Flags getFlags() {
			return flags;
		}

		public int getTrType() {
			return trType;
		}

		public int getIddt() {
			return iddt;
		}

		public byte[] getId() {
			return id;
		}

		public Set<Antenna> getAntennas() {
			return antennas;
		}

		public Dataset withFlags(Flags flags) {
			this.flags = flags;
			return this;
		}

		public Dataset withTrType(int trType) {
			this.trType = trType;
			return this;
		}

		public Dataset withIddt(int iddt) {
			this.iddt = iddt;
			return this;
		}

		public Dataset withId(byte[] id) {
			this.id = id;
			return this;
		}

		public Dataset withAntennas(Set<Antenna> antennas) {
			this.antennas = antennas;
			return this;
		}

		public static Dataset parse(int mode, byte... payload) {
			ByteBuffer buffer = ByteBuffer.wrap(payload);
			Dataset result = new Dataset();
			if (mode == 1) {
				Flags flags = Flags.parse(Byte.toUnsignedInt(buffer.get()));
				result.withFlags(flags);
			}
			int trType = Byte.toUnsignedInt(buffer.get());
			int iddt = Byte.toUnsignedInt(buffer.get());
			result.withTrType(trType).withIddt(iddt);
			if (mode == 0 || result.getFlags().isIdd()) {
				byte[] id = new byte[Byte.toUnsignedInt(buffer.get())];
				buffer.get(id);
				result.withId(id);
			}
			if (result.getFlags().isAntennas()) {
				Set<Antenna> antennas = new HashSet<>();
				int size = Byte.toUnsignedInt(buffer.get());
				for (int i = 0; i < size; i++) {
					byte[] antenna = new byte[7];
					buffer.get(antenna);
					antennas.add(Antenna.parse(antenna));
				}
				result.withAntennas(antennas);
			}
			return result;
		}
	}

	public static class Flags {
		private boolean idd;
		private boolean antennas;

		public boolean isIdd() {
			return idd;
		}

		public boolean isAntennas() {
			return antennas;
		}

		public Flags withIdd(boolean idd) {
			this.idd = idd;
			return this;
		}

		public Flags withAntennas(boolean antennas) {
			this.antennas = antennas;
			return this;
		}

		public static Flags parse(int value) {
			boolean idd = (value & 0x01) == 0x01;
			boolean antennas = (value & 0x10) == 0x10;
			return new Flags().withIdd(idd).withAntennas(antennas);
		}
	}

	public static class Antenna {
		private int antennaNumber;
		private int antennaStatus;
		private int rssi;
		private double phaseAngle;

		public int getAntennaNumber() {
			return antennaNumber;
		}

		public int getAntennaStatus() {
			return antennaStatus;
		}

		public int getRssi() {
			return rssi;
		}

		public double getPhaseAngle() {
			return phaseAngle;
		}

		public Antenna withAntennaNumber(int antennaNumber) {
			this.antennaNumber = antennaNumber;
			return this;
		}

		public Antenna withAntennaStatus(int antennaStatus) {
			this.antennaStatus = antennaStatus;
			return this;
		}

		public Antenna withRssi(int rssi) {
			this.rssi = rssi;
			return this;
		}

		public Antenna withPhaseAngle(double phaseAngle) {
			this.phaseAngle = phaseAngle;
			return this;
		}

		public static Antenna parse(byte... payload) {
			ByteBuffer buffer = ByteBuffer.wrap(payload);
			int antennaNumber = Byte.toUnsignedInt(buffer.get());
			int antennaStatus = Byte.toUnsignedInt(buffer.get());
			int rssi = Byte.toUnsignedInt(buffer.get());
			int phaseAngle = Short.toUnsignedInt(buffer.getShort()) * 360 / 4096;
			return new Antenna().withAntennaNumber(antennaNumber).withAntennaStatus(antennaStatus).withRssi(rssi)
					.withPhaseAngle(phaseAngle);
		}
	}

	@Override
	public InventoryResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int status = Byte.toUnsignedInt(buffer.get(2));
		InventoryResponse result = new InventoryResponse();
		result.withStatus(status);
		if (status == 0) {
			int size = Byte.toUnsignedInt(buffer.get(3));
			int shift = 4;
			List<Dataset> datasets = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				buffer.position(shift);
				byte[] data = new byte[buffer.remaining()];
				buffer.get(data);
				Dataset dataset = Dataset.parse(mode, data);
				datasets.add(dataset);
				shift += 2 + (mode == 1 ? 1 : 0) +
						(mode == 0 || dataset.getFlags().isIdd() ? 1 + dataset.getId().length : 0) +
						(dataset.getFlags().isAntennas() ? 1 + dataset.getAntennas().size() * 7 : 0);
			}
			result.withDatasets(datasets);
		}
		return result;
	}
}
