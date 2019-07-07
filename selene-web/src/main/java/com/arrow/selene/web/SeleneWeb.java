package com.arrow.selene.web;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import com.arrow.acs.JsonUtils;
import com.arrow.pegasus.security.Crypto;
import com.arrow.pegasus.security.CryptoClientImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@ComponentScan(basePackages = { "com.arrow.selene" })
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableSpringHttpSession
@EnableCaching
public class SeleneWeb {
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public MapSessionRepository sessionRepository() {
		return new MapSessionRepository();
	}

	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		return JsonUtils.getObjectMapper();
	}

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager();
	}

	@Bean
	public Crypto crypto() {
		return new CryptoClientImpl();
	}

	@Bean
	public SeleneAuthProvider seleneAuthProvider() {
		return new SeleneAuthProvider();
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SeleneWeb.class);
		app.setBannerMode(Mode.OFF);
		app.setWebApplicationType(WebApplicationType.SERVLET);
		app.run(args);

	}
}
