package com.arrow.pegasus.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AccessPrivilege.AccessLevel;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = AccessKey.COLLECTION_NAME)
@CompoundIndexes({
		@CompoundIndex(name = "privileges__pri", background = true, sparse = true, def = "{'privileges.pri' : 1}") })
public class AccessKey extends DocumentAbstract {
	private static final long serialVersionUID = 445011553342626937L;
	private final static EnumSet<AccessLevel> CAN_WRITE_SET = EnumSet.of(AccessLevel.OWNER, AccessLevel.WRITE);
	public static final String COLLECTION_NAME = "access_key";

	// TODO @NotBlank
	private String name;
	@NotBlank
	private String companyId;
	@Indexed(sparse = true)
	private String subscriptionId;
	@Indexed(sparse = true)
	private String applicationId;
	@NotBlank
	@Indexed(unique = true)
	private String hashedApiKey;
	@NotBlank
	private String encryptedApiKey;
	@NotBlank
	private String encryptedSecretKey;
	private Instant expiration;
	private List<AccessPrivilege> privileges = new ArrayList<>();

	@Transient
	@JsonIgnore
	private Company refCompany;
	@Transient
	@JsonIgnore
	private Application refApplication;

	@Transient
	@JsonIgnore
	private String apiKey;
	@Transient
	@JsonIgnore
	private String secretKey;

	public boolean canRead(DocumentAbstract document) {
		return document == null ? false : findByPri(document.getPri()).size() > 0;
	}

	public boolean canWrite(DocumentAbstract document) {
		if (document != null) {
			for (AccessPrivilege priv : findByPri(document.getPri())) {
				if (CAN_WRITE_SET.contains(priv.getLevel())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isOwner(DocumentAbstract document) {
		if (document != null) {
			for (AccessPrivilege priv : findByPri(document.getPri())) {
				if (priv.getLevel() == AccessLevel.OWNER) {
					return true;
				}
			}
		}
		return false;
	}

	public List<AccessPrivilege> findByPri(String pri) {
		List<AccessPrivilege> result = new ArrayList<>();
		privileges.forEach(priv -> {
			if (priv.getPri().equals(pri)) {
				result.add(priv);
			}
		});
		return result;
	}

	public Company getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(Company refCompany) {
		this.refCompany = refCompany;
	}

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getHashedApiKey() {
		return hashedApiKey;
	}

	public void setHashedApiKey(String hashedApiKey) {
		this.hashedApiKey = hashedApiKey;
	}

	public String getEncryptedApiKey() {
		return encryptedApiKey;
	}

	public void setEncryptedApiKey(String encryptedApiKey) {
		this.encryptedApiKey = encryptedApiKey;
	}

	public String getEncryptedSecretKey() {
		return encryptedSecretKey;
	}

	public void setEncryptedSecretKey(String encryptedSecretKey) {
		this.encryptedSecretKey = encryptedSecretKey;
	}

	public Instant getExpiration() {
		return expiration;
	}

	public void setExpiration(Instant expiration) {
		this.expiration = expiration;
	}

	public List<AccessPrivilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<AccessPrivilege> privileges) {
		this.privileges = privileges;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.ACCESS_KEY;
	}
}
