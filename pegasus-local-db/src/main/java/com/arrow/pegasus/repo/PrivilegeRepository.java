package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.security.Privilege;

@Repository
public interface PrivilegeRepository extends MongoRepository<Privilege, String>, PrivilegeRepositoryExtension {
	Privilege findByName(String name);

	List<Privilege> findByEnabled(Boolean enabled);

	List<Privilege> findByProductIdAndEnabled(String productId, Boolean enabled);

	Page<Privilege> findAll(Pageable pageable);

	Privilege findBySystemName(String systemName);

	Long deleteByProductId(String productId);
}
