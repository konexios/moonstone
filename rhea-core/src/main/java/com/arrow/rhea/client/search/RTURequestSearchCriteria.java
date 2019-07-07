package com.arrow.rhea.client.search;

import com.arrow.acs.client.search.SearchCriteria;

public class RTURequestSearchCriteria extends SearchCriteria {

	private static final String COMPANY_ID = "companyIds";
	private static final String STATUSES = "statuses";
	private static final String SOFTWARERELEASE_ID = "softwareReleaseIds";
	private static final String PAGE = "page";
	private static final String SIZE = "size";
	private static final String SORT_DIRECTION = "sortDirection";
	private static final String SORT_PROPERTY = "sortProperty";

	public RTURequestSearchCriteria withCompanyIds(String... companyIds) {
		if (companyIds != null) {
			arrayCriteria.put(COMPANY_ID, companyIds);
		}
		return this;
	}

	public RTURequestSearchCriteria withStatuses(String... statuses) {
		if (statuses != null) {
			arrayCriteria.put(STATUSES, statuses);
		}
		return this;
	}

	public RTURequestSearchCriteria withSoftwareReleaseIds(String... softwareReleaseIds) {
		if (softwareReleaseIds != null) {
			arrayCriteria.put(SOFTWARERELEASE_ID, softwareReleaseIds);
		}
		return this;
	}
	
	public RTURequestSearchCriteria withSortDirection(String sortDirection) {
		if (sortDirection != null) {
			simpleCriteria.put(SORT_DIRECTION, sortDirection);
		}
		return this;
	}

	public RTURequestSearchCriteria withSortProperty(String sortProperty) {
		if (sortProperty != null) {
			simpleCriteria.put(SORT_PROPERTY, sortProperty);
		}
		return this;
	}

	public RTURequestSearchCriteria withPage(long page) {
		simpleCriteria.put(PAGE, Long.toString(page));
		return this;
	}

	public RTURequestSearchCriteria withSize(long size) {
		simpleCriteria.put(SIZE, Long.toString(size));
		return this;
	}
}
