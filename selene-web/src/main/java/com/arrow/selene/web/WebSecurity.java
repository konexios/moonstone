package com.arrow.selene.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Component;

@Component
// @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private static final String X_XSRF_TOKEN = "X-XSRF-TOKEN";

	@Bean
	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName(X_XSRF_TOKEN);
		return repository;
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Autowired
	private SeleneAuthProvider seleneAuthProvider;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(seleneAuthProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.authorizeRequests()
				.antMatchers("/node_modules/**", "/assets/css/**", "/assets/img/**", "/assets/vender/**", "/scripts/**",
						"/", "/index.html", "/partials/signin.html", "/api/selene/security/user")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/api/selene/security/login")
				.defaultSuccessUrl("/api/selene/security/user").usernameParameter("username")
				.passwordParameter("password").permitAll().and().logout().logoutSuccessUrl("/index.html")
				.invalidateHttpSession(true).and().csrf().csrfTokenRepository(csrfTokenRepository())
				.ignoringAntMatchers().and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
		// @formatter:on

		// restrict user to only one session at at time
		http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false).sessionRegistry(sessionRegistry());
	}
}
