package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.repo.params.CompanySearchParams;

public class CompanyRepositoryExtensionImpl extends RepositoryExtensionAbstract<Company> implements CompanyRepositoryExtension {

	public CompanyRepositoryExtensionImpl() {
		super(Company.class);
	}

	@Override
	public List<Company> findByAbbrNames(String[] abbrNames) {
		return doFind(doProcessCriteria(addCriteria(new ArrayList<Criteria>(), "abbrName", abbrNames)));
	}

	/**
	 * This method was implemented prior to the implementation of
	 * CompanySearchParams.
	 *
	 * @deprecated use
	 *             {@link #findCompanies(com.arrow.pegasus.repo.params.CompanySearchParams params)}
	 *             instead.
	 */
	@Deprecated
	public List<Company> findByNameAndAbbrNameAndStatus(String name, String abbrName, EnumSet<CompanyStatus> status) {

		CompanySearchParams params = new CompanySearchParams();
		params.setName(name);
		params.setAbbrName(abbrName);
		params.setStatuses(status);

		return findCompanies(params);
	}

	@Override
	public List<Company> findRootCompaniesByStatus(EnumSet<CompanyStatus> statuses) {

		List<Criteria> criteria = new ArrayList<Criteria>();
		criteria = addCriteria(criteria, "status", statuses);
		criteria.add(Criteria.where("parentCompanyId").is(null));

		return doFind(doProcessCriteria(criteria));
	}

	@Override
	public List<Company> findByCompanyIdOrParentCompanyIdAndStatus(String companyId, EnumSet<CompanyStatus> statuses) {

		List<Criteria> criteria = new ArrayList<Criteria>();

		criteria = addCriteria(criteria, "status", statuses);

		if (!StringUtils.isEmpty(companyId))
			criteria.add(new Criteria().orOperator(Criteria.where("_id").is(companyId),
			        Criteria.where("parentCompanyId").is(companyId)));

		return doFind(doProcessCriteria(criteria));
	}

	public Page<Company> findCompanies(Pageable pageable, CompanySearchParams params) {
		Assert.notNull(pageable, "pageable is null");
		Assert.notNull(params, "params is null");

		String method = "findCompanies";
		logInfo(method, "...");

		return doProcessQuery(pageable, buildCriteria(params));
	}

	public List<Company> findCompanies(CompanySearchParams params) {
		Assert.notNull(params, "params is null");

		String method = "findCompanies";
		logInfo(method, "...");

		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(CompanySearchParams params) {

		List<Criteria> criteria = new ArrayList<>();

		criteria = addCriteria(criteria, "_id", params.getIds());
		criteria = addCriteria(criteria, "hid", params.getHids());
		criteria = addCriteria(criteria, "createdBy", params.getCreatedBys());
		criteria = addCriteria(criteria, "lastModifiedBy", params.getLastModifiedBys());

		if (!StringUtils.isEmpty(params.getName()))
			criteria.add(Criteria.where("name").regex(params.getName(), "i"));

		if (!StringUtils.isEmpty(params.getAbbrName()))
			criteria.add(Criteria.where("abbrName").regex(params.getAbbrName(), "i"));

		criteria = addCriteria(criteria, "status", params.getStatuses());
		criteria = addCriteria(criteria, "parentCompanyId", params.getParentCompanyIds());

		return criteria;
	}

	@Override
	public List<Company> findByCompanyIdOrParentCompanyId(String companyId, CompanySearchParams params) {
		Assert.notNull(params, "params is null");

		String method = "findByCompanyIdOrParentCompanyId";
		logInfo(method, "...");

		return doProcessQuery(buildCompanyIdOrParentCompanyIdCriteria(companyId, params));
	}

	@Override
	public Page<Company> findByCompanyIdOrParentCompanyId(Pageable pageable, String companyId,
	        CompanySearchParams params) {
		Assert.notNull(pageable, "pageable is null");
		Assert.notNull(params, "params is null");

		String method = "findByCompanyIdOrParentCompanyId";
		logInfo(method, "...");

		return doProcessQuery(pageable, buildCompanyIdOrParentCompanyIdCriteria(companyId, params));
	}

	private List<Criteria> buildCompanyIdOrParentCompanyIdCriteria(String companyId, CompanySearchParams params) {
		Assert.hasText(companyId, "companyId is empty");

		List<Criteria> criteria = new ArrayList<>();
		criteria = addCriteria(criteria, "hid", params.getHids());
		criteria = addCriteria(criteria, "createdBy", params.getCreatedBys());
		criteria = addCriteria(criteria, "lastModifiedBy", params.getLastModifiedBys());

		if (!StringUtils.isEmpty(params.getName()))
			criteria.add(Criteria.where("name").regex(params.getName(), "i"));

		if (!StringUtils.isEmpty(params.getAbbrName()))
			criteria.add(Criteria.where("abbrName").regex(params.getAbbrName(), "i"));

		criteria = addCriteria(criteria, "status", params.getStatuses());

		criteria.add(new Criteria().orOperator(Criteria.where("_id").is(companyId),
		        Criteria.where("parentCompanyId").is(companyId)));

		return criteria;
	}
}
