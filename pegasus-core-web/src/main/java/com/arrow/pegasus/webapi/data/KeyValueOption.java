package com.arrow.pegasus.webapi.data;

import java.io.Serializable;

public class KeyValueOption implements Serializable, Comparable<KeyValueOption> {
	private static final long serialVersionUID = -2732014411828515794L;

	private String key;
	private String value;

	public KeyValueOption() {
	}

	public KeyValueOption(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		KeyValueOption other = (KeyValueOption) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public int compareTo(KeyValueOption o) {
		return value.compareTo(o.value);
	}
}
