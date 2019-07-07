package com.arrow.kronos.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.PlatformConfig;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.PlatformConfigService;
import com.arrow.pegasus.webapi.data.UserModel;

@RestController
@RequestMapping("/api/kronos/security")
public class KronosSecurityController extends BaseControllerAbstract {

	@Autowired
	private PlatformConfigService platformConfigService;

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public UserModel user() {
		Product product = getCoreCacheService().findProductBySystemName(ProductSystemNames.KRONOS);
		Assert.notNull(product, "product is null");

		if (!StringUtils.isEmpty(product.getParentProductId())) {
			product.setRefParentProduct(getCoreCacheService().findProductById(product.getParentProductId()));
			Assert.notNull(product.getRefParentProduct(), "parentProduct is null");
		}

		User authenticatedUser = getAuthenticatedUser();

		Company company = getCoreCacheService().findCompanyById(authenticatedUser.getCompanyId());
		Assert.notNull(company, "company is null");

		PlatformConfig platformConfig = platformConfigService.getConfig();
		Assert.notNull(platformConfig, "platformConfig is null");

		return new UserModel(getAuthenticatedUser(), product, platformConfig.getRefZone(), company.getName());
	}
}
