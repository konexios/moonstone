package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.arrow.kronos.data.GlobalTag;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface GlobalTagRepositoryExtension extends RepositoryExtension<GlobalTag> {

	List<GlobalTag> findGlobalTags(GlobalTagRepositoryParams params, Sort sort);

	List<GlobalTag> findGlobalTags(GlobalTagRepositoryParams params);

	Page<GlobalTag> findGlobalTags(Pageable pageable, GlobalTagRepositoryParams params);
}
