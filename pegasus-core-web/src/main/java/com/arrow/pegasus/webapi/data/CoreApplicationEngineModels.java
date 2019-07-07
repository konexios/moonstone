package com.arrow.pegasus.webapi.data;

import com.arrow.pegasus.data.ApplicationEngine;

public class CoreApplicationEngineModels {

	public static class ApplicationEngineOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -52057952462557940L;

		public ApplicationEngineOption(ApplicationEngine applicationEngine) {
			super(applicationEngine.getId(), applicationEngine.getHid(), applicationEngine.getName());
		}
	}
}
