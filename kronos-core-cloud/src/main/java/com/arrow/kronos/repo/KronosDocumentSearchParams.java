package com.arrow.kronos.repo;

import java.io.Serializable;
import java.util.Set;

import com.arrow.pegasus.repo.params.AuditableDocumentSearchParams;

public class KronosDocumentSearchParams extends AuditableDocumentSearchParams implements Serializable {

    private static final long serialVersionUID = 3569796315562341025L;

    private Set<String> applicationIds;
    private Boolean enabled;

    public Set<String> getApplicationIds() {
        return super.getValues(applicationIds);
    }

    public KronosDocumentSearchParams addApplicationIds(String... applicationIds) {
        this.applicationIds = super.addValues(this.applicationIds, applicationIds);

        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public KronosDocumentSearchParams setEnabled(Boolean enabled) {
        this.enabled = enabled;

        return this;
    }

}
