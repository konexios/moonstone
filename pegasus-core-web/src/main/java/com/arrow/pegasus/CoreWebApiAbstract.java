package com.arrow.pegasus;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;

import com.arrow.pegasus.security.AccessKeyAuthenticationProvider;
import com.arrow.pegasus.security.CoreApiSecurity;

public abstract class CoreWebApiAbstract extends CoreWebAbstract {
	@Bean
	CoreApiSecurity coreApiSecurity() {
		return new CoreApiSecurity(isSecured(), Arrays.asList(getExceptionPaths()));
	}

	protected String[] getExceptionPaths() {
		return new String[0];
	}

	protected boolean isSecured() {
		return true;
	}

	@Bean
	AccessKeyAuthenticationProvider accessKeyAuthenticationProvider() {
		return new AccessKeyAuthenticationProvider();
	}
}
