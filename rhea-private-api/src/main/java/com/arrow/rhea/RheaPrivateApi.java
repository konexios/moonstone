package com.arrow.rhea;

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

import com.arrow.pegasus.CoreWebApiAbstract;
import com.arrow.pegasus.client.service.RemoteCoreCacheProxy;
import com.arrow.pegasus.service.CoreCacheProxy;
import com.arrow.rhea.service.LocalRheaCacheProxy;
//import com.arrow.pegasus.service.CoreCacheProxy;
//import com.arrow.pegasus.service.LocalCoreCacheProxy;
import com.arrow.rhea.service.RheaCacheProxy;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = { "com.arrow.pegasus", "com.arrow.rhea" })
@EnableMongoRepositories(basePackages = { "com.arrow.pegasus", "com.arrow.rhea" })
@EnableGlobalMethodSecurity
@EnableRedisHttpSession
@EnableCaching
@EnableSwagger2
@EnableAutoConfiguration(exclude = RedisRepositoriesAutoConfiguration.class)
public class RheaPrivateApi extends CoreWebApiAbstract {

	@Bean()
	public RheaCacheProxy rheaCacheProxy() {
		return new LocalRheaCacheProxy();
	}

	@Bean
	public CoreCacheProxy coreCacheProxy() {
		return new RemoteCoreCacheProxy();
	}

	@Override
	protected String[] getExceptionPaths() {
		return new String[] { "/swagger-ui.html", "/swagger-resources/configuration/ui",
				"/swagger-resources/configuration/security", "/swagger-resources", "/v2/api-docs",
				"/swagger-resources/configuration/security", "/webjars/springfox-swagger-ui/.*", "/status" };
	}

	@Override
	protected boolean isSecured() {
		return false;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(RheaPrivateApi.class);
		app.setBannerMode(Mode.OFF);
		app.setWebApplicationType(WebApplicationType.SERVLET);
		app.run(args);
	}
}
