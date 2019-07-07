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
package com.arrow.acs.client.model;

import java.io.Serializable;

public abstract class ModelAbstract<T extends ModelAbstract<T>> implements Serializable {
	private static final long serialVersionUID = 5491748225878069743L;

	public final static String SELF_LINK = "self";

	private String hid;
	private String pri;

	protected abstract T self();

	public T withHid(String hid) {
		setHid(hid);
		return self();
	}

	public T withPri(String pri) {
		setPri(pri);
		return self();
	}

	public String getPri() {
		return pri;
	}

	public void setPri(String pri) {
		this.pri = pri;
	}

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hid == null) ? 0 : hid.hashCode());
		result = prime * result + ((pri == null) ? 0 : pri.hashCode());
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
		ModelAbstract<?> other = (ModelAbstract<?>) obj;
		if (hid == null) {
			if (other.hid != null)
				return false;
		} else if (!hid.equals(other.hid))
			return false;
		if (pri == null) {
			if (other.pri != null)
				return false;
		} else if (!pri.equals(other.pri))
			return false;
		return true;
	}
}
