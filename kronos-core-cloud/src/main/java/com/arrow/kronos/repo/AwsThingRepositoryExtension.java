package com.arrow.kronos.repo;

import java.util.List;

import com.arrow.kronos.data.AwsThing;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface AwsThingRepositoryExtension extends RepositoryExtension<AwsThing> {
    List<AwsThing> findAwsThings(AwsThingSearchParams params);
}
