package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String>, CompanyRepositoryExtension {

	Company findByName(String name);

	Company findByAbbrName(String abbrName);

	List<Company> findByStatus(CompanyStatus status);

	List<Company> findByParentCompanyId(String parentCompanyId);

	List<Company> findByIdOrParentCompanyIdAndStatus(String id, String parentCompanyId, CompanyStatus status);

	Long deleteByParentCompanyId(String parentCompanyId);
}
