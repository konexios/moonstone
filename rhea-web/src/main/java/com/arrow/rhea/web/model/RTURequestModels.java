package com.arrow.rhea.web.model;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;

import com.arrow.acn.client.model.RightToUseChangeStatus;
import com.arrow.acn.client.model.RightToUseStatus;
import com.arrow.pegasus.webapi.data.CoreApplicationModels.ApplicationOption;
import com.arrow.pegasus.webapi.data.CoreCompanyModels.CompanyOption;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.rhea.data.RTURequest;

public class RTURequestModels {
	
	public static class RTURequestOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 4566301714298832803L;

		public RTURequestOption() {
			super(null, null, null);
		}

		public RTURequestOption(RTURequest rtuRequest) {
			super(rtuRequest.getId(), rtuRequest.getHid(), null);
		}
	}

	public static class RTURequestModel extends  RTURequestOption{
		private static final long serialVersionUID = 3636257632472073165L;

		private CompanyOption company;
		private String firmwareVersion;
		private RightToUseStatus status;
		private EnumSet<RightToUseChangeStatus> changeStatuses;

		public RTURequestModel() {
			super();
		}

		public RTURequestModel(RTURequest rtuRequest) {
			super(rtuRequest);
			this.setCompany(new CompanyOption(rtuRequest.getRefCompany()));
			this.setFirmwareVersion(rtuRequest.getFirmwareVersion());
			this.setStatus(rtuRequest.getStatus());
		}

		public CompanyOption getCompany() {
			return company;
		}

		public void setCompany(CompanyOption company) {
			this.company = company;
		}

		public String getFirmwareVersion() {
			return firmwareVersion;
		}

		public void setFirmwareVersion(String firmwareVersion) {
			this.firmwareVersion = firmwareVersion;
		}

		public RightToUseStatus getStatus() {
			return status;
		}

		public void setStatus(RightToUseStatus status) {
			this.status = status;
		}

		public RTURequestModel withChangeStatuses(EnumSet<RightToUseChangeStatus> changeStatuses) {
			this.setChangeStatuses(changeStatuses);
			return this;
		}

		public EnumSet<RightToUseChangeStatus> getChangeStatuses() {
			return changeStatuses;
		}

		public void setChangeStatuses(EnumSet<RightToUseChangeStatus> changeStatuses) {
			this.changeStatuses = changeStatuses;
		}
	}

	public static class RTURequestFilterOptionsModel implements Serializable {
		private static final long serialVersionUID = 7006810969191069867L;

		private EnumSet<RightToUseStatus> statuses;
		private List<CompanyOption> companies;

		public RTURequestFilterOptionsModel( List<CompanyOption> companyOptions, 
				EnumSet<RightToUseStatus> statuses) {
			this.companies = companyOptions;
			this.statuses = statuses;
		}

		public List<CompanyOption> getCompanies() {
			return companies;
		}

		public EnumSet<RightToUseStatus> getStatuses() {
			return statuses;
		}


	}
}
