package com.arrow.kronos.repo;

import com.arrow.kronos.data.KronosApplication;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class KronosApplicationRepositoryExtensionImpl extends RepositoryExtensionAbstract<KronosApplication>
        implements KronosApplicationRepositoryExtension {
	public KronosApplicationRepositoryExtensionImpl() {
		super(KronosApplication.class);
	}
}
