package com.arrow.kronos.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.csrf.CsrfFilter;

import com.arrow.pegasus.security.CoreWebSecurityAbstract;
import com.arrow.pegasus.security.CsrfHeaderFilter;

@Configuration
// @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurity extends CoreWebSecurityAbstract {
	private final static String CSRF_TOKEN_NAME = "XSRF-TOKEN-KRONOS-WEB";

	@Value("${server.error.path:${error.path:/error}}")
	private String errorPath;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
        http   
	        .authorizeRequests()
	        .antMatchers(getHttpSecureExceptionPaths())
	        .permitAll()
	        .anyRequest()
	        .authenticated()
	    .and()
	        .formLogin()
	        .loginPage("/api/v1/core/security/login")
	        .defaultSuccessUrl("/api/kronos/security/user", true)
	        .usernameParameter("username")
	        .passwordParameter("password")
	        .permitAll()
	    .and()
	        .logout()
	        .logoutSuccessUrl("/index.html")
	        .invalidateHttpSession(true)
	    .and()
	        .csrf()
	        .csrfTokenRepository(csrfTokenRepository()).ignoringAntMatchers(
        		"/api/kronos/social-event/*", 
        		"/api/v1/core/security/login",
                "/status")
	    .and()
	        .addFilterAfter(new CsrfHeaderFilter(configureCsrfTokenName(), getCsrfHeaderExceptionPaths()), CsrfFilter.class)
	        .exceptionHandling()
	            .accessDeniedHandler(new KronosAccessDeniedHandler());
        // @formatter:on 

		// restrict user to only one session at at time
		// TODO temporarily commented out, need to move to configuration
		// http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false).sessionRegistry(sessionRegistry());
	}

	@Override
	protected String[] getHttpSecureExceptionPaths() {
		// @formatter:off
		return new String[] {
			"/webjars/**",
            "/assets/css/**",
            "/assets/img/**",
            "/assets/vender/**",
            "/assets/fonts/**",
            "/node_modules/**",
            "/scripts/**",
            "/index.html",
            "/kiosk.html",
            "/",
            "/partials/events/*",
            "/partials/kiosk/setup.html",
            "/partials/kiosk/kiosk.html",
            "/partials/signup.html",
            "/partials/signin.html",
            "/partials/verify.html",
            "/partials/home.html",
            "/partials/user/forgotpassword.html",
            "/partials/user/change-password.html",
            "/api/kronos/user/password-policy/*",
            "/api/kronos/user/change-password/*",
            "/api/kronos/user/reset-password/*",
            "/api/kronos/user/verify-account/*",
            "/api/kronos/registration/*",
            "/api/kronos/registration/*/preregistration",
            "/api/kronos/registration/*/verify",
            "/api/kronos/registration/*/resendVerifyEmail",
            "/api/kronos/kiosk/*",
            "/api/kronos/version/*",
            "/api/kronos/social-event/*",
            "/status",
            "/favicon.ico",
            errorPath};
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
