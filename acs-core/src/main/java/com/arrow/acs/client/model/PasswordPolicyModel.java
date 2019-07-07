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

public class PasswordPolicyModel implements Serializable {
	private static final long serialVersionUID = -8165095241355450047L;

	private int minLength;
	private int maxLength;
	private int minLowerCase;
	private int minUpperCase;
	private int minDigit;
	private int minSpecial;
	private int historical;
	private boolean allowWhitespace;

	public PasswordPolicyModel withMinLength(int minLength) {
		setMinDigit(minLength);
		return this;
	}

	public PasswordPolicyModel withMaxLength(int maxLength) {
		setMaxLength(maxLength);
		return this;
	}

	public PasswordPolicyModel withMinLowerCase(int minLowerCase) {
		setMinLowerCase(minLowerCase);
		return this;
	}

	public PasswordPolicyModel withMinUpperCase(int minUpperCase) {
		setMinUpperCase(minUpperCase);
		return this;
	}

	public PasswordPolicyModel withMinDigit(int minDigit) {
		setMinDigit(minDigit);
		return this;
	}

	public PasswordPolicyModel withMinSpecial(int minSpecial) {
		setMinSpecial(minSpecial);
		return this;
	}

	public PasswordPolicyModel withHistorical(int historical) {
		setHistorical(historical);
		return this;
	}

	public PasswordPolicyModel withAllowWhitespace(boolean allowWhitespace) {
		setAllowWhitespace(allowWhitespace);
		return this;
	}

	public boolean isAllowWhitespace() {
		return allowWhitespace;
	}

	public void setAllowWhitespace(boolean allowWhitespace) {
		this.allowWhitespace = allowWhitespace;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getMinLowerCase() {
		return minLowerCase;
	}

	public void setMinLowerCase(int minLowerCase) {
		this.minLowerCase = minLowerCase;
	}

	public int getMinUpperCase() {
		return minUpperCase;
	}

	public void setMinUpperCase(int minUpperCase) {
		this.minUpperCase = minUpperCase;
	}

	public int getMinDigit() {
		return minDigit;
	}

	public void setMinDigit(int minDigit) {
		this.minDigit = minDigit;
	}

	public int getMinSpecial() {
		return minSpecial;
	}

	public void setMinSpecial(int minSpecial) {
		this.minSpecial = minSpecial;
	}

	public int getHistorical() {
		return historical;
	}

	public void setHistorical(int historical) {
		this.historical = historical;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (allowWhitespace ? 1231 : 1237);
		result = prime * result + historical;
		result = prime * result + maxLength;
		result = prime * result + minDigit;
		result = prime * result + minLength;
		result = prime * result + minLowerCase;
		result = prime * result + minSpecial;
		result = prime * result + minUpperCase;
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
		PasswordPolicyModel other = (PasswordPolicyModel) obj;
		if (allowWhitespace != other.allowWhitespace)
			return false;
		if (historical != other.historical)
			return false;
		if (maxLength != other.maxLength)
			return false;
		if (minDigit != other.minDigit)
			return false;
		if (minLength != other.minLength)
			return false;
		if (minLowerCase != other.minLowerCase)
			return false;
		if (minSpecial != other.minSpecial)
			return false;
		if (minUpperCase != other.minUpperCase)
			return false;
		return true;
	}
}
