package com.arrow.rhea.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.repo.RepositoryExtension;
import com.arrow.rhea.data.SoftwareProduct;

public interface SoftwareProductRepositoryExtension extends RepositoryExtension<SoftwareProduct> {
	Page<SoftwareProduct> findSoftwareProducts(Pageable pageable, SoftwareProductSearchParams params);

	List<SoftwareProduct> findSoftwareProducts(SoftwareProductSearchParams params);
}
