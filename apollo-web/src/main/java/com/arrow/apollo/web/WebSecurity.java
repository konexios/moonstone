package com.arrow.apollo.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.arrow.pegasus.CoreAccessDeniedHandler;
import com.arrow.pegasus.security.CoreWebSecurityAbstract;

@Configuration
// @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurity extends CoreWebSecurityAbstract {

    private final static String CSRF_TOKEN_NAME = "XSRF-TOKEN-APOLLO-WEB";

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
                .defaultSuccessUrl("/api/apollo/security/user", true)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            .and()
                .logout()
                .logoutSuccessUrl("/index.html")
                .invalidateHttpSession(true)
            .and()
                .csrf().disable()
                //.csrfTokenRepository(csrfTokenRepository()).ignoringAntMatchers()
            //.and()
                //.addFilterAfter(new CsrfHeaderFilter(configureCsrfTokenName()), CsrfFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(new CoreAccessDeniedHandler());
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
            "/widget-types/**",
            "/widget-properties/**",
            "/index.html",
            "/",
            "/partials/signin.html",
            "/partials/home.html",
            "/partials/forgotpassword.html",
            "/partials/changePassword.html",
            "/api/apollo/**",
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
