package moonstone.selene.device.libelium.data;

import java.io.Serializable;
import java.sql.Timestamp;

public class SensorParser implements Serializable {
	private static final long serialVersionUID = 7596647284191226523L;

	private Integer id;
	private String idWasp;
	private String sensor;
	private String value;
	private Timestamp timestamp;
	private Long sync;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIdWasp() {
		return idWasp;
	}

	public void setIdWasp(String idWasp) {
		this.idWasp = idWasp;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Long getSync() {
		return sync;
	}

	public void setSync(Long sync) {
		this.sync = sync;
	}
}
