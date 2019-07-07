package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acn.client.model.DeviceStateValueType;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.kronos.DeviceActionTypeConstants;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceStateValueMetadata;
import com.arrow.kronos.data.DeviceTelemetry;
import com.arrow.kronos.data.DeviceType;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class DeviceTypeModels {

	public static class DeviceTypeModel extends CoreDocumentModel {

		private String name;
		private String description;
		private String lastModifiedBy;
		private Instant lastModifiedDate;

		// TODO telemetries

		public DeviceTypeModel() {
			super(null, null);
			this.name = "";
			this.description = "";
		}

		public DeviceTypeModel(DeviceType deviceType) {
			super(deviceType.getId(), deviceType.getHid());
			this.name = deviceType.getName();
			this.description = deviceType.getDescription();
			this.lastModifiedBy = deviceType.getLastModifiedBy();
			this.lastModifiedDate = deviceType.getLastModifiedDate();
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getLastModifiedBy() {
		    return lastModifiedBy;
		}

		public Instant getLastModifiedDate(){
		    return lastModifiedDate;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
		}
	}

	public static class DeviceTypeListModel extends DeviceTypeModel {

		private AcnDeviceCategory deviceCategory;
		private boolean enabled;
		private boolean editable;
		private long numDevices = 0L;

		public DeviceTypeListModel() {
			super();
			this.enabled = true;
		}

		public DeviceTypeListModel(DeviceType deviceType) {
			super(deviceType);
			this.deviceCategory = deviceType.getDeviceCategory();
			this.enabled = deviceType.isEnabled();
			this.editable = deviceType.isEditable();
		}

		public AcnDeviceCategory getDeviceCategory() {
			return deviceCategory;
		}

		public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
			this.deviceCategory = deviceCategory;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public boolean isEditable() {
			return editable;
		}

		public long getNumDevices() {
			return numDevices;
		}

		public void setNumDevices(long numDevices) {
			this.numDevices = numDevices;
		}
	}

	public static class DeviceTypeDetailsModel extends DeviceTypeListModel {

		private List<DeviceTelemetry> telemetries;
		private String productTypeId;
		private Map<String, DeviceStateValueMetadata> stateMetadata;
		private List<DeviceAction> actions;

		public DeviceTypeDetailsModel() {
			super();
			this.telemetries = new ArrayList<>();
			this.stateMetadata = new HashMap<>();
			this.actions = new ArrayList<>();
		}

		public DeviceTypeDetailsModel(DeviceType deviceType) {
			super(deviceType);
			this.telemetries = deviceType.getTelemetries();
			this.productTypeId = deviceType.getRheaDeviceTypeId();
			this.stateMetadata = deviceType.getStateMetadata();
			this.actions = deviceType.getActions();
		}

		public List<DeviceAction> getActions() {
			return actions;
		}

		public void setActions(List<DeviceAction> actions) {
			this.actions = actions;
		}

		public List<DeviceTelemetry> getTelemetries() {
			return telemetries;
		}

		public void setTelemetries(List<DeviceTelemetry> telemetries) {
			this.telemetries = telemetries;
		}

		public String getProductTypeId() {
			return productTypeId;
		}

		public void setProductTypeId(String productTypeId) {
			this.productTypeId = productTypeId;
		}

		public Map<String, DeviceStateValueMetadata> getStateMetadata() {
			return stateMetadata;
		}

		public void setStateMetadata(Map<String, DeviceStateValueMetadata> stateMetadata) {
			this.stateMetadata = stateMetadata;
		}

	}

	public static class DeviceTypeOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -5200848137968936124L;

		public DeviceTypeOption() {
			super(null, null, null);
		}

		public DeviceTypeOption(DeviceType deviceType) {
			super(deviceType.getId(), deviceType.getHid(), deviceType.getName());
		}
	}

	public static class DeviceTelemetryModel implements Serializable {
		private static final long serialVersionUID = 847495772021303335L;

		private String name;
		private String description;
		private String typeName;
		private boolean chartable;

		public DeviceTelemetryModel() {
		}

		public DeviceTelemetryModel(DeviceTelemetry deviceTelemetry) {
			super();
			this.name = deviceTelemetry.getName();
			this.description = deviceTelemetry.getDescription();
			this.typeName = deviceTelemetry.getType().name();
			this.chartable = deviceTelemetry.getType().isChartable();
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

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public boolean isChartable() {
			return chartable;
		}

		public void setChartable(boolean chartable) {
			this.chartable = chartable;
		}

	}

	public static class DeviceTypeOptionsModel implements Serializable {
		private static final long serialVersionUID = 1014987585133307711L;

		private List<TelemetryUnitModels.TelemetryUnitOption> telemetryUnits;
		private List<String> telemetryTypes;
		private List<String> deviceStateValueTypes;
		// private List<RheaModels.DeviceCategoryOption> categories;
		private EnumSet<AcnDeviceCategory> deviceCategories;
		private List<RheaModels.DeviceManufacturerOption> manufacturers;
		private List<RheaModels.DeviceProductOption> products;
		private List<RheaModels.DeviceProductTypeOption> productTypes;
		private DeviceTypeSelectionModel selection;
		private List<DeviceActionTypeModels.DeviceActionTypeOption> actionTypes;
		private DeviceActionTypeConstants.PostBackURL.ContentType[] contentTypes;

		public DeviceTypeOptionsModel() {
			this.telemetryTypes = new ArrayList<>(TelemetryItemType.values().length);
			for (TelemetryItemType item : TelemetryItemType.values()) {
				if (!item.isSystem()) {
					this.telemetryTypes.add(item.name());
				}
			}
			this.telemetryTypes.sort(String.CASE_INSENSITIVE_ORDER);

			this.deviceStateValueTypes = new ArrayList<>(DeviceStateValueType.values().length);
			for (DeviceStateValueType item : DeviceStateValueType.values()) {
				this.deviceStateValueTypes.add(item.name());
			}
			this.deviceStateValueTypes.sort(String.CASE_INSENSITIVE_ORDER);
		}

		public DeviceActionTypeConstants.PostBackURL.ContentType[] getContentTypes() {
			return contentTypes;
		}

		public void setContentTypes(DeviceActionTypeConstants.PostBackURL.ContentType[] contentTypes) {
			this.contentTypes = contentTypes;
		}

		public List<DeviceActionTypeModels.DeviceActionTypeOption> getActionTypes() {
			return actionTypes;
		}

		public void setActionTypes(List<DeviceActionTypeModels.DeviceActionTypeOption> actionTypes) {
			this.actionTypes = actionTypes;
		}

		public List<TelemetryUnitModels.TelemetryUnitOption> getTelemetryUnits() {
			return telemetryUnits;
		}

		public void setTelemetryUnits(List<TelemetryUnitModels.TelemetryUnitOption> telemetryUnits) {
			this.telemetryUnits = telemetryUnits;
		}

		public List<String> getTelemetryTypes() {
			return telemetryTypes;
		}

		public List<String> getDeviceStateValueTypes() {
			return deviceStateValueTypes;
		}

		// public List<RheaModels.DeviceCategoryOption> getCategories() {
		// return categories;
		// }
		//
		// public void setCategories(List<RheaModels.DeviceCategoryOption>
		// categories) {
		// this.categories = categories;
		// }

		public EnumSet<AcnDeviceCategory> getDeviceCategories() {
			return deviceCategories;
		}

		public void setDeviceCategories(EnumSet<AcnDeviceCategory> deviceCategories) {
			this.deviceCategories = deviceCategories;
		}

		public List<RheaModels.DeviceManufacturerOption> getManufacturers() {
			return manufacturers;
		}

		public void setManufacturers(List<RheaModels.DeviceManufacturerOption> manufacturers) {
			this.manufacturers = manufacturers;
		}

		public List<RheaModels.DeviceProductOption> getProducts() {
			return products;
		}

		public void setProducts(List<RheaModels.DeviceProductOption> products) {
			this.products = products;
		}

		public List<RheaModels.DeviceProductTypeOption> getProductTypes() {
			return productTypes;
		}

		public void setProductTypes(List<RheaModels.DeviceProductTypeOption> productTypes) {
			this.productTypes = productTypes;
		}

		public DeviceTypeSelectionModel getSelection() {
			return selection;
		}

		public void setSelection(DeviceTypeSelectionModel selection) {
			this.selection = selection;
		}

	}

	public static class DeviceTypeSelectionModel implements Serializable {
		private static final long serialVersionUID = -167700528510596542L;

		private String manufacturerId;
		// private String categoryId;
		private AcnDeviceCategory deviceCategory;
		private String productId;
		private String productTypeId;

		public String getManufacturerId() {
			return manufacturerId;
		}

		public void setManufacturerId(String manufacturerId) {
			this.manufacturerId = manufacturerId;
		}

		// public String getCategoryId() {
		// return categoryId;
		// }
		//
		// public void setCategoryId(String categoryId) {
		// this.categoryId = categoryId;
		// }

		public AcnDeviceCategory getDeviceCategory() {
			return deviceCategory;
		}

		public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
			this.deviceCategory = deviceCategory;
		}

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getProductTypeId() {
			return productTypeId;
		}

		public void setProductTypeId(String productTypeId) {
			this.productTypeId = productTypeId;
		}

	}
}
