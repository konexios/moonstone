package com.arrow.pegasus.web.model;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AccessPrivilege;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

import java.io.Serializable;
import java.util.List;

public abstract class AccessKeyModelAbstract<T extends AccessKeyModelAbstract<T>> extends CoreDocumentModel {
	private static final long serialVersionUID = 140217346688848068L;

	private String companyId;
	private String name;
	private String rawApiKey;
	private String rawSecretKey;
	private List<AccessPrivilegeModel> privileges;
	private String aes128ApiKey;
	private String aes128SecretKey;
	private String aes256ApiKey;
	private String aes256SecretKey;
	private String expiration;
	private Boolean expired;

	private String ownerDisplayName;

	protected abstract T self();

	public T withCompanyId(String companyId) {
		setCompanyId(companyId);
		return self();
	}

	public T withName(String name) {
		setName(name);
		return self();
	}

	public T withAes128ApiKey(String aes128ApiKey) {
		setAes128ApiKey(aes128ApiKey);
		return self();
	}

	public T withAes128SecretKey(String aes128SecretKey) {
		setAes128SecretKey(aes128SecretKey);
		return self();
	}

	public T withAes256ApiKey(String aes256ApiKey) {
		setAes256ApiKey(aes256ApiKey);
		return self();
	}

	public T withAes256SecretKey(String aes256SecretKey) {
		setAes256SecretKey(aes256SecretKey);
		return self();
	}

	public T withExpiration(String expiration) {
		setExpiration(expiration);
		return self();
	}

	public T withOwnerDisplayName(String ownerDisplayName) {
		setOwnerDisplayName(ownerDisplayName);
		return self();
	}

	public T withRawApiKey(String rawApiKey) {
		setRawApiKey(rawApiKey);
		return self();
	}

	public T withRawSecretKey(String rawSecretKey) {
		setRawSecretKey(rawSecretKey);
		return self();
	}

	public T withAccessPrivilege(List<AccessPrivilegeModel> privileges) {
		setPrivileges(privileges);
		return self();
	}

	public T withIsExpired(Boolean expired) {
		setExpired(expired);
		return self();
	}

	public AccessKeyModelAbstract() {
		super(null, null);
	}

	public AccessKeyModelAbstract(AccessKey accessKey) {
		super(accessKey.getId(), accessKey.getHid());
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAes128ApiKey() {
		return aes128ApiKey;
	}

	public void setAes128ApiKey(String aes128ApiKey) {
		this.aes128ApiKey = aes128ApiKey;
	}

	public String getAes128SecretKey() {
		return aes128SecretKey;
	}

	public void setAes128SecretKey(String aes128SecretKey) {
		this.aes128SecretKey = aes128SecretKey;
	}

	public String getAes256ApiKey() {
		return aes256ApiKey;
	}

	public void setAes256ApiKey(String aes256ApiKey) {
		this.aes256ApiKey = aes256ApiKey;
	}

	public String getAes256SecretKey() {
		return aes256SecretKey;
	}

	public void setAes256SecretKey(String aes256SecretKey) {
		this.aes256SecretKey = aes256SecretKey;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public String getOwnerDisplayName() {
		return ownerDisplayName;
	}

	public void setOwnerDisplayName(String ownerDisplayName) {
		this.ownerDisplayName = ownerDisplayName;
	}

	public String getRawApiKey() {
		return rawApiKey;
	}

	public void setRawApiKey(String rawApiKey) {
		this.rawApiKey = rawApiKey;
	}

	public String getRawSecretKey() {
		return rawSecretKey;
	}

	public void setRawSecretKey(String rawSecretKey) {
		this.rawSecretKey = rawSecretKey;
	}

	public List<AccessPrivilegeModel> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<AccessPrivilegeModel> privileges) {
		this.privileges = privileges;
	}

	public Boolean isExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	public static class AccessPrivilegeModel implements Serializable {
		private static final long serialVersionUID = 165982751854552357L;

		private AccessPrivilege.AccessLevel level;
		private String pri;

		public AccessPrivilegeModel() {
		}

		public AccessPrivilegeModel(AccessPrivilege.AccessLevel level, String pri) {
			this.level = level;
			this.pri = pri;
		}

		public AccessPrivilege.AccessLevel getLevel() {
			return level;
		}

		public void setLevel(AccessPrivilege.AccessLevel level) {
			this.level = level;
		}

		public String getPri() {
			return pri;
		}

		public void setPri(String pri) {
			this.pri = pri;
		}
	}
}
