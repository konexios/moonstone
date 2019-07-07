package com.arrow.pegasus;

import org.springframework.context.annotation.Bean;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.web.http.CookieSerializer;

public abstract class CoreWebAbstract extends CoreAppAbstract {

	@Bean
	CookieSerializer cookieSerializer() {
		String simpleName = getClass().getSimpleName().toUpperCase();
		int idx = simpleName.indexOf('$');
		if (idx > 0) {
			simpleName = simpleName.substring(0, idx);
		}
		return new CoreSessionCookieSerializer(simpleName);
	}

	@Bean
	HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	CoreApplicationListener coreApplicationListener() {
		return new CoreApplicationListener();
	}
}