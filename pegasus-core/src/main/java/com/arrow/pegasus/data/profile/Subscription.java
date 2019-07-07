package com.arrow.pegasus.data.profile;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "subscription")
public class Subscription extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = -779791492205514036L;

	@NotBlank
	private String companyId;
	@NotNull
	private Instant startDate;
	@NotNull
	private Instant endDate;
	private Contact contact;
	private Contact billingContact;

	@Transient
	@JsonIgnore
	private Company refCompany;

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Contact getBillingContact() {
		return billingContact;
	}

	public void setBillingContact(Contact billingContact) {
		this.billingContact = billingContact;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}

	public Instant getStartDate() {
		return startDate;
	}

	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
	}

	public Instant getEndDate() {
		return endDate;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.SUBSCRIPTION;
	}

	public Company getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(Company refCompany) {
		this.refCompany = refCompany;
	}
}
