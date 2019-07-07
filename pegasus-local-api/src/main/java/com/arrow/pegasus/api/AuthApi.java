package com.arrow.pegasus.api;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.security.Auth;

@RestController(value = "localPegasusAuthApi")
@RequestMapping("/api/v1/local/pegasus/auths")
public class AuthApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.GET)
	public Auth findBySamlIdp(@RequestParam(name = "samlIdp", required = true) String remoteEntityId) {
		Assert.hasText(remoteEntityId, "samlIdp is empty");
		Auth auth = getAuthService().getAuthRepository().findBySamlIdp(remoteEntityId);
		Assert.notNull(auth, "auth not found");
		return auth;
	}
}
