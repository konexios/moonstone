package com.arrow.rhea.web.model;

import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.rhea.data.DeviceCategory;

public class DeviceCategoryModels {
	public static class DeviceCategoryOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -2996820429331513139L;

		public DeviceCategoryOption() {
			super(null, null, null);
		}

		public DeviceCategoryOption(DeviceCategory deviceCategory) {
			super(deviceCategory.getId(), deviceCategory.getHid(), deviceCategory.getName());
		}
	}
}
