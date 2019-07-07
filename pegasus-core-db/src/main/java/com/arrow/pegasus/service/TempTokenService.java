package com.arrow.pegasus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.data.TempToken;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.repo.TempTokenRepository;

@Service
public class TempTokenService extends ServiceAbstract {

	@Autowired
	private CoreCacheService coreCache;
	@Autowired
	private TempTokenRepository tempTokenRepository;

	public TempTokenRepository getTempTokenRepository() {
		return tempTokenRepository;
	}

	public TempToken create(TempToken tempToken, String who) {
		Assert.notNull(tempToken, "tempToken is null");
		Assert.hasText(who, "who is empty");

		if (StringUtils.isNotEmpty(tempToken.getApplicationId())) {
			Application application = coreCache.findApplicationById(tempToken.getApplicationId());
			checkEnabled(application, "application");
			tempToken.setCompanyId(application.getCompanyId());
		} else if (StringUtils.isNotEmpty(tempToken.getCompanyId())) {
			Company company = coreCache.findCompanyById(tempToken.getCompanyId());
			Assert.isTrue(company != null && company.getStatus() == CompanyStatus.Active,
			        "company not found or not active: " + tempToken.getCompanyId());
		} else {
			throw new AcsLogicalException("Either applicationId or companyId must be provided");
		}

		tempTokenRepository.doInsert(tempToken, who);

		return tempToken;
	}

	public TempToken update(TempToken tempToken, String who) {
		Assert.notNull(tempToken, "tempToken is null");
		Assert.hasText(who, "who is empty");

		tempTokenRepository.doSave(tempToken, who);

		return tempToken;
	}

	public boolean isValidToken(TempToken tempToken) {

		return false;
	}
}