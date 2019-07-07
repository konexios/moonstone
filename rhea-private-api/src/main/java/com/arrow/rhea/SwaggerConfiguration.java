package com.arrow.rhea;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import com.arrow.acs.ApiHeaders;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

	@Bean
	public Docket documentation() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
		        .paths(PathSelectors.regex("/api/.*")).build().pathMapping("/").apiInfo(metadata())
		        .securitySchemes(Collections.singletonList(apiKey()))
		        .securityContexts(Collections.singletonList(securityContext()))
		        .globalOperationParameters(globalParameters()).produces(Collections.singleton("application/json"))
		        .consumes(Collections.singleton("application/json"))
		        .globalResponseMessage(RequestMethod.GET, defaultResponseMessages())
		        .globalResponseMessage(RequestMethod.POST, defaultResponseMessages())
		        .globalResponseMessage(RequestMethod.PUT, defaultResponseMessages());
	}

	private ApiInfo metadata() {
		return new ApiInfoBuilder().title("Rhea Local API").description("Rhea Local API documentation")
		        .version("1.0").contact(new Contact("Tam Nguyen", "", "tnguyen@arrowsi.com")).build();
	}

	private ApiKey apiKey() {
		return new ApiKey("apiKey", ApiHeaders.X_AUTH_TOKEN, "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/api/.*"))
		        .build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		new ParameterBuilder().build();
		return Collections.singletonList(new SecurityReference("apiKey", authorizationScopes));
	}

	private List<Parameter> globalParameters() {
		Parameter apiKeyHeader = new ParameterBuilder().name(ApiHeaders.X_AUTH_TOKEN).description("api key")
		        .modelRef(new ModelRef("string")).parameterType("header").required(true).build();
		return Collections.singletonList(apiKeyHeader);
	}

	private List<ResponseMessage> defaultResponseMessages() {
		return Arrays.asList(new ResponseMessageBuilder().code(200).message("OK").build(),
		        new ResponseMessageBuilder().code(400).message("Bad Request").build(),
		        new ResponseMessageBuilder().code(401).message("Unauthorized").build(),
		        new ResponseMessageBuilder().code(500).message("Internal Server Error").build());
	}
}