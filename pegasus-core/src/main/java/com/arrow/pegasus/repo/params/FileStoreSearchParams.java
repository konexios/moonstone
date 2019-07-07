package com.arrow.pegasus.repo.params;

import java.util.Set;

public class FileStoreSearchParams extends DocumentSearchParams {
	private static final long serialVersionUID = -6849561158332702772L;

	private Set<String> companyIds;
	private Set<String> applicationIds;
	private Set<String> names;
	private Set<String> categories;
	private Set<String> relatedIds;
	private Set<String> contentTypes;

	public Set<String> getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(Set<String> companyIds) {
		this.companyIds = companyIds;
	}

	public FileStoreSearchParams addCompanyIds(String... companyIds) {
		this.companyIds = addValues(this.companyIds, companyIds);
		return this;
	}

	public Set<String> getApplicationIds() {
		return applicationIds;
	}

	public void setApplicationIds(Set<String> applicationIds) {
		this.applicationIds = applicationIds;
	}

	public FileStoreSearchParams addApplicationIds(String... applicationIds) {
		this.applicationIds = addValues(this.applicationIds, applicationIds);
		return this;
	}

	public Set<String> getNames() {
		return names;
	}

	public void setNames(Set<String> names) {
		this.names = names;
	}

	public FileStoreSearchParams addNames(String... names) {
		this.names = addValues(this.names, names);
		return this;
	}

	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	public FileStoreSearchParams addCategories(String... categories) {
		this.categories = addValues(this.categories, categories);
		return this;
	}

	public Set<String> getRelatedIds() {
		return relatedIds;
	}

	public void setRelatedIds(Set<String> relatedIds) {
		this.relatedIds = relatedIds;
	}

	public FileStoreSearchParams addRelatedIds(String... relatedIds) {
		this.relatedIds = addValues(this.relatedIds, relatedIds);
		return this;
	}

	public Set<String> getContentTypes() {
		return contentTypes;
	}

	public void setContentTypes(Set<String> contentTypes) {
		this.contentTypes = contentTypes;
	}

	public FileStoreSearchParams addContentTypes(String... contentTypes) {
		this.contentTypes = addValues(this.contentTypes, contentTypes);
		return this;
	}
}
