package com.arrow.kronos.repo;

import com.arrow.kronos.data.AzureAccount;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class AzureAccountRepositoryExtensionImpl extends RepositoryExtensionAbstract<AzureAccount>
		implements AzureAccountRepositoryExtension {

	public AzureAccountRepositoryExtensionImpl() {
		super(AzureAccount.class);
	}
}
