package com.arrow.pegasus.web.model;

import java.util.ArrayList;
import java.util.List;

public abstract class ApplicationModelAbstract<T extends ApplicationModelAbstract<T>> extends ModelAbstract<T> {
	private static final long serialVersionUID = 5094564461582963209L;

	private String name;
	private String description;
	private String code;
	private Boolean enabled;
	private String productName;
	private List<String> productExtensionNames = new ArrayList<>();

	public String getName() {
		return name;
	}

	public T withName(String name) {
		setName(name);
		return self();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public T withDescription(String description) {
		setDescription(description);
		return self();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public T withCode(String code) {
		setCode(code);
		return self();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public T withEnabled(Boolean enabled) {
		setEnabled(enabled);
		return self();
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public T withProductName(String productName) {
		setProductName(productName);
		return self();
	}

	public List<String> getProductExtensionNames() {
		return productExtensionNames;
	}

	public void setProductExtensionNames(List<String> productExtensionNames) {
		this.productExtensionNames = productExtensionNames;
	}

	public T withProductExtensionNames(List<String> productExtensionNames) {
		setProductExtensionNames(productExtensionNames);
		return self();
	}
}
