package com.arrow.pegasus.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;

@Document(collection = TempToken.COLLECTION_NAME)
public class TempToken extends AuditableDocumentAbstract {
	private static final long serialVersionUID = 3954043164337238018L;
	public static final String COLLECTION_NAME = "pegasus_temp_token";

	private static final boolean DEFAULT_SINGLE_USE = false;
	private static final boolean DEFAULT_EXPIRED = false;
	private static final long DEFUALT_TIME_TO_EXPIRE_SECONDS = 60 * 5;

	private long timeToExpireSeconds = DEFUALT_TIME_TO_EXPIRE_SECONDS;
	private boolean singleUse = DEFAULT_SINGLE_USE;
	private boolean expired = DEFAULT_EXPIRED;

	private Map<String, String> properties = new HashMap<>();

	public long getTimeToExpireSeconds() {
		return timeToExpireSeconds;
	}

	public void setTimeToExpireSeconds(long timeToExpireSeconds) {
		this.timeToExpireSeconds = timeToExpireSeconds;
	}

	public boolean isSingleUse() {
		return singleUse;
	}

	public void setSingleUse(boolean singleUse) {
		this.singleUse = singleUse;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void addProperty(String name, String value) {
		properties.put(name, value);
	}

	public String getCompanyId() {
		return properties.get("companyId");
	}

	public void setCompanyId(String companyId) {
		properties.put("companyId", companyId);
	}

	public String getApplicationId() {
		return properties.get("applicationId");
	}

	public void setApplicationId(String applicationId) {
		properties.put("applicationId", applicationId);
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.TEMP_TOKEN;
	}
}