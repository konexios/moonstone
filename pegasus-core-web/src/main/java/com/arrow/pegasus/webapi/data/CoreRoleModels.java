package com.arrow.pegasus.webapi.data;

import com.arrow.pegasus.data.security.Role;

public class CoreRoleModels {

	public static class RoleOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 7550981100370864344L;

		public RoleOption(Role role) {
			super(role.getId(), role.getHid(), role.getName());
		}
	}
}
