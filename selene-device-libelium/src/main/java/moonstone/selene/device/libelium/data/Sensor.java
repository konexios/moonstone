package moonstone.selene.device.libelium.data;

import java.io.Serializable;

public class Sensor implements Serializable {
	private static final long serialVersionUID = 1558894669873165980L;

	private Integer id;
	private String name;
	private String description;
	private String idAscii;
	private String units;
	private Integer value;
	private Integer vis;
	private Integer fields;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdAscii() {
		return idAscii;
	}

	public void setIdAscii(String idAscii) {
		this.idAscii = idAscii;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getVis() {
		return vis;
	}

	public void setVis(Integer vis) {
		this.vis = vis;
	}

	public Integer getFields() {
		return fields;
	}

	public void setFields(Integer fields) {
		this.fields = fields;
	}
}
