package com.arrow.pegasus;

import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

@Configuration
public class SwaggerConfiguration extends SwaggerConfigurationAbstract {

	@Override
	protected ApiInfo metadata() {
		return new ApiInfoBuilder().title("Pegasus Local API").description("Pegasus Local API documentation")
		        .version("1.0").contact(new Contact("Tam Nguyen", "", "tnguyen@arrowsi.com")).build();
	}
}