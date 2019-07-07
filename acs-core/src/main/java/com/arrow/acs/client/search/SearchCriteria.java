/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acs.client.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public abstract class SearchCriteria {
	protected Map<String, String> simpleCriteria = new HashMap<>();
	protected Map<String, String[]> arrayCriteria = new HashMap<>();

	public Map<String, String> getSimpleCriteria() {
		return Collections.unmodifiableMap(simpleCriteria);
	}

	public Map<String, String[]> getArrayCriteria() {
		return Collections.unmodifiableMap(arrayCriteria);
	}

	public List<NameValuePair> getAllCriteria() {
		List<NameValuePair> pairs = new ArrayList<>();
		for (Entry<String, String> entry : simpleCriteria.entrySet()) {
			String value = entry.getValue();
			if (!value.isEmpty()) {
				pairs.add(new BasicNameValuePair(entry.getKey(), value));
			}
		}
		for (Entry<String, String[]> entry : arrayCriteria.entrySet()) {
			String name = entry.getKey();
			for (String value : entry.getValue()) {
				if (!value.isEmpty()) {
					pairs.add(new BasicNameValuePair(name, value));
				}
			}
		}
		return pairs;
	}

	public String toString() {
		StringBuilder result = new StringBuilder("?");
		for (NameValuePair entry : getAllCriteria()) {
			result.append(entry.getName()).append('=').append(entry.getValue()).append('&');
		}
		return result.substring(0, result.length() - 1);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrayCriteria == null) ? 0 : arrayCriteria.hashCode());
		result = prime * result + ((simpleCriteria == null) ? 0 : simpleCriteria.hashCode());
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
		SearchCriteria other = (SearchCriteria) obj;
		if (arrayCriteria == null) {
			if (other.arrayCriteria != null)
				return false;
		} else if (!arrayCriteria.equals(other.arrayCriteria))
			return false;
		if (simpleCriteria == null) {
			if (other.simpleCriteria != null)
				return false;
		} else if (!simpleCriteria.equals(other.simpleCriteria))
			return false;
		return true;
	}

}
