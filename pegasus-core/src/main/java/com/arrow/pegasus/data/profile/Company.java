package com.arrow.pegasus.data.profile;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "company")
public class Company extends AuditableDocumentAbstract {
	private static final long serialVersionUID = -2053964512032066986L;

	@NotBlank
	private String name;
	@NotBlank
	@Indexed(unique = true)
	private String abbrName;
	private CompanyStatus status;
	private Address address;
	private Address billingAddress;
	private Contact contact;
	private Contact billingContact;
	@NotNull
	private PasswordPolicy passwordPolicy = new PasswordPolicy();
	private LoginPolicy loginPolicy = new LoginPolicy();
	private String parentCompanyId;
	private Set<String> impersonateUsers = new HashSet<>();

	@Transient
	@JsonIgnore
	private Company refParentCompany;

	public LoginPolicy getLoginPolicy() {
		return loginPolicy;
	}

	public void setLoginPolicy(LoginPolicy loginPolicy) {
		this.loginPolicy = loginPolicy;
	}

	public PasswordPolicy getPasswordPolicy() {
		return passwordPolicy;
	}

	public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}

	public Contact getBillingContact() {
		return billingContact;
	}

	public void setBillingContact(Contact billingContact) {
		this.billingContact = billingContact;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbrName() {
		return abbrName;
	}

	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}

	public CompanyStatus getStatus() {
		return status;
	}

	public void setStatus(CompanyStatus status) {
		this.status = status;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getParentCompanyId() {
		return parentCompanyId;
	}

	public void setParentCompanyId(String parentCompanyId) {
		this.parentCompanyId = parentCompanyId;
	}

	public Company getRefParentCompany() {
		return refParentCompany;
	}

	public void setRefParentCompany(Company refParentCompany) {
		this.refParentCompany = refParentCompany;
	}

	public Set<String> getImpersonateUsers() {
		return impersonateUsers;
	}

	public void setImpersonateUsers(Set<String> impersonateUsers) {
		this.impersonateUsers = impersonateUsers;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.COMPANY;
	}
}
