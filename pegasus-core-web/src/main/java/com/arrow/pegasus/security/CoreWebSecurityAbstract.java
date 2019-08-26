package com.arrow.pegasus.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import moonstone.acs.Loggable;

public abstract class CoreWebSecurityAbstract extends WebSecurityConfigurerAdapter {

	private Loggable logger = new Loggable(getClass().getName()) {
	};

	@Autowired
	private CoreAuthenticationProvider authenticationProvider;

	@Bean
	public CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName(String.format("X-%s", configureCsrfTokenName()));
		return repository;
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	protected Loggable getLogger() {
		return logger;
	}

	protected abstract String[] getHttpSecureExceptionPaths();
	
	protected abstract String configureCsrfTokenName();
	
	protected abstract String[] getCsrfHeaderExceptionPaths();
}
