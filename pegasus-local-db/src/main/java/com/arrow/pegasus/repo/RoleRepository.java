package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.security.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String>, RoleRepositoryExtension {
	Role findFirstByNameAndApplicationId(String name, String applicationId);

	List<Role> findByEnabled(boolean enabled);

	List<Role> findByApplicationIdAndEnabled(String applicationId, boolean enabled);

	List<Role> findByApplicationId(String applicationId);

	Page<Role> findAllByApplicationId(Pageable pageable, String applicationId);

	Role findByIdAndApplicationId(String id, String applicationId);

	Page<Role> findAll(Pageable pageable);

	Long deleteByProductId(String productId);

	Long deleteByApplicationId(String applicationId);
}
