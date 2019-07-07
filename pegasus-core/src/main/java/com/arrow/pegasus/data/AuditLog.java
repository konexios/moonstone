package com.arrow.pegasus.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = AuditLog.COLLECTION_NAME)
public class AuditLog extends TsDocumentAbstract {
	private static final long serialVersionUID = -5667918345700568710L;
	public static final String COLLECTION_NAME = "audit_log";

	@NotBlank
	private String productName;
	private String applicationId;
	@NotBlank
	private String type;
	@Indexed
	private String objectId;
	private List<String> relatedIds = new ArrayList<>();
	private Map<String, String> parameters = new HashMap<>();

	@Transient
	@JsonIgnore
	private Application refApplication;

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<String> getRelatedIds() {
		return relatedIds;
	}

	public void setRelatedIds(List<String> relatedIds) {
		this.relatedIds = relatedIds;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.AUDIT_LOG;
	}
}
