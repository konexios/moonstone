package com.arrow.pegasus.repo;

import com.arrow.pegasus.data.MigrationTask;

public class MigrationTaskRepositoryExtensionImpl extends RepositoryExtensionAbstract<MigrationTask>
        implements MigrationTaskRepositoryExtension {

    public MigrationTaskRepositoryExtensionImpl() {
        super(MigrationTask.class);
    }
}
