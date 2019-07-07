package com.arrow.kronos.repo;

import com.arrow.kronos.data.AwsAccount;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class AwsAccountRepositoryExtensionImpl extends RepositoryExtensionAbstract<AwsAccount>
        implements AwsAccountRepositoryExtension {

    public AwsAccountRepositoryExtensionImpl() {
        super(AwsAccount.class);
    }
}
