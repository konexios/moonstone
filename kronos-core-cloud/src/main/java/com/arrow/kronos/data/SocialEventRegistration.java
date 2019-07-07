package com.arrow.kronos.data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.SocialEvent;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "social_event_registration")
public class SocialEventRegistration extends AuditableDocumentAbstract {
	private static final long serialVersionUID = -714969119078962802L;

	@NotBlank
	private String socialEventId;
	private String name;
	private String email;
	private String hashedPassword;
	private String salt;
	@NotBlank
	private String verificationCode;
	@NotNull
	private SocialEventRegistrationStatuses status = SocialEventRegistrationStatuses.PENDING;
	@NotBlank
	private String companyId;
	@NotBlank
	private String subscriptionId;
	@NotBlank
	private String applicationId;
	@NotBlank
	private String origEmail;
	@NotBlank
	private String origEncryptedPassword;
	@NotBlank
	private String userId;
	@Valid
	private AzureIntegrationProperties azureIntegration;

	@Transient
	@JsonIgnore
	private Company refCompany;
	@Transient
	@JsonIgnore
	private Subscription refSubscription;
	@Transient
	@JsonIgnore
	private Application refApplication;
	@Transient
	@JsonIgnore
	private SocialEvent refSocialEvent;
	@Transient
	@JsonIgnore
	private User refUser;

	public Company getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(Company refCompany) {
		this.refCompany = refCompany;
	}

	public Subscription getRefSubscription() {
		return refSubscription;
	}

	public void setRefSubscription(Subscription refSubscription) {
		this.refSubscription = refSubscription;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.SOCIAL_EVENT_REGISTRATION;
	}

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public SocialEvent getRefSocialEvent() {
		return refSocialEvent;
	}

	public void setRefSocialEvent(SocialEvent socialEvent) {
		this.refSocialEvent = socialEvent;
	}

	public SocialEventRegistrationStatuses getStatus() {
		return status;
	}

	public void setStatus(SocialEventRegistrationStatuses status) {
		this.status = status;
	}

	public String getSocialEventId() {
		return socialEventId;
	}

	public void setSocialEventId(String socialEventId) {
		this.socialEventId = socialEventId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getOrigEmail() {
		return origEmail;
	}

	public void setOrigEmail(String origEmail) {
		this.origEmail = origEmail;
	}

	public String getOrigEncryptedPassword() {
		return origEncryptedPassword;
	}

	public void setOrigEncryptedPassword(String origEncryptedPassword) {
		this.origEncryptedPassword = origEncryptedPassword;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public AzureIntegrationProperties getAzureIntegration() {
		return azureIntegration;
	}

	public void setAzureIntegration(AzureIntegrationProperties azureIntegration) {
		this.azureIntegration = azureIntegration;
	}

	public User getRefUser() {
		return refUser;
	}

	public void setRefUser(User refUser) {
		this.refUser = refUser;
	}
}
