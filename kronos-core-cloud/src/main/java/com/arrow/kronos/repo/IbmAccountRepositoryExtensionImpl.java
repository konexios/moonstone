package com.arrow.kronos.repo;

import com.arrow.kronos.data.IbmAccount;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class IbmAccountRepositoryExtensionImpl extends RepositoryExtensionAbstract<IbmAccount>
        implements IbmAccountRepositoryExtension {

    public IbmAccountRepositoryExtensionImpl() {
        super(IbmAccount.class);
    }
}
