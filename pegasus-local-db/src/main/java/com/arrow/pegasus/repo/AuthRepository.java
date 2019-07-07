package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.security.Auth;
import com.arrow.pegasus.data.security.AuthType;

@Repository
public interface AuthRepository extends MongoRepository<Auth, String>, AuthRepositoryExtension {
	List<Auth> findAllByCompanyId(String companyId);

	List<Auth> findAllByIdIn(List<String> companyId);

	List<Auth> findAllByCompanyIdAndType(String companyId, AuthType type);

	Auth findBySamlIdp(String samlIdp);

	Long deleteByCompanyId(String companyId);
}
