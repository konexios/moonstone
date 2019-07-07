/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acs.client.model;

import java.io.Serializable;

public class VersionModel implements Serializable {
	private static final long serialVersionUID = -5828507529810423672L;

	public static VersionModel UNDEFINED = new VersionModel().withName("UNDEFINED").withMajor(0).withMinor(0)
			.withBuild(0);

	private String name;
	private String description;
	private Integer major;
	private Integer minor;
	private Integer build;
	private Integer compatibleMajor;
	private Integer compatibleMinor;
	private String vendor;

	// build info
	private String builtBy;
	private String builtJdk;
	private String builtDate;
	private String gitBranch;
	private String gitLastCommit;

	public VersionModel withName(String name) {
		setName(name);
		return this;
	}

	public VersionModel withDescription(String description) {
		setDescription(description);
		return this;
	}

	public VersionModel withGitBranch(String gitBranch) {
		setGitBranch(gitBranch);
		return this;
	}

	public VersionModel withGitLastCommit(String gitLastCommit) {
		setGitLastCommit(gitLastCommit);
		return this;
	}

	public VersionModel withVendor(String vendor) {
		setVendor(vendor);
		return this;
	}

	public VersionModel withBuiltBy(String builtBy) {
		setBuiltBy(builtBy);
		return this;
	}

	public VersionModel withBuiltJdk(String buildJdk) {
		setBuiltJdk(buildJdk);
		return this;
	}

	public VersionModel withBuiltDate(String builtDate) {
		setBuiltDate(builtDate);
		return this;
	}

	public VersionModel withMajor(Integer major) {
		setMajor(major);
		return this;
	}

	public VersionModel withMinor(Integer minor) {
		setMinor(minor);
		return this;
	}

	public VersionModel withBuild(Integer build) {
		setBuild(build);
		return this;
	}

	public VersionModel withCompatibleMajor(Integer compatibleMajor) {
		setCompatibleMajor(compatibleMajor);
		return this;
	}

	public VersionModel withCompatibleMinor(Integer compatibleMinor) {
		setCompatibleMinor(compatibleMinor);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMajor() {
		return major;
	}

	public void setMajor(Integer major) {
		this.major = major;
	}

	public Integer getMinor() {
		return minor;
	}

	public void setMinor(Integer minor) {
		this.minor = minor;
	}

	public Integer getBuild() {
		return build;
	}

	public void setBuild(Integer build) {
		this.build = build;
	}

	public Integer getCompatibleMajor() {
		return compatibleMajor;
	}

	public void setCompatibleMajor(Integer compatibleMajor) {
		this.compatibleMajor = compatibleMajor;
	}

	public Integer getCompatibleMinor() {
		return compatibleMinor;
	}

	public void setCompatibleMinor(Integer compatibleMinor) {
		this.compatibleMinor = compatibleMinor;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getBuiltBy() {
		return builtBy;
	}

	public void setBuiltBy(String builtBy) {
		this.builtBy = builtBy;
	}

	public String getBuiltJdk() {
		return builtJdk;
	}

	public void setBuiltJdk(String builtJdk) {
		this.builtJdk = builtJdk;
	}

	public String getBuiltDate() {
		return builtDate;
	}

	public void setBuiltDate(String builtDate) {
		this.builtDate = builtDate;
	}

	public String getGitBranch() {
		return gitBranch;
	}

	public void setGitBranch(String gitBranch) {
		this.gitBranch = gitBranch;
	}

	public String getGitLastCommit() {
		return gitLastCommit;
	}

	public void setGitLastCommit(String gitLastCommit) {
		this.gitLastCommit = gitLastCommit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((build == null) ? 0 : build.hashCode());
		result = prime * result + ((builtBy == null) ? 0 : builtBy.hashCode());
		result = prime * result + ((builtDate == null) ? 0 : builtDate.hashCode());
		result = prime * result + ((builtJdk == null) ? 0 : builtJdk.hashCode());
		result = prime * result + ((compatibleMajor == null) ? 0 : compatibleMajor.hashCode());
		result = prime * result + ((compatibleMinor == null) ? 0 : compatibleMinor.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((gitBranch == null) ? 0 : gitBranch.hashCode());
		result = prime * result + ((gitLastCommit == null) ? 0 : gitLastCommit.hashCode());
		result = prime * result + ((major == null) ? 0 : major.hashCode());
		result = prime * result + ((minor == null) ? 0 : minor.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((vendor == null) ? 0 : vendor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersionModel other = (VersionModel) obj;
		if (build == null) {
			if (other.build != null)
				return false;
		} else if (!build.equals(other.build))
			return false;
		if (builtBy == null) {
			if (other.builtBy != null)
				return false;
		} else if (!builtBy.equals(other.builtBy))
			return false;
		if (builtDate == null) {
			if (other.builtDate != null)
				return false;
		} else if (!builtDate.equals(other.builtDate))
			return false;
		if (builtJdk == null) {
			if (other.builtJdk != null)
				return false;
		} else if (!builtJdk.equals(other.builtJdk))
			return false;
		if (compatibleMajor == null) {
			if (other.compatibleMajor != null)
				return false;
		} else if (!compatibleMajor.equals(other.compatibleMajor))
			return false;
		if (compatibleMinor == null) {
			if (other.compatibleMinor != null)
				return false;
		} else if (!compatibleMinor.equals(other.compatibleMinor))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (gitBranch == null) {
			if (other.gitBranch != null)
				return false;
		} else if (!gitBranch.equals(other.gitBranch))
			return false;
		if (gitLastCommit == null) {
			if (other.gitLastCommit != null)
				return false;
		} else if (!gitLastCommit.equals(other.gitLastCommit))
			return false;
		if (major == null) {
			if (other.major != null)
				return false;
		} else if (!major.equals(other.major))
			return false;
		if (minor == null) {
			if (other.minor != null)
				return false;
		} else if (!minor.equals(other.minor))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (vendor == null) {
			if (other.vendor != null)
				return false;
		} else if (!vendor.equals(other.vendor))
			return false;
		return true;
	}
}
