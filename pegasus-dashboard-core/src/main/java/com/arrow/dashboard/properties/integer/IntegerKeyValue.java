package com.arrow.dashboard.properties.integer;

import com.arrow.dashboard.properties.KeyValueAbstract;

public class IntegerKeyValue extends KeyValueAbstract<Integer, String> {
	private static final long serialVersionUID = 3038664101130099432L;

	public IntegerKeyValue(Integer key, String value) {
		super(key, value);
	}
}
