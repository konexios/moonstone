package com.arrow.pegasus.repo.params;

import java.io.Serializable;
import java.util.Set;

import com.arrow.pegasus.repo.params.AuditableDocumentSearchParams;

public class PegasusDocumentSearchParams extends AuditableDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = 6535367132022232796L;

	private Set<String> applicationIds;
    private Boolean enabled;

    public Set<String> getApplicationIds() {
        return super.getValues(applicationIds);
    }

    public PegasusDocumentSearchParams addApplicationIds(String... applicationIds) {
        this.applicationIds = super.addValues(this.applicationIds, applicationIds);

        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public PegasusDocumentSearchParams setEnabled(Boolean enabled) {
        this.enabled = enabled;

        return this;
    }

}
