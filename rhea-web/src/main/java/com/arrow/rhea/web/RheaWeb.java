package com.arrow.rhea.web;

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
import com.arrow.pegasus.client.service.RemoteCoreCacheProxy;
import com.arrow.pegasus.service.CoreCacheProxy;
import com.arrow.rhea.service.LocalRheaCacheProxy;
import com.arrow.rhea.service.RheaCacheProxy;

@SpringBootApplication
@ComponentScan(basePackages = { "com.arrow.pegasus", "com.arrow.rhea" })
@EnableMongoRepositories(basePackages = { "com.arrow.pegasus", "com.arrow.rhea" })
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableRedisHttpSession
@EnableCaching
@EnableAutoConfiguration(exclude = RedisRepositoriesAutoConfiguration.class)
public class RheaWeb extends CoreWebAppAbstract {

    @Bean
    public RheaCacheProxy rheaCacheProxy() {
        return new LocalRheaCacheProxy();
    }

    @Bean
    public CoreCacheProxy coreCacheProxy() {
        return new RemoteCoreCacheProxy();
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RheaWeb.class);
        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.setBannerMode(Mode.OFF);
        app.run(args);
    }
}
