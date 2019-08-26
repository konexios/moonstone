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
package moonstone.acn.client;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public final class AcnClientVersion extends VersionAbstract {
	private final static VersionModel VERSION = readManifest(AcnClientVersion.class);

	public static VersionModel get() {
		return VERSION != null ? VERSION : VersionModel.UNDEFINED;
	}
}
