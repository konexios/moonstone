package com.arrow.pegasus.repo.params;

import java.util.Set;

public class DocumentSearchParams extends SearchParamsAbstract {
	private static final long serialVersionUID = 3399384446330964602L;

	private Set<String> ids;
	private Set<String> hids;

	public Set<String> getIds() {
		return super.getValues(ids);
	}

	public DocumentSearchParams addIds(String... ids) {
		this.ids = super.addValues(this.ids, ids);

		return this;
	}

	public Set<String> getHids() {
		return super.getValues(hids);
	}

	public DocumentSearchParams addHids(String... hids) {
		this.hids = super.addValues(this.hids, hids);

		return this;
	}
}
