package moonstone.selene.device.conduit;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;
import moonstone.selene.engine.DeviceDataAbstract;
import moonstone.selene.engine.EngineConstants;

public class LoraDeviceData extends DeviceDataAbstract {
	private String time;
	private Long tmst;
	private Double freq;
	private Long chan;
	private Long rfch;
	private Long stat;
	private String modu;
	private String datr;
	private String codr;
	private Long rssi;
	private Double lsnr;
	private Long size;
	private String data;
	private String mhdr;
	private String opts;
	private Long port;
	private Long seqn;

	@Override
	public IotParameters writeIoTParameters() {
		IotParameters result = getParsedIotParameters();
		if (result == null) {
			result = new IotParameters();
		}
		if (!isParsedFully()) {
			if (StringUtils.isNotEmpty(time))
				result.setString("time", time);
			if (tmst != null)
				result.setLong("tmst", tmst);
			if (freq != null)
				result.setDouble("freq", freq, EngineConstants.FORMAT_DECIMAL_2);
			if (chan != null)
				result.setLong("chan", chan);
			if (rfch != null)
				result.setLong("rfch", rfch);
			if (stat != null)
				result.setLong("stat", stat);
			if (StringUtils.isNotEmpty(modu))
				result.setString("modu", modu);
			if (StringUtils.isNotEmpty(datr))
				result.setString("datr", datr);
			if (StringUtils.isNotEmpty(codr))
				result.setString("codr", codr);
			if (rssi != null)
				result.setLong("rssi", rssi);
			if (lsnr != null)
				result.setDouble("lsnr", lsnr, EngineConstants.FORMAT_DECIMAL_2);
			if (size != null)
				result.setLong("size", size);
			if (StringUtils.isNotEmpty(data))
				result.setString("data", data);
			if (StringUtils.isNotEmpty(mhdr))
				result.setString("mhdr", mhdr);
			if (StringUtils.isNotEmpty(opts))
				result.setString("opts", opts);
			if (port != null)
				result.setLong("port", port);
			if (seqn != null)
				result.setLong("seqn", seqn);
		}
		return result;
	}

	@Override
	public List<Telemetry> writeTelemetries() {
		List<Telemetry> result = getParsedTelemetries();
		if (result == null) {
			result = new ArrayList<>();
		}
		if (!isParsedFully()) {
			if (StringUtils.isNotEmpty(time))
				result.add(writeStringTelemetry(TelemetryItemType.String, "time", time));
			if (tmst != null)
				result.add(writeIntTelemetry("tmst", tmst));
			if (freq != null)
				result.add(writeFloatTelemetry("freq", freq));
			if (chan != null)
				result.add(writeIntTelemetry("chan", chan));
			if (rfch != null)
				result.add(writeIntTelemetry("rfch", rfch));
			if (stat != null)
				result.add(writeIntTelemetry("stat", stat));
			if (StringUtils.isNotEmpty(modu))
				result.add(writeStringTelemetry(TelemetryItemType.String, "modu", modu));
			if (StringUtils.isNotEmpty(datr))
				result.add(writeStringTelemetry(TelemetryItemType.String, "datr", datr));
			if (StringUtils.isNotEmpty(codr))
				result.add(writeStringTelemetry(TelemetryItemType.String, "codr", codr));
			if (rssi != null)
				result.add(writeIntTelemetry("rssi", rssi));
			if (lsnr != null)
				result.add(writeFloatTelemetry("lsnr", lsnr));
			if (size != null)
				result.add(writeIntTelemetry("size", size));
			if (StringUtils.isNotEmpty(data))
				result.add(writeStringTelemetry(TelemetryItemType.String, "data", data));
			if (StringUtils.isNotEmpty(mhdr))
				result.add(writeStringTelemetry(TelemetryItemType.String, "mhdr", mhdr));
			if (StringUtils.isNotEmpty(opts))
				result.add(writeStringTelemetry(TelemetryItemType.String, "opts", opts));
			if (port != null)
				result.add(writeIntTelemetry("port", port));
			if (seqn != null)
				result.add(writeIntTelemetry("seqn", seqn));
		}
		return result;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Long getTmst() {
		return tmst;
	}

	public void setTmst(Long tmst) {
		this.tmst = tmst;
	}

	public Double getFreq() {
		return freq;
	}

	public void setFreq(Double freq) {
		this.freq = freq;
	}

	public Long getChan() {
		return chan;
	}

	public void setChan(Long chan) {
		this.chan = chan;
	}

	public Long getRfch() {
		return rfch;
	}

	public void setRfch(Long rfch) {
		this.rfch = rfch;
	}

	public Long getStat() {
		return stat;
	}

	public void setStat(Long stat) {
		this.stat = stat;
	}

	public String getModu() {
		return modu;
	}

	public void setModu(String modu) {
		this.modu = modu;
	}

	public String getDatr() {
		return datr;
	}

	public void setDatr(String datr) {
		this.datr = datr;
	}

	public String getCodr() {
		return codr;
	}

	public void setCodr(String codr) {
		this.codr = codr;
	}

	public Long getRssi() {
		return rssi;
	}

	public void setRssi(Long rssi) {
		this.rssi = rssi;
	}

	public Double getLsnr() {
		return lsnr;
	}

	public void setLsnr(Double lsnr) {
		this.lsnr = lsnr;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMhdr() {
		return mhdr;
	}

	public void setMhdr(String mhdr) {
		this.mhdr = mhdr;
	}

	public String getOpts() {
		return opts;
	}

	public void setOpts(String opts) {
		this.opts = opts;
	}

	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
	}

	public Long getSeqn() {
		return seqn;
	}

	public void setSeqn(Long seqn) {
		this.seqn = seqn;
	}
}
