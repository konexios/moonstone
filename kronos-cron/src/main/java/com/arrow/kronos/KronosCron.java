package com.arrow.kronos;

import java.util.concurrent.Executor;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import com.arrow.pegasus.CoreAppAbstract;
import com.arrow.pegasus.service.CoreCacheProxy;
import com.arrow.rhea.client.service.RemoteRheaCacheProxy;
import com.arrow.rhea.service.RheaCacheProxy;

@SpringBootApplication
@ComponentScan(basePackages = { "com.arrow.pegasus", "com.arrow.kronos", "com.arrow.rhea" })
@EnableMongoRepositories(basePackages = { "com.arrow.pegasus", "com.arrow.kronos" })
@EnableCaching
@EnableScheduling
@EnableElasticsearchRepositories(basePackages = { "com.arrow.pegasus", "com.arrow.kronos" })
@EnableAutoConfiguration(exclude = { RedisRepositoriesAutoConfiguration.class })
public class KronosCron extends CoreAppAbstract {

	@Bean
	public RheaCacheProxy rheaCacheProxy() {
		return new RemoteRheaCacheProxy();
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return new ConcurrentTaskScheduler();
	}

	@Bean
	public Executor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	@Bean
	public CoreCacheProxy coreCacheProxy() {
		return new com.arrow.pegasus.client.service.RemoteCoreCacheProxy();
	}

	public static void main(String[] args) {
		// workaround for Elasticsearch to work
		System.setProperty("es.set.netty.runtime.available.processors", "false");

		SpringApplication app = new SpringApplication(KronosCron.class);
		app.setBannerMode(Mode.OFF);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}
}
