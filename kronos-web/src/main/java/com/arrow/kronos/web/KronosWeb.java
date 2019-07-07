package com.arrow.kronos.web;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.arrow.pegasus.CoreWebAppAbstract;
import com.arrow.pegasus.service.CoreCacheProxy;
import com.arrow.rhea.client.service.RemoteRheaCacheProxy;
import com.arrow.rhea.service.RheaCacheProxy;

@SpringBootApplication
@ComponentScan(basePackages = { "com.arrow.pegasus", "com.arrow.kronos", "com.arrow.rhea" })
@EnableMongoRepositories(basePackages = { "com.arrow.pegasus", "com.arrow.kronos" })
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableRedisHttpSession
@EnableCaching
@EnableScheduling
@EntityScan(basePackages = { "com.arrow.kronos" })
@EnableElasticsearchRepositories(basePackages = { "com.arrow.pegasus", "com.arrow.kronos" })
@EnableAutoConfiguration(exclude = { RedisRepositoriesAutoConfiguration.class })
public class KronosWeb extends CoreWebAppAbstract {

    @Bean
    public CoreCacheProxy coreCacheProxy() {
        return new com.arrow.pegasus.client.service.RemoteCoreCacheProxy();
    }

    @Bean
    public RheaCacheProxy rheaCacheProxy() {
        return new RemoteRheaCacheProxy();
    }

    public static void main(String[] args) {
        // workaround for Elasticsearch to work
        System.setProperty("es.set.netty.runtime.available.processors", "false");

        SpringApplication app = new SpringApplication(KronosWeb.class);
        app.setBannerMode(Mode.OFF);
        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.run(args);
    }
}
