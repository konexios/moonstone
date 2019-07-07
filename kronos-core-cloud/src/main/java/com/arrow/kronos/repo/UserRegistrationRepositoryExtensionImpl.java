package com.arrow.kronos.repo;

import com.arrow.kronos.data.UserRegistration;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class UserRegistrationRepositoryExtensionImpl extends RepositoryExtensionAbstract<UserRegistration>
        implements UserRegistrationRepositoryExtension {

	public UserRegistrationRepositoryExtensionImpl() {
		super(UserRegistration.class);
	}

}
