package com.arrow.pegasus.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CsrfFilter;

import com.arrow.pegasus.security.CoreSwitchUserFilter;
import com.arrow.pegasus.security.CoreWebSecurityAbstract;
import com.arrow.pegasus.security.CsrfHeaderFilter;

@Configuration
// @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurity extends CoreWebSecurityAbstract {
	private final static String CSRF_TOKEN_NAME = "XSRF-TOKEN-PEGASUS-WEB";

	@Bean
	CoreSwitchUserFilter coreSwitchUserFilter() {
		return new CoreSwitchUserFilter();
	}

	@Override
	// @formatter:off
    protected void configure(HttpSecurity http) throws Exception {

		final String defaultSuccessUrl = "/api/pegasus/security/user";

		http
                .authorizeRequests()
                .antMatchers(getHttpSecureExceptionPaths())
                .permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .formLogin()
                .loginPage("/api/v1/core/security/login")
                .defaultSuccessUrl(defaultSuccessUrl, true)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
				.successHandler(new CustomSuccessHandler(defaultSuccessUrl))
            .and()
                .logout()
                .logoutSuccessUrl("/index.html")
                .invalidateHttpSession(true)
            .and()
                .csrf()
                .csrfTokenRepository(csrfTokenRepository()).ignoringAntMatchers(
                		"/api/v1/core/security/login",
                        "/status")
            .and()
                .addFilterAfter(new CsrfHeaderFilter(configureCsrfTokenName(), getCsrfHeaderExceptionPaths()), CsrfFilter.class)
                .addFilterAfter(new PegasusExceptionFilter(), LogoutFilter.class)
                .addFilterAfter(coreSwitchUserFilter(), FilterSecurityInterceptor.class)
                .exceptionHandling()
                .accessDeniedHandler(new PegasusAccessDeniedHandler());

        // restrict user to only one session at at time
        // TODO temporarily commented out, need to move to configuration
        // http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false).sessionRegistry(sessionRegistry());
    }
    // @formatter:on

	@Override
	protected String[] getHttpSecureExceptionPaths() {
		// @formatter:off
		return new String[] {
			"/webjars/**",
            "/assets/css/**",
            "/assets/img/**",
            "/assets/vender/**",
            "/node_modules/**",
            "/scripts/**",
            "/index.html",
            "/",
            "/partials/signin.html",
            "/partials/forgotPassword.html",
            "/partials/security/change-password.html",
            "/api/pegasus/**",
            "/status",
            "/favicon.ico"};
		// @formatter:on
	}

	@Override
	protected String configureCsrfTokenName() {
		return CSRF_TOKEN_NAME;
	}

	@Override
	protected String[] getCsrfHeaderExceptionPaths() {
		// @formatter:off
		return new String[] {
                "/status"};
		// @formatter:on
	}
}
