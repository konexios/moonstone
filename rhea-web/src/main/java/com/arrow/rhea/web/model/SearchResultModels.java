package com.arrow.rhea.web.model;

import org.springframework.data.domain.Page;

import com.arrow.pegasus.webapi.data.CoreSearchResultModel;
import com.arrow.rhea.web.model.DeviceManufacturerModels.DeviceManufacturerModel;
import com.arrow.rhea.web.model.DeviceProductModels.DeviceProductModel;
import com.arrow.rhea.web.model.DeviceTypeModels.DeviceTypeModel;
import com.arrow.rhea.web.model.RTURequestModels.RTURequestModel;
import com.arrow.rhea.web.model.SearchFilterModels.DeviceManufacturerSearchFilterModel;
import com.arrow.rhea.web.model.SearchFilterModels.DeviceProductSearchFilterModel;
import com.arrow.rhea.web.model.SearchFilterModels.DeviceTypeSearchFilterModel;
import com.arrow.rhea.web.model.SearchFilterModels.RTURequestSearchFilterModel;
import com.arrow.rhea.web.model.SearchFilterModels.SoftwareProductSearchFilterModel;
import com.arrow.rhea.web.model.SearchFilterModels.SoftwareReleaseSearchFilterModel;
import com.arrow.rhea.web.model.SearchFilterModels.SoftwareVendorSearchFilterModel;
import com.arrow.rhea.web.model.SoftwareProductModels.SoftwareProductModel;
import com.arrow.rhea.web.model.SoftwareReleaseModels.SoftwareReleaseModel;
import com.arrow.rhea.web.model.SoftwareVendorModels.SoftwareVendorModel;

public class SearchResultModels {

	public static class DeviceManufacturerSearchResultModel
	        extends CoreSearchResultModel<DeviceManufacturerModel, DeviceManufacturerSearchFilterModel> {
		private static final long serialVersionUID = -410712028935481477L;

		public DeviceManufacturerSearchResultModel(Page<DeviceManufacturerModel> result,
		        DeviceManufacturerSearchFilterModel filter) {
			super(result, filter);
		}
	}

	public static class DeviceProductSearchResultModel
	        extends CoreSearchResultModel<DeviceProductModel, DeviceProductSearchFilterModel> {
		private static final long serialVersionUID = 380607545659252437L;

		public DeviceProductSearchResultModel(Page<DeviceProductModel> result, DeviceProductSearchFilterModel filter) {
			super(result, filter);
		}
	}

	public static class DeviceTypeSearchResultModel
	        extends CoreSearchResultModel<DeviceTypeModel, DeviceTypeSearchFilterModel> {
		private static final long serialVersionUID = -6184096555748713416L;

		public DeviceTypeSearchResultModel(Page<DeviceTypeModel> result, DeviceTypeSearchFilterModel filter) {
			super(result, filter);
		}
	}

	public static class SoftwareVendorSearchResultModel
	        extends CoreSearchResultModel<SoftwareVendorModel, SoftwareVendorSearchFilterModel> {
		private static final long serialVersionUID = -5249851406234503101L;

		public SoftwareVendorSearchResultModel(Page<SoftwareVendorModel> result,
		        SoftwareVendorSearchFilterModel filter) {
			super(result, filter);
		}
	}

	public static class SoftwareProductSearchResultModel
	        extends CoreSearchResultModel<SoftwareProductModel, SoftwareProductSearchFilterModel> {
		private static final long serialVersionUID = 7812590427765137718L;

		public SoftwareProductSearchResultModel(Page<SoftwareProductModel> result,
		        SoftwareProductSearchFilterModel filter) {
			super(result, filter);
		}
	}

	public static class SoftwareReleaseSearchResultModel
	        extends CoreSearchResultModel<SoftwareReleaseModel, SoftwareReleaseSearchFilterModel> {
		private static final long serialVersionUID = -5580254547465781648L;

		public SoftwareReleaseSearchResultModel(Page<SoftwareReleaseModel> result,
		        SoftwareReleaseSearchFilterModel filter) {
			super(result, filter);
		}
	}
	
	public static class RTURequestSearchResultModel
			extends CoreSearchResultModel<RTURequestModel, RTURequestSearchFilterModel> {
		private static final long serialVersionUID = -5580254547465781648L;

		public RTURequestSearchResultModel(Page<RTURequestModel> result,
				RTURequestSearchFilterModel filter) {
			super(result, filter);
		}
	}
}