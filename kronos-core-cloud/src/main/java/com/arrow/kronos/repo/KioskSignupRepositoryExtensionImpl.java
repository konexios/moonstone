package com.arrow.kronos.repo;

import com.arrow.kronos.data.KioskSignup;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class KioskSignupRepositoryExtensionImpl extends RepositoryExtensionAbstract<KioskSignup>
        implements KioskSignupRepositoryExtension {

	public KioskSignupRepositoryExtensionImpl() {
		super(KioskSignup.class);
	}
}
