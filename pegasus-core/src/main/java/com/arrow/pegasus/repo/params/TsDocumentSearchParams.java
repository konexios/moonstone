package com.arrow.pegasus.repo.params;

import java.util.Set;

public class TsDocumentSearchParams extends DocumentSearchParams {
	private static final long serialVersionUID = 6625599165309833230L;

	// TODO createdDate
	private Set<String> createdBys;

	public Set<String> getCreatedBys() {
		return super.getValues(createdBys);
	}

	public DocumentSearchParams addCreatedBys(String... createdBys) {
		this.createdBys = super.addValues(this.createdBys, createdBys);

		return this;
	}
}
