package com.arrow.pegasus.webapi.data;

import java.io.Serializable;

import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;

public class CoreCompanyModels {

	public static class CompanyStatusOption implements Serializable {
		private static final long serialVersionUID = -2698198938901692259L;

		private String id;
		private String name;

		public CompanyStatusOption(CompanyStatus companyStatus) {
			this.id = companyStatus.name();
			this.name = companyStatus.name();
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	public static class CompanyOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 5261989128897508364L;
		
		public CompanyOption() {
			super(null, null, null);
		}

		public CompanyOption(Company company) {
			super(company.getId(), company.getHid(), company.getName());
		}
	}
}
