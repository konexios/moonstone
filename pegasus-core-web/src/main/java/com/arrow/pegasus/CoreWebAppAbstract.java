package com.arrow.pegasus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ForwardedHeaderFilter;

import com.arrow.pegasus.security.CoreAuthenticationProvider;
import com.arrow.pegasus.security.CoreUserDetailsService;

public abstract class CoreWebAppAbstract extends CoreWebAbstract {

	// 0 means use the value from RedisHttpSessionConfiguration
	@Value("${server.session.timeout:0}")
	private int serverSessionTimeout;

	@Bean
	CoreAuthenticationProvider coreAuthenticationProvider() {
		return new CoreAuthenticationProvider();
	}

	@Bean
	CoreUserDetailsService coreUserDetailsService() {
		return new CoreUserDetailsService();
	}

	@Bean
	ForwardedHeaderFilter forwardedHeaderFilter() {
		return new ForwardedHeaderFilter();
	}
}
