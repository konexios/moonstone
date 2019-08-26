package com.arrow.pegasus.webapi;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.websso.WebSSOProfileOptions;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.arrow.pegasus.LoginRequiredException;
import com.arrow.pegasus.data.PlatformConfig;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.PlatformConfigService;
import com.arrow.pegasus.webapi.data.UserModel;

import moonstone.acs.client.model.StatusModel;

public abstract class SecurityApiAbstract extends WebApiAbstract {

	@Autowired
	private ApplicationContext ctx;
	@Autowired
	private PlatformConfigService platformConfigService;

	@RequestMapping(value = "/samlLogin")
	public StatusModel samlLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String method = "samlLogin";
		String returnUrl = request.getParameter("returnUrl");
		if (StringUtils.isEmpty(returnUrl)) {
			returnUrl = "/";
		}
		logInfo(method, "returnUrl: %s", returnUrl);
		SAMLEntryPoint entryPoint = new SAMLEntryPoint();
		WebSSOProfileOptions options = new WebSSOProfileOptions();
		options.setIncludeScoping(false);
		options.setRelayState(returnUrl);
		entryPoint.setDefaultProfileOptions(options);
		ctx.getAutowireCapableBeanFactory().autowireBean(entryPoint);
		entryPoint.afterPropertiesSet();
		entryPoint.commence(request, response, null);
		return StatusModel.OK;
	}

	@RequestMapping(value = "/login")
	public StatusModel login() {
		throw new LoginRequiredException();
	}

	@RequestMapping(value = "/token", method = RequestMethod.GET)
	public Map<String, String> token(HttpSession session) {
		if (session == null) {
			throw new LoginRequiredException();
		}
		return Collections.singletonMap("token", session.getId());
	}

	@RequestMapping(value = "/user/{productName}", method = RequestMethod.GET)
	public UserModel user(@PathVariable(value = "productName") String productName) {
		Product product = getCoreCacheService().findProductBySystemName(productName);
		Assert.notNull(product, "product is null");

		User authenticatedUser = getAuthenticatedUser();

		Company company = getCoreCacheService().findCompanyById(authenticatedUser.getCompanyId());
		Assert.notNull(company, "company is null");

		PlatformConfig platformConfig = platformConfigService.getConfig();
		Assert.notNull(platformConfig, "platformConfig is null");

		return new UserModel(authenticatedUser, product, platformConfig.getRefZone(), company.getName());
	}

	@RequestMapping(path = "/application/{applicationId}", method = RequestMethod.GET)
	public Application application(HttpSession session, @PathVariable String applicationId) {
		String method = "application";
		logInfo(method, "...");
		return setCurrentApplication(session, applicationId);
	}
}
