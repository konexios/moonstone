package com.arrow.pegasus.repo;

import com.arrow.pegasus.data.security.Auth;

public class AuthRepositoryExtensionImpl extends RepositoryExtensionAbstract<Auth> implements AuthRepositoryExtension {

    public AuthRepositoryExtensionImpl() {
        super(Auth.class);
    }
}
