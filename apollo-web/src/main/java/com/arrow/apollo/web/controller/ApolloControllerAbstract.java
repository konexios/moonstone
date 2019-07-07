package com.arrow.apollo.web.controller;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.util.Assert;

import com.arrow.apollo.web.util.ApolloEmailSender;
import com.arrow.apollo.web.util.ApolloSmtpProperties;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.client.api.ClientRoleApi;
import com.arrow.pegasus.client.api.ClientUserApi;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.util.CoreConfigurationPropertyUtil;
import com.arrow.pegasus.webapi.WebApiAbstract;

public abstract class ApolloControllerAbstract extends WebApiAbstract {
	@Autowired
	private ClientUserApi clientUserApi;
	@Autowired
	private ClientRoleApi clientRoleApi;
	@Autowired
	private CoreConfigurationPropertyUtil coreConfigurationPropertyUtil;

	public ClientUserApi getClientUserApi() {
		return clientUserApi;
	}

	public ClientRoleApi getClientRoleApi() {
		return clientRoleApi;
	}

	public CoreConfigurationPropertyUtil getCoreConfigurationPropertyUtil() {
		return coreConfigurationPropertyUtil;
	}

	@Bean
	public ApolloEmailSender getApolloEmailSender() {
		return new ApolloEmailSender();
	}

	@Bean
	public VelocityEngine getVelocityEngine() {
		return VelocityFactory.getEngine();
	}

	@Bean
	public ApolloSmtpProperties getApolloSmtpProperties() {
		return new ApolloSmtpProperties();
	}

	protected Product getApolloProduct() {
		Product product = getCoreCacheService().findProductBySystemName(ProductSystemNames.APOLLO);
		Assert.notNull(product, "Product not found! name: " + ProductSystemNames.APOLLO);

		return product;
	}

	protected String getApolloProductId() {
		return getApolloProduct().getId();
	}
}
