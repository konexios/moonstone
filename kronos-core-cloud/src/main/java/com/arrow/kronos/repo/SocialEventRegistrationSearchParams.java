package com.arrow.kronos.repo;

import java.io.Serializable;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.kronos.data.SocialEventRegistrationStatuses;
import com.arrow.pegasus.repo.params.AuditableDocumentSearchParams;

public class SocialEventRegistrationSearchParams extends AuditableDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = -2563939772039111582L;
	
	private Set<String> socialEventIds;
	private Set<String> names;
	private Set<String> emails;
	private EnumSet<SocialEventRegistrationStatuses> statuses;
	private Set<String> applicationIds;
	private Instant createdBefore;
	private Instant createdAfter;
	private Instant updatedBefore;
	private Instant updatedAfter;
	private Set<String> origEmails;

	public EnumSet<SocialEventRegistrationStatuses> getStatuses() {
		return statuses;
	}

	public void setStatuses(EnumSet<SocialEventRegistrationStatuses> statuses) {
		this.statuses = statuses;
	}

	public Set<String> getNames() {
		return names;
	}

	public void setNames(Set<String> names) {
		this.names = names;
	}
	
    public SocialEventRegistrationSearchParams addNames(String... names) {
        this.names = super.addValues(this.names, names);

        return this;
    }

	public Set<String> getEmails() {
		return emails;
	}

	public void setEmails(Set<String> emails) {
		this.emails = emails;
	}
	
    public SocialEventRegistrationSearchParams addEmails(String... emails) {
        this.emails = super.addValues(this.emails, emails);

        return this;
    }

	public Set<String> getApplicationIds() {
		return applicationIds;
	}

	public void setApplicationIds(Set<String> applicationIds) {
		this.applicationIds = applicationIds;
	}
	
    public SocialEventRegistrationSearchParams addApplicationIds(String... applicationIds) {
        this.applicationIds = super.addValues(this.applicationIds, applicationIds);

        return this;
    }

	public Set<String> getSocialEventIds() {
		return socialEventIds;
	}

	public void setSocialEventIds(Set<String> socialEventIds) {
		this.socialEventIds = socialEventIds;
	}
	
    public SocialEventRegistrationSearchParams addSocialEventIds(String... socialEventIds) {
        this.socialEventIds = super.addValues(this.socialEventIds, socialEventIds);

        return this;
    }
	
	public Instant getCreatedBefore() {
		return createdBefore;
	}

	public void setCreatedBefore(Instant createdBefore) {
		this.createdBefore = createdBefore;
	}

	public SocialEventRegistrationSearchParams addCreatedBefore(Instant createdBefore) {
		setCreatedBefore(createdBefore);
		return this;
	}

	public Instant getCreatedAfter() {
		return createdAfter;
	}

	public void setCreatedAfter(Instant createdAfter) {
		this.createdAfter = createdAfter;
	}

	public Instant getUpdatedBefore() {
		return updatedBefore;
	}

	public void setUpdatedBefore(Instant updatedBefore) {
		this.updatedBefore = updatedBefore;
	}

	public Instant getUpdatedAfter() {
		return updatedAfter;
	}

	public void setUpdatedAfter(Instant updatedAfter) {
		this.updatedAfter = updatedAfter;
	}

	public Set<String> getOrigEmails() {
		return origEmails;
	}

	public void setOrigEmails(Set<String> origEmails) {
		this.origEmails = origEmails;
	}
	
    public SocialEventRegistrationSearchParams addOrigEmails(String... origEmails) {
        this.origEmails = super.addValues(this.origEmails, origEmails);

        return this;
    }
}
