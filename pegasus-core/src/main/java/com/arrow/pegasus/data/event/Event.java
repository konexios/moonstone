package com.arrow.pegasus.data.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;

@Document(collection = "event")
@CompoundIndexes({
		@CompoundIndex(name = "application_id__object_id", background = true, def = "{'applicationId' : 1, 'objectId' : 1}"),
		@CompoundIndex(name = "createdDate", background = true, def = "{'createdDate': 1}") })
public class Event extends AuditableDocumentAbstract {
	private static final long serialVersionUID = -234522342549377113L;

	private String applicationId;
	@NotNull
	private EventType type;
	@NotBlank
	private String name;
	@NotNull
	private EventStatus status;
	private String error;
	private Date expired;
	private String objectId;
	private List<EventParameter> parameters = new ArrayList<>();

	/*----------------------------------------------------------------
	 convenient methods
	 ----------------------------------------------------------------*/

	public boolean typeSecured() {
		return type == EventType.Encrypted;
	}

	public String buildRedisKey(String zoneSystemName) {
		return String.format("%s-EventId-%s", zoneSystemName.toUpperCase(), getId());
	}

	/*----------------------------------------------------------------
	 getters / setters
	 ----------------------------------------------------------------*/

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EventStatus getStatus() {
		return status;
	}

	public void setStatus(EventStatus status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Date getExpired() {
		return expired;
	}

	public void setExpired(Date expired) {
		this.expired = expired;
	}

	public List<EventParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<EventParameter> parameters) {
		this.parameters = parameters;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.EVENT;
	}
}
