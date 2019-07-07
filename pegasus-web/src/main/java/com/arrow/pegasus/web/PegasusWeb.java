package com.arrow.pegasus.web;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.arrow.pegasus.CoreWebAppAbstract;
import com.arrow.pegasus.service.CoreCacheProxy;
import com.arrow.pegasus.service.LocalCoreCacheProxy;

@SpringBootApplication
@ComponentScan(basePackages = { "com.arrow.pegasus" })
@EnableMongoRepositories(basePackages = { "com.arrow.pegasus" })
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableRedisHttpSession
@EnableCaching
@EnableAutoConfiguration(exclude = RedisRepositoriesAutoConfiguration.class)
public class PegasusWeb extends CoreWebAppAbstract {

	@Bean
	CoreCacheProxy coreCacheProxy() {
		return new LocalCoreCacheProxy();
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(PegasusWeb.class);
		app.setBannerMode(Mode.OFF);
		app.setWebApplicationType(WebApplicationType.SERVLET);
		app.run(args);
	}
}
