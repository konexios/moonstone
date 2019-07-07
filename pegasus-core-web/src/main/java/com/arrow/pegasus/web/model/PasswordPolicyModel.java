package com.arrow.pegasus.web.model;

import java.io.Serializable;

public class PasswordPolicyModel implements Serializable {
	private static final long serialVersionUID = 2069452155582968120L;

	private int minLength;
	private int maxLength;
	private int minLowerCase;
	private int minUpperCase;
	private int minDigit;
	private int minSpecial;
	private int historical;
	private boolean allowWhitespace;

	public PasswordPolicyModel() {
	}

	public PasswordPolicyModel withMinLength(int minLength) {
		setMinLength(minLength);
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
}
