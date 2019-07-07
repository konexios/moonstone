package com.arrow.kronos;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.arrow.kronos.service.TelemetryProcessor;
import com.arrow.pegasus.CoreWebApiAbstract;
import com.arrow.pegasus.client.service.RemoteCoreCacheProxy;
import com.arrow.pegasus.kafka.KafkaSender;
import com.arrow.pegasus.service.CoreCacheProxy;
import com.arrow.rhea.client.service.RemoteRheaCacheProxy;
import com.arrow.rhea.service.RheaCacheProxy;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = { "com.arrow.pegasus", "com.arrow.kronos", "com.arrow.rhea.client" })
@EnableMongoRepositories(basePackages = { "com.arrow.pegasus", "com.arrow.kronos" })
@EnableGlobalMethodSecurity
@EnableCaching
@EnableSwagger2
@EnableAutoConfiguration(exclude = { SessionAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class })
@EnableElasticsearchRepositories(basePackages = { "com.arrow.pegasus", "com.arrow.kronos" })
@EnableKafka
public class KronosApi extends CoreWebApiAbstract {

	@Bean
	public CoreCacheProxy coreCacheProxy() {
		return new RemoteCoreCacheProxy();
	}

	@Bean
	public RheaCacheProxy rheaCacheProxy() {
		return new RemoteRheaCacheProxy();
	}

	@Bean
	KafkaSender kafkaSender() {
		return new KafkaSender();
	}

	@Bean
	TelemetryProcessor telemetryProcessor() {
		return new TelemetryProcessor();
	}

	@Override
	protected String[] getExceptionPaths() {
		// @formatter:off
		return new String[] { "/api/v1/kronos/rabbitmq-auth/.*",

				// swagger
				"/swagger-ui.html", "/configuration/ui", "/configuration/security", "/swagger-resources",
				"/swagger-resources/configuration/ui", "/swagger-resources/configuration/security", "/v2/api-docs",
				"/webjars/springfox-swagger-ui/.*", "/status", "/favicon.ico" };
		// @formatter:on
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				RequestMethod[] allMethods = RequestMethod.values();
				registry.addMapping("/api/**")
						.allowedMethods(Arrays.asList(allMethods).stream().map(RequestMethod::name)
								.collect(Collectors.toList()).toArray(new String[allMethods.length]))
						.allowedOrigins("*").allowedHeaders("*").allowCredentials(true);
			}
		};
	}

	public static void main(String[] args) {
		// workaround for Elasticsearch to work
		System.setProperty("es.set.netty.runtime.available.processors", "false");

		SpringApplication app = new SpringApplication(KronosApi.class);
		app.setBannerMode(Mode.OFF);
		app.setWebApplicationType(WebApplicationType.SERVLET);
		app.run(args);
	}
}
