package com.arrow.kronos.web.model;

import java.util.List;

import org.springframework.data.domain.Page;

import com.arrow.kronos.web.model.AuditLogModels.AuditLogList;
import com.arrow.kronos.web.model.DeviceStateTransModels.DeviceStateTransModel;
import com.arrow.kronos.web.model.GatewayModels.GatewayModel;
import com.arrow.kronos.web.model.GlobalActionModels.GlobalActionDetailsModel;
import com.arrow.kronos.web.model.GlobalActionTypeModels.GlobalActionTypeDetailsModel;
import com.arrow.kronos.web.model.NodeModels.NodeModel;
import com.arrow.kronos.web.model.NodeTypeModels.NodeTypeModel;
import com.arrow.kronos.web.model.SearchFilterModels.AccessKeySearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.AuditLogSearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.DeviceStateTransSearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.GatewaySearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.KronosSearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.NodeSearchFilterModel;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseScheduleAssetModel;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseScheduleList;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseScheduleStatusModel;
import com.arrow.kronos.web.model.SoftwareReleaseTransModels.SoftwareReleaseTransModel;
import com.arrow.kronos.web.model.TelemetryUnitModels.TelemetryUnitModel;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;
import com.arrow.pegasus.webapi.data.CoreSearchFilterModel;
import com.arrow.pegasus.webapi.data.CoreSearchResultModel;

public class SearchResultModels {

	public static class BulkEditSearchResultModel<R extends CoreDocumentModel, F extends CoreSearchFilterModel>
	        extends CoreSearchResultModel<R, F> {
		private static final long serialVersionUID = -6553496610900003854L;

		private List<String> documentIds;

		public BulkEditSearchResultModel(Page<R> result, F filter, List<String> documentIds) {
			super(result, filter);

			this.documentIds = documentIds;
		}

		public List<String> getDocumentIds() {
			return documentIds;
		}

	}

	public static class DeviceSearchResultModel
	        extends BulkEditSearchResultModel<DeviceModels.DeviceList, SearchFilterModels.DeviceSearchFilterModel> {
		private static final long serialVersionUID = -7297814886216333441L;

		public DeviceSearchResultModel(Page<DeviceModels.DeviceList> result,
		        SearchFilterModels.DeviceSearchFilterModel filter, List<String> documentIds) {
			super(result, filter, documentIds);
		}
	}

	public static class DeviceTypeSearchResultModel
	        extends CoreSearchResultModel<DeviceTypeModels.DeviceTypeListModel, CoreSearchFilterModel> {
		private static final long serialVersionUID = 504450732331293251L;

		public DeviceTypeSearchResultModel(Page<DeviceTypeModels.DeviceTypeListModel> result,
		        CoreSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class DeviceEventSearchResultModel extends
	        CoreSearchResultModel<DeviceEventModels.DeviceEventList, SearchFilterModels.DeviceEventSearchFilterModel> {
		private static final long serialVersionUID = 604311425243597576L;

		public DeviceEventSearchResultModel(Page<DeviceEventModels.DeviceEventList> result,
		        SearchFilterModels.DeviceEventSearchFilterModel filter) {
			super(result, filter);
		}

	}

	public static class NodeTypeSearchResultModel
	        extends CoreSearchResultModel<NodeTypeModel, KronosSearchFilterModel> {
		private static final long serialVersionUID = 3662677931479115776L;

		public NodeTypeSearchResultModel(Page<NodeTypeModel> result, KronosSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class NodeSearchResultModel extends CoreSearchResultModel<NodeModel, NodeSearchFilterModel> {
		private static final long serialVersionUID = 1464729155865112906L;

		public NodeSearchResultModel(Page<NodeModel> result, NodeSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class GatewaySearchResultModel
	        extends BulkEditSearchResultModel<GatewayModel, GatewaySearchFilterModel> {
		private static final long serialVersionUID = 4534657451566165511L;

		public GatewaySearchResultModel(Page<GatewayModel> result, GatewaySearchFilterModel paging,
		        List<String> documentIds) {
			super(result, paging, documentIds);
		}
	}

	public static class AuditLogSearchResultModel
	        extends CoreSearchResultModel<AuditLogList, AuditLogSearchFilterModel> {
		private static final long serialVersionUID = 7732491986859994706L;

		public AuditLogSearchResultModel(Page<AuditLogList> result, AuditLogSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class AccessKeySearchResultModel
	        extends CoreSearchResultModel<AccessKeyModels.AccessKeyList, AccessKeySearchFilterModel> {
		private static final long serialVersionUID = 504450732331293251L;

		public AccessKeySearchResultModel(Page<AccessKeyModels.AccessKeyList> result,
		        AccessKeySearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class DeviceActionTypeSearchResultModel
	        extends CoreSearchResultModel<DeviceActionTypeModels.DeviceActionTypeDetailsModel, CoreSearchFilterModel> {
		private static final long serialVersionUID = -2428978760989225890L;

		public DeviceActionTypeSearchResultModel(Page<DeviceActionTypeModels.DeviceActionTypeDetailsModel> result,
		        CoreSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class TelemetryUnitSearchResultModel
	        extends CoreSearchResultModel<TelemetryUnitModel, KronosSearchFilterModel> {
		private static final long serialVersionUID = 3662677931479115776L;

		public TelemetryUnitSearchResultModel(Page<TelemetryUnitModel> result, KronosSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class SoftwareReleaseScheduleSearchResultModel
	        extends CoreSearchResultModel<SoftwareReleaseScheduleList, CoreSearchFilterModel> {
		private static final long serialVersionUID = -2198287630955042110L;

		public SoftwareReleaseScheduleSearchResultModel(Page<SoftwareReleaseScheduleList> result,
		        CoreSearchFilterModel paging) {
			super(result, paging);
		}
	}
	
	public static class SoftwareReleaseScheduleSearchResultStatusModel
    		extends CoreSearchResultModel<SoftwareReleaseScheduleStatusModel, CoreSearchFilterModel> {
		private static final long serialVersionUID = -2198287630955042110L;

		public SoftwareReleaseScheduleSearchResultStatusModel(Page<SoftwareReleaseScheduleStatusModel> result,
				CoreSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class SoftwareReleaseTransSearchResultModel
	        extends CoreSearchResultModel<SoftwareReleaseTransModel, CoreSearchFilterModel> {
		private static final long serialVersionUID = 2541157394846981663L;

		public SoftwareReleaseTransSearchResultModel(Page<SoftwareReleaseTransModel> result,
		        CoreSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class SoftwareReleaseScheduleAssetSearchResultModel
	        extends CoreSearchResultModel<SoftwareReleaseScheduleAssetModel, CoreSearchFilterModel> {
		private static final long serialVersionUID = -2198287630955042110L;

		public SoftwareReleaseScheduleAssetSearchResultModel(Page<SoftwareReleaseScheduleAssetModel> result,
		        CoreSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class DeviceStateTransSearchResultModel
	        extends CoreSearchResultModel<DeviceStateTransModel, DeviceStateTransSearchFilterModel> {
		private static final long serialVersionUID = 2541157394846981663L;

		public DeviceStateTransSearchResultModel(Page<DeviceStateTransModel> result,
		        DeviceStateTransSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class TestProcedureSearchResultModel
	        extends CoreSearchResultModel<TestProcedureModels.TestProcedureDetailsModel, CoreSearchFilterModel> {
		private static final long serialVersionUID = 2393354208304964538L;

		public TestProcedureSearchResultModel(Page<TestProcedureModels.TestProcedureDetailsModel> result,
		        CoreSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class TestResultSearchResultModel
	        extends CoreSearchResultModel<TestResultModels.TestResultDetailsModel, CoreSearchFilterModel> {
		private static final long serialVersionUID = 2393354208304964538L;

		public TestResultSearchResultModel(Page<TestResultModels.TestResultDetailsModel> result,
		        CoreSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class ConfigBackupSearchResultModel extends
	        CoreSearchResultModel<BackupModels.BackupModel, SearchFilterModels.ConfigBackupSearchFilterOptions> {
		private static final long serialVersionUID = 504450732331293251L;

		public ConfigBackupSearchResultModel(Page<BackupModels.BackupModel> result,
		        SearchFilterModels.ConfigBackupSearchFilterOptions paging) {
			super(result, paging);
		}
	}

	public static class MyDeviceSearchResultModel
	        extends CoreSearchResultModel<HomeModels.MyDevice, SearchFilterModels.MyDevicesSearchFilterModel> {
		private static final long serialVersionUID = -7297814886216333441L;

		public MyDeviceSearchResultModel(Page<HomeModels.MyDevice> result,
		        SearchFilterModels.MyDevicesSearchFilterModel filter) {
			super(result, filter);
		}
	}

	public static class MyDeviceEventSearchResultModel
	        extends CoreSearchResultModel<HomeModels.MyDeviceEvent, SearchFilterModels.MyDeviceEventSearchFilterModel> {
		private static final long serialVersionUID = -7297814886216333441L;

		public MyDeviceEventSearchResultModel(Page<HomeModels.MyDeviceEvent> result,
		        SearchFilterModels.MyDeviceEventSearchFilterModel filter) {
			super(result, filter);
		}
	}

	public static class MyGatewaySearchResultModel
	        extends CoreSearchResultModel<HomeModels.MyGateway, SearchFilterModels.MyGatewaySearchFilterModel> {
		private static final long serialVersionUID = -7297814886216333441L;

		public MyGatewaySearchResultModel(Page<HomeModels.MyGateway> result,
		        SearchFilterModels.MyGatewaySearchFilterModel filter) {
			super(result, filter);
		}
	}

	public static class RTUSearchResultModel
	        extends CoreSearchResultModel<RTUFirmwareModels.RTURequestFirmwareModel, CoreSearchFilterModel> {
		private static final long serialVersionUID = 2393354208304964538L;

		public RTUSearchResultModel(Page<RTUFirmwareModels.RTURequestFirmwareModel> result,
				CoreSearchFilterModel paging) {
			super(result, paging);
		}
	}

	public static class GlobalActionTypeSearchResultModel
	        extends CoreSearchResultModel<GlobalActionTypeDetailsModel, CoreSearchFilterModel> {
		private static final long serialVersionUID = 8016045639992279098L;

		public GlobalActionTypeSearchResultModel(Page<GlobalActionTypeDetailsModel> result,
		        CoreSearchFilterModel filter) {
			super(result, filter);
		}
	}

	public static class GlobalActionSearchResultModel
	        extends CoreSearchResultModel<GlobalActionDetailsModel, CoreSearchFilterModel> {
		private static final long serialVersionUID = -320157082316230616L;

		public GlobalActionSearchResultModel(Page<GlobalActionDetailsModel> result, CoreSearchFilterModel filter) {
			super(result, filter);
		}
	}

}