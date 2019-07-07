package com.arrow.apollo.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.apollo.web.model.LoginModels;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.PlatformConfig;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserAuth;
import com.arrow.pegasus.data.security.AuthType;
import com.arrow.pegasus.service.PlatformConfigService;
import com.arrow.pegasus.webapi.data.UserModel;

@RestController
@RequestMapping("/api/apollo/security")
public class ApolloSecurityController extends ApolloControllerAbstract {

	@Autowired
	private PlatformConfigService platformConfigService;

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public UserModel user() {
		Product product = getCoreCacheService().findProductBySystemName(ProductSystemNames.APOLLO);
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

	/**
	 * This Method is used to check User Authentication type by calling
	 * ClientUserApi . Method return list of Authentication type and check and
	 * return for Authenticationtype Native or SSO/LDAP.
	 */
	@RequestMapping(value = "/getUserAuthenticationType/{userName:.+}", method = RequestMethod.GET)
	public LoginModels.AutheticationDetails getUserAuthenticationType(@PathVariable String userName) {
		AuthType authType = null;

		if (!StringUtils.isEmpty(userName)) {
			User user = getClientUserApi().findByLogin(userName);
			Assert.notNull(user, "User is null");

			for (UserAuth userAuth : user.getAuths()) {
				if (userAuth.isEnabled()) {
					authType = userAuth.getType();
					break;
				}
			}
		}

		return new LoginModels.AutheticationDetails((authType == null ? StringUtils.EMPTY : authType.name()));
	}

	// @RequestMapping(value = "/getUserPersona/{id:.+}", method =
	// RequestMethod.GET)
	// public Map<String, String> getUserPersona(@PathVariable String id) {
	// String userType = "";
	// Map<String, String> h = new HashMap<String, String>();
	//
	// ServiceRuntime s =
	// getDatabaseRepository().getServiceRuntimeRepository().findById(id);
	// UserRuntime u =
	// getDatabaseRepository().getUserRuntimeRepository().findById(s.getUserRuntimeId());
	// if (u.getUserAuthorities().contains("APOLLO_CREATE_PERSONA")) {
	// userType = ApolloDynamicPersonaConstant.APOLLO_CREAT_PERSONA;
	// } else if
	// (u.getUserAuthorities().contains("APOLLO_SYSTEM_ADMINISTRATOR_PERSONA"))
	// {
	// userType = ApolloDynamicPersonaConstant.APOLLO_SYSTEMADMIN_PERSONA;
	// } else if (u.getUserAuthorities().contains("APOLLO_READ_ONLY_PERSONA")) {
	// userType = ApolloDynamicPersonaConstant.APOLLO_READONLY_PERSONA;
	// }
	// h.put("personaType", userType);
	// return h;
	// }
}