package com.arrow.pegasus.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.Loggable;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.security.Auth;
import com.arrow.pegasus.service.AuthenticationService;
import com.arrow.pegasus.service.CoreCacheHelper;
import com.arrow.pegasus.service.CoreCacheProxy;

@Service
public class SamlUserDetailsService extends Loggable implements SAMLUserDetailsService {

	@Autowired
	private CoreCacheProxy coreCacheService;
	@Autowired
	private CoreCacheHelper coreCacheHelper;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private Crypto crypto;

	@Override
	public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
		String method = "loadUserBySAML";
		String nameId = credential.getNameID().getValue();
		String idp = credential.getRemoteEntityID();
		logInfo(method, "nameId: %s, idp: %s", nameId, idp);
		User user = coreCacheHelper.populateUser(authenticationService.samlAuthenticate(idp, nameId));
		if (user == null) {
			user = authenticationService.findByLogin(CoreConstant.ANONYMOUS_USER);
			Assert.notNull(user, "ERROR: anonymous user not defined!");
		} else {
			// decrypt user's login
			user.setLogin(crypto.internalDecrypt(user.getLogin()));
		}

		Auth auth = coreCacheService.findAuthBySamlIdp(idp);
		return new SamlUserDetails(user, credential, auth);
	}
}
