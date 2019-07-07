package com.arrow.pegasus.data;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.arrow.pegasus.CoreConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class DocumentAbstract implements Serializable {
	private static final long serialVersionUID = -2741014093846324142L;

	@Id
	private String id;

	@Indexed
	private String hid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	@JsonIgnore
	public String getPri() {
		return String.format("%s:%s:%s:%s", CoreConstant.ROOT_PRI, getProductPri(), getTypePri(), hid);
	}

	@JsonIgnore
	protected abstract String getProductPri();

	@JsonIgnore
	protected abstract String getTypePri();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentAbstract other = (DocumentAbstract) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
