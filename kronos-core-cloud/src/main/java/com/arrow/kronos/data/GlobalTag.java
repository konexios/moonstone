package com.arrow.kronos.data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.AuditableDocumentAbstract;

@Document(collection = "global_tag")
public class GlobalTag extends AuditableDocumentAbstract {

	private static final long serialVersionUID = -5170213840872239133L;

	@NotBlank
	private String name;
	@NotNull
	private GlobalTagType tagType;
	@NotBlank
	private String objectType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GlobalTagType getTagType() {
		return tagType;
	}

	public void setTagType(GlobalTagType tagType) {
		this.tagType = tagType;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.GLOBAL_TAG;
	}

}
