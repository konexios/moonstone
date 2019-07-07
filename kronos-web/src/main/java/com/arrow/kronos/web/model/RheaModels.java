package com.arrow.kronos.web.model;

import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.rhea.data.DeviceCategory;
import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.data.DeviceType;
import com.arrow.rhea.data.SoftwareRelease;

public class RheaModels {
	public static class DeviceCategoryOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -1259726966593933403L;

		public DeviceCategoryOption() {
			super(null, null, null);
		}

		public DeviceCategoryOption(DeviceCategory deviceCategory) {
			super(deviceCategory.getId(), deviceCategory.getHid(), deviceCategory.getName());
		}
	}

	public static class DeviceManufacturerOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 6887106834774235580L;

		public DeviceManufacturerOption() {
			super(null, null, null);
		}

		public DeviceManufacturerOption(DeviceManufacturer deviceManufacturer) {
			super(deviceManufacturer.getId(), deviceManufacturer.getHid(), deviceManufacturer.getName());
		}
	}

	public static class DeviceProductOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -1032380364424219478L;

		public DeviceProductOption() {
			super(null, null, null);
		}

		public DeviceProductOption(DeviceProduct deviceProduct) {
			super(deviceProduct.getId(), deviceProduct.getHid(), deviceProduct.getName());
		}
	}

	public static class DeviceProductTypeOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 1925197853715007146L;

		public DeviceProductTypeOption() {
			super(null, null, null);
		}

		public DeviceProductTypeOption(DeviceType deviceType) {
			super(deviceType.getId(), deviceType.getHid(), deviceType.getName());
		}
	}

	public static class SoftwareReleaseOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -7053819655718590520L;
		private static final String SOFTWARE_RELEASE_NAME = "%s %d.%d";

		public SoftwareReleaseOption() {
			super(null, null, null);
		}

		public SoftwareReleaseOption(SoftwareRelease softwareRelease, String softwareProduct) {
			super(softwareRelease.getId(), softwareRelease.getHid(),
			        String.format(SOFTWARE_RELEASE_NAME, softwareProduct, softwareRelease.getMajor(),
			                softwareRelease.getMinor())
			        + (softwareRelease.getBuild() != null ? "." + softwareRelease.getBuild() : ""));
		}
	}
}
