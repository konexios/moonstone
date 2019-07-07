package com.arrow.kronos.data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.service.UserRegistrationStatus;
import com.arrow.pegasus.data.AuditableDocumentAbstract;

@Document(collection = "user_registration")
public class UserRegistration extends AuditableDocumentAbstract {
	private static final long serialVersionUID = 3522289553033452593L;

	// @Indexed(unique = true)
	@NotBlank
	private String email;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NotBlank
	private String title;
	@NotBlank
	private String companyName;
	@NotBlank
	private String companyWebSite;
	@NotBlank
	private String projectDescription;
	private String referralCode;
	private String eventCode;
	@NotNull
	private UserRegistrationStatus status = UserRegistrationStatus.Pending;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyWebSite() {
		return companyWebSite;
	}

	public void setCompanyWebSite(String companyWebSite) {
		this.companyWebSite = companyWebSite;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public UserRegistrationStatus getStatus() {
		return status;
	}

	public void setStatus(UserRegistrationStatus status) {
		this.status = status;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.USER_REGISTRATION;
	}
}
