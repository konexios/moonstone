/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acn.client.model;

import java.io.Serializable;

public class GlobalActionInputModel implements Serializable {
		private static final long serialVersionUID = -2068273575053334836L;
		
		private String name;
		private String type;
		private boolean required = false;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public boolean isRequired() {
			return required;
		}

		public void setRequired(boolean required) {
			this.required = required;
		}
		
		public GlobalActionInputModel withName(String name) {
			setName(name);
			return this;
		}
		
		public GlobalActionInputModel withType(String type) {
			setType(type);
			return this;
		}
		
		public GlobalActionInputModel withRequired(boolean required) {
			setRequired(required);
			return this;
		}
	}
