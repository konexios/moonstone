package com.arrow.kronos.repo;

import com.arrow.kronos.data.KronosUser;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class KronosUserRepositoryExtensionImpl extends RepositoryExtensionAbstract<KronosUser>
        implements KronosUserRepositoryExtension {
	public KronosUserRepositoryExtensionImpl() {
		super(KronosUser.class);
	}
}
