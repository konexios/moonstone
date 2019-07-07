package com.arrow.kronos;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.arrow.kronos.service.TelemetryProcessor;
import com.arrow.pegasus.CoreAppAbstract;
import com.arrow.pegasus.kafka.KafkaSender;
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
public class KronosEngine extends CoreAppAbstract {

	@Bean
	public AwsMqttTelemetryListener awsMqttTelemetryListener() {
		return new AwsMqttTelemetryListener();
	}

	@Bean
	public AzureIotHubListener azureIotHubListener() {
		return new AzureIotHubListener();
	}

	@Bean
	public IbmMqttTelemetryListener ibmMqttTelemetryListener() {
		return new IbmMqttTelemetryListener();
	}

	@Bean
	public MqttTelemetryListener mqttTelemetryListener() {
		return new MqttTelemetryListener();
	}

	@Bean
	public MqttApiRequestListener mqttApiRequestListener() {
		return new MqttApiRequestListener();
	}

	@Bean
	public TelemetryConsumer telemetryConsumer() {
		return new TelemetryConsumer();
	}

	@Bean
	public RheaCacheProxy rheaCacheProxy() {
		return new RemoteRheaCacheProxy();
	}

	@Bean
	public CoreCacheProxy coreCacheProxy() {
		return new com.arrow.pegasus.client.service.RemoteCoreCacheProxy();
	}

	@Bean
	public KronosEngineProperties kronosEngineProperties() {
		return new KronosEngineProperties();
	}

	@Bean
	public KafkaSender kafkaSender() {
		return new KafkaSender();
	}

	@Bean
	TelemetryProcessor telemetryProcessor() {
		return new TelemetryProcessor();
	}

	public static void main(String[] args) {
		// workaround for Elasticsearch to work
		System.setProperty("es.set.netty.runtime.available.processors", "false");

		SpringApplication app = new SpringApplication(KronosEngine.class);
		app.setBannerMode(Mode.OFF);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}
}
