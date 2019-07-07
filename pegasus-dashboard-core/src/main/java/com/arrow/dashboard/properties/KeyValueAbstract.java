package com.arrow.dashboard.properties;

import java.io.Serializable;

public class KeyValueAbstract<K, V> implements Serializable {
	private static final long serialVersionUID = -4370444640319553368L;

	private K key;
	private V value;

	public KeyValueAbstract(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}
}
