package com.arrow.kronos.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.webapi.data.CoreCompanyModels;

@RestController
@RequestMapping("/api/kronos/companies")
public class CompanyController extends BaseControllerAbstract {

	@RequestMapping(value = "/company", method = RequestMethod.GET)
	public CoreCompanyModels.CompanyOption myCompany(HttpSession session) {
		Assert.notNull(session, "session is null");

		Application application = getApplication(session);
		Assert.notNull(application, "application is null");

		Company company = getCoreCacheService().findCompanyById(application.getCompanyId());
		Assert.notNull(company, "company not found; companyId = " + application.getCompanyId());

		return new CoreCompanyModels.CompanyOption(company);
	}
}
