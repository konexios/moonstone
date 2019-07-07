package com.arrow.pegasus.repo.params;

import java.util.Set;

public class AuditableDocumentSearchParams extends TsDocumentSearchParams {
	private static final long serialVersionUID = -5246180624711571979L;

	// TODO lastModifiedDate
	private Set<String> lastModifiedBys;

	public Set<String> getLastModifiedBys() {
		return super.getValues(lastModifiedBys);
	}

	public DocumentSearchParams addLastModifiedBys(String... lastModifiedBys) {
		this.lastModifiedBys = super.addValues(this.lastModifiedBys, lastModifiedBys);

		return this;
	}
}
