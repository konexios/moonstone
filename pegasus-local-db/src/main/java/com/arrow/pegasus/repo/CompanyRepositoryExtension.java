package com.arrow.pegasus.repo;

import java.util.EnumSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.repo.params.CompanySearchParams;

public interface CompanyRepositoryExtension extends RepositoryExtension<Company> {

	public List<Company> findByAbbrNames(String[] abbrNames);

	/**
	 * This method was implemented prior to the implementation of
	 * CompanySearchParams.
	 *
	 * @deprecated use
	 *             {@link #findCompanies(com.arrow.pegasus.repo.params.CompanySearchParams params)}
	 *             instead.
	 */
	@Deprecated
	public List<Company> findByNameAndAbbrNameAndStatus(String name, String abbrName, EnumSet<CompanyStatus> status);

	/**
	 * @param statuses
	 * @return
	 */
	public List<Company> findRootCompaniesByStatus(EnumSet<CompanyStatus> statuses);

	/**
	 * @param companyId
	 * @param statuses
	 * @return
	 */
	public List<Company> findByCompanyIdOrParentCompanyIdAndStatus(String companyId, EnumSet<CompanyStatus> statuses);

	/**
	 * @param pageable
	 * @param params
	 * @return
	 */
	public Page<Company> findCompanies(Pageable pageable, CompanySearchParams params);

	/**
	 * @param params
	 * @return
	 */
	public List<Company> findCompanies(CompanySearchParams params);

	/**
	 * @param companyId
	 * @param params
	 * @return
	 */
	public List<Company> findByCompanyIdOrParentCompanyId(String companyId, CompanySearchParams params);

	/**
	 * @param pageable
	 * @param companyId
	 * @param params
	 * @return
	 */
	public Page<Company> findByCompanyIdOrParentCompanyId(Pageable pageable, String companyId,
	        CompanySearchParams params);
}
