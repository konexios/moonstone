package com.arrow.pegasus.dashboard.data;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;

public abstract class ContainerAbstract extends AuditableDocumentAbstract {
	private static final long serialVersionUID = 2620545355336193679L;

	@NotBlank
	private String name;
	private String description;
	private String layoutClass;
	private String layout;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLayoutClass() {
		return layoutClass;
	}

	public void setLayoutClass(String layoutClass) {
		this.layoutClass = layoutClass;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public Object getLayoutValue() {
		if (!StringUtils.isEmpty(layout) && !StringUtils.isEmpty(layoutClass)) {
			try {
				return JsonUtils.fromJson(layout, Class.forName(layoutClass));
			} catch (ClassNotFoundException e) {
				throw new AcsLogicalException("Invalid layoutClass: " + layoutClass);
			}
		} else {
			return layout;
		}
	}

	public <T> T getLayoutValue(TypeReference<T> typeRef) {
		if (StringUtils.isEmpty(layout)) {
			return null;
		} else {
			return JsonUtils.fromJson(layout, typeRef);
		}
	}
}