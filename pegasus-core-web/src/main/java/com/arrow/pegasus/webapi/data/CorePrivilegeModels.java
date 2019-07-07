package com.arrow.pegasus.webapi.data;

import com.arrow.pegasus.data.security.Privilege;

public class CorePrivilegeModels {

	public static class PrivilegeOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -3725491435357547354L;

		private String category;
		private String description;

		public PrivilegeOption(Privilege privilege) {
			super(privilege.getId(), privilege.getHid(), privilege.getName());

			this.category = privilege.getCategory();
			this.description = privilege.getDescription();
		}

		public String getCategory() {
			return category;
		}

		public String getDescription() {
			return description;
		}
	}
}
