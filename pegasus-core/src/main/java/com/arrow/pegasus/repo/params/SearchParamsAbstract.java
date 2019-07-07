package com.arrow.pegasus.repo.params;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class SearchParamsAbstract implements Serializable {

	private static final long serialVersionUID = 1502447505034923575L;

	protected Set<String> addValues(Set<String> valueSet, String... values) {
		if (values != null) {
			if (valueSet == null)
				valueSet = new HashSet<>();

			for (String value : values)
				if (value != null)
					valueSet.add(value);
		}

		return valueSet;
	}

	protected Set<Object> addValues(Set<Object> valueSet, Object... values) {
		if (values != null) {
			if (valueSet == null)
				valueSet = new HashSet<>();

			for (Object value : values)
				if (value != null)
					valueSet.add(value);
		}

		return valueSet;
	}

	protected Set<String> getValues(Set<String> valueSet) {
		return valueSet == null ? Collections.emptySet() : Collections.unmodifiableSet(valueSet);
	}
}
