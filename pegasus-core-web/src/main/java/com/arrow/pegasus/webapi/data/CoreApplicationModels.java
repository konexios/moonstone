package com.arrow.pegasus.webapi.data;

import com.arrow.pegasus.data.YesNoInherit;
import com.arrow.pegasus.data.profile.Application;

public class CoreApplicationModels {

	public static class ApiSigningRequiredOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 6188244533600289821L;

		public ApiSigningRequiredOption(YesNoInherit yesNoInherit) {
			super(yesNoInherit.name(), yesNoInherit.name(), yesNoInherit.name());
		}
	}

	public static class ApplicationOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -2583942352323992851L;
		
		public ApplicationOption() {
			super(null, null, null);
		}

		public ApplicationOption(Application application) {
			super(application.getId(), application.getHid(), application.getName());
		}
	}
}
