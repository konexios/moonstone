package com.arrow.pegasus.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CoreApiSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private AccessKeyAuthenticationProvider accessKeyAuthenticationProvider;

	private List<String> exceptionPaths;
	private boolean secured;

	public CoreApiSecurity() {
		exceptionPaths = new ArrayList<>();
		secured = true;
	}

	public CoreApiSecurity(boolean secured, List<String> exceptionPaths) {
		this();
		this.secured = secured;
		if (exceptionPaths != null) {
			this.exceptionPaths = exceptionPaths;
		}
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(accessKeyAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().permitAll();
		CoreApiKeyFilter filter = new CoreApiKeyFilter(secured, exceptionPaths);
		applicationContext.getAutowireCapableBeanFactory().autowireBean(filter);
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		http.csrf().disable();
	}
}
