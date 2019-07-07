package com.arrow.apollo.web.model;

import com.arrow.pegasus.webapi.data.DefinitionModel;

public class WidgetTypeModels {

	public static class WidgetTypeOptionModel extends DefinitionModel {
		private static final long serialVersionUID = -8491437691214071894L;

		public WidgetTypeOptionModel(String id, String name, String description) {
			super(id, name, description);
		}
	}

	public static class WidgetTypeModel extends DefinitionModel {
		private static final long serialVersionUID = -4572774789198376548L;

		private String className;
		private String directive;

		public WidgetTypeModel(String id, String name, String description) {
			super(id, name, description);
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getDirective() {
			return directive;
		}

		public void setDirective(String directive) {
			this.directive = directive;
		}
	}
}
