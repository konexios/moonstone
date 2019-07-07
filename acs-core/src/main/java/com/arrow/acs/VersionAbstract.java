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
package com.arrow.acs;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

import com.arrow.acs.client.model.VersionModel;

public abstract class VersionAbstract {
	protected final static Loggable LOGGER = new Loggable(VersionAbstract.class.getName()) {
	};

	protected static VersionModel readManifest(Class<?> clazz) {
		VersionModel model = new VersionModel().withMajor(0).withMinor(0).withBuild(0).withName("Unknown")
		        .withDescription("Unknown");
		try {
			Manifest manifest = ManifestUtils.readManifest(clazz);
			Attributes attrs = manifest.getMainAttributes();
			String title = attrs.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
			model.withName(title).withDescription(title);
			String version = attrs.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
			String[] tokens = version.split("\\.");
			if (tokens.length > 0) {
				try {
					model.withMajor(Integer.parseInt(tokens[0]));
				} catch (Exception e) {
				}
				if (tokens.length > 1) {
					try {
						model.withMinor(Integer.parseInt(tokens[1]));
					} catch (Exception e) {
					}
					if (tokens.length > 2) {
						try {
							model.withBuild(Integer.parseInt(tokens[2]));
						} catch (Exception e) {
						}
					}
				}
			}
			model.withVendor(attrs.getValue(Attributes.Name.IMPLEMENTATION_VENDOR));
			model.withBuiltBy(attrs.getValue("Built-By"));
			model.withBuiltDate(attrs.getValue("Built-Date"));
			model.withBuiltJdk(attrs.getValue("Built-Jdk"));
			model.withGitBranch(attrs.getValue("Git-Branch"));
			model.withGitLastCommit(attrs.getValue("Git-Last-Commit"));
		} catch (Throwable t) {
		}
		return model;
	}
}
