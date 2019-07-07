package com.arrow.pegasus.security;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.saml2.metadata.provider.AbstractReloadingMetadataProvider;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.saml2.metadata.provider.ResourceBackedMetadataProvider;
import org.opensaml.util.resource.ClasspathResource;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLBootstrap;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.SAMLRelayStateSuccessHandler;
import org.springframework.security.saml.SAMLWebSSOHoKProcessingFilter;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.context.SAMLContextProviderImpl;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.metadata.CachingMetadataManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.parser.ParserPoolHolder;
import org.springframework.security.saml.processor.HTTPArtifactBinding;
import org.springframework.security.saml.processor.HTTPPAOS11Binding;
import org.springframework.security.saml.processor.HTTPPostBinding;
import org.springframework.security.saml.processor.HTTPRedirectDeflateBinding;
import org.springframework.security.saml.processor.HTTPSOAP11Binding;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessor;
import org.springframework.security.saml.processor.SAMLProcessorImpl;
import org.springframework.security.saml.trust.httpclient.TLSProtocolConfigurer;
import org.springframework.security.saml.trust.httpclient.TLSProtocolSocketFactory;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.ArtifactResolutionProfile;
import org.springframework.security.saml.websso.ArtifactResolutionProfileImpl;
import org.springframework.security.saml.websso.SingleLogoutProfile;
import org.springframework.security.saml.websso.SingleLogoutProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfile;
import org.springframework.security.saml.websso.WebSSOProfileConsumer;
import org.springframework.security.saml.websso.WebSSOProfileConsumerHoKImpl;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;
import org.springframework.security.saml.websso.WebSSOProfileECPImpl;
import org.springframework.security.saml.websso.WebSSOProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfileOptions;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public abstract class SamlWebSecurityAbstract extends CoreWebSecurityAbstract {

	@Autowired
	private SamlUserDetailsService samlUserDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.authenticationProvider(samlAuthenticationProvider());
	}

	@Bean
	public SamlProperties samlProperties() {
		return new SamlProperties();
	}

	@Bean
	public SAMLAuthenticationProvider samlAuthenticationProvider() {
		SAMLAuthenticationProvider samlAuthenticationProvider = new SAMLAuthenticationProvider();
		samlAuthenticationProvider.setUserDetails(samlUserDetailsService);
		samlAuthenticationProvider.setForcePrincipalAsString(false);
		return samlAuthenticationProvider;
	}

	// XML parser pool needed for OpenSAML parsing
	@Bean(initMethod = "initialize")
	public StaticBasicParserPool parserPool() {
		return new StaticBasicParserPool();
	}

	public ParserPoolHolder parserPoolHolder() {
		return new ParserPoolHolder();
	}

	// Provider of default SAML Context
	@Bean
	public SAMLContextProvider contextProvider() throws MetadataProviderException {
		return new SAMLContextProviderImpl();
	}

	// Initialization of OpenSAML library
	@Bean
	public static SAMLBootstrap samlBootstrap() {
		return new SAMLBootstrap();
	}

	// Logger for SAML messages and events
	@Bean
	public SAMLDefaultLogger samlLogger() {
		return new SAMLDefaultLogger();
	}

	// SAML 2.0 WebSSO Assertion Consumer
	@Bean
	public WebSSOProfileConsumer webSSOprofileConsumer() {
		return new WebSSOProfileConsumerImpl();
	}

	// SAML 2.0 Holder-of-Key WebSSO Assertion Consumer
	@Bean
	public WebSSOProfileConsumerHoKImpl hokWebSSOprofileConsumer() {
		return new WebSSOProfileConsumerHoKImpl();
	}

	// SAML 2.0 Web SSO profile
	@Bean
	public WebSSOProfile webSSOprofile() {
		return new WebSSOProfileImpl();
	}

	// SAML 2.0 Holder-of-Key Web SSO profile
	@Bean
	public WebSSOProfileConsumerHoKImpl hokWebSSOProfile() {
		return new WebSSOProfileConsumerHoKImpl();
	}

	// SAML 2.0 ECP profile
	@Bean
	public WebSSOProfileECPImpl ecpprofile() {
		return new WebSSOProfileECPImpl();
	}

	@Bean
	public ExtendedMetadata extendedMetadata() {
		ExtendedMetadata extendedMetadata = new ExtendedMetadata();
		extendedMetadata.setIdpDiscoveryEnabled(false);
		extendedMetadata.setSignMetadata(false);
		return extendedMetadata;
	}

	@Bean
	public MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager() {
		return new MultiThreadedHttpConnectionManager();
	}

	@Bean
	public HttpClient httpClient() {
		return new HttpClient(multiThreadedHttpConnectionManager());
	}

	@Bean
	public KeyManager keyManager() {
		DefaultResourceLoader loader = new DefaultResourceLoader();
		SamlProperties properties = samlProperties();
		Resource storeFile = loader.getResource(properties.getJksStore());
		String storePass = properties.getJksStorePassword();
		Map<String, String> passwords = new HashMap<>();
		for (String password : properties.getJksPasswords()) {
			String[] tokens = password.split(":");
			if (tokens.length == 2) {
				passwords.put(tokens[0].trim(), tokens[1].trim());
			}
		}
		String defaultKey = properties.getJksDefaultKey();
		return new JKSKeyManager(storeFile, storePass, passwords, defaultKey);
	}

	@Bean
	public MetadataManager metadataManager() throws MetadataProviderException {
		String method = "cachingMetadaManager";
		List<MetadataProvider> providers = new ArrayList<MetadataProvider>();
		for (String url : samlProperties().getIdpMetadataUrls()) {
			getLogger().logInfo(method, "processing: %s", url);
			try {
				Timer timer = new Timer(true);
				MetadataProvider provider = null;
				if (url.startsWith("http")) {
					provider = new HTTPMetadataProvider(timer, httpClient(), url);
				} else {
					// try classpath first
					try {
						getLogger().logInfo(method, "trying ClasspathResource: %s", url);
						ClasspathResource resource = new ClasspathResource(url);
						if (resource.exists()) {
							provider = new ResourceBackedMetadataProvider(timer, resource);
						}
					} catch (Exception e) {
						// try file system
						getLogger().logInfo(method, "trying fileProvider: %s", url);
						File file = new File(url);
						if (file.exists()) {
							provider = new FilesystemMetadataProvider(file);
						}
					}
				}
				if (provider != null) {
					((AbstractReloadingMetadataProvider) provider).setParserPool(parserPool());
					ExtendedMetadataDelegate extended = new ExtendedMetadataDelegate(provider, extendedMetadata());
					extended.setMetadataTrustCheck(false);
					extended.setMetadataRequireSignature(false);
					providers.add(provider);
					getLogger().logInfo(method, "added path: %s", url);
				} else {
					getLogger().logError(method, "Unable to derive MetadataProvider!");
				}
			} catch (Exception e) {
				getLogger().logError(method, e);
			}
		}
		CachingMetadataManager manager = new CachingMetadataManager(providers);
		manager.setRefreshRequired(false);
		manager.setRequireValidMetadata(false);
		return manager;
	}

	// Processor
	@Bean
	public SAMLProcessor samlProcessor() {
		Collection<SAMLBinding> bindings = new ArrayList<SAMLBinding>();
		bindings.add(httpRedirectDeflateBinding());
		bindings.add(httpPostBinding());
		bindings.add(artifactBinding());
		bindings.add(httpSOAP11Binding());
		bindings.add(httpPAOS11Binding());
		return new SAMLProcessorImpl(bindings);
	}

	// Bindings
	private ArtifactResolutionProfile artifactResolutionProfile() {
		final ArtifactResolutionProfileImpl artifactResolutionProfile = new ArtifactResolutionProfileImpl(httpClient());
		artifactResolutionProfile.setProcessor(new SAMLProcessorImpl(soapBinding()));
		return artifactResolutionProfile;
	}

	@Bean
	public HTTPArtifactBinding artifactBinding() {
		return new HTTPArtifactBinding(parserPool(), velocityEngine(), artifactResolutionProfile());
	}

	@Bean
	public VelocityEngine velocityEngine() {
		return VelocityFactory.getEngine();
	}

	@Bean
	public HTTPSOAP11Binding soapBinding() {
		return new HTTPSOAP11Binding(parserPool());
	}

	@Bean
	public HTTPPostBinding httpPostBinding() {
		return new HTTPPostBinding(parserPool(), velocityEngine());
	}

	@Bean
	public HTTPRedirectDeflateBinding httpRedirectDeflateBinding() {
		return new HTTPRedirectDeflateBinding(parserPool());
	}

	@Bean
	public HTTPSOAP11Binding httpSOAP11Binding() {
		return new HTTPSOAP11Binding(parserPool());
	}

	@Bean
	public HTTPPAOS11Binding httpPAOS11Binding() {
		return new HTTPPAOS11Binding(parserPool());
	}

	// Setup TLS Socket Factory
	@Bean
	public TLSProtocolConfigurer tlsProtocolConfigurer() {
		TLSProtocolConfigurer configurer = new TLSProtocolConfigurer();
		configurer.setSslHostnameVerification(getSslHostnameVerification());
		return configurer;
	}

	@Bean
	public ProtocolSocketFactory socketFactory() {
		return new TLSProtocolSocketFactory(keyManager(), null, getSslHostnameVerification());
	}

	@Bean
	public Protocol socketFactoryProtocol() {
		return new Protocol("https", socketFactory(), 443);
	}

	// The filter is waiting for connections on URL suffixed with filterSuffix
	// and presents SP metadata there
	@Bean
	public MetadataDisplayFilter metadataDisplayFilter() {
		return new MetadataDisplayFilter();
	}

	@Bean
	public MetadataGeneratorFilter metadataGeneratorFilter() throws MetadataProviderException {
		return new MetadataGeneratorFilter(metadataGenerator());
	}

	// Filter automatically generates default SP metadata
	@Bean
	public MetadataGenerator metadataGenerator() {
		MetadataGenerator metadataGenerator = new MetadataGenerator();
		metadataGenerator.setEntityId(samlProperties().getSpEntityId());
		metadataGenerator.setIncludeDiscoveryExtension(false);
		return metadataGenerator;
	}

	@Bean
	public WebSSOProfileOptions defaultWebSSOProfileOptions() {
		WebSSOProfileOptions webSSOProfileOptions = new WebSSOProfileOptions();
		webSSOProfileOptions.setIncludeScoping(false);
		return webSSOProfileOptions;
	}

	@Bean
	public SAMLEntryPoint samlEntryPoint() {
		SAMLEntryPoint samlEntryPoint = new SAMLEntryPoint();
		samlEntryPoint.setDefaultProfileOptions(defaultWebSSOProfileOptions());
		return samlEntryPoint;
	}

	@Bean
	public SecurityContextLogoutHandler logoutHandler() {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.setInvalidateHttpSession(true);
		logoutHandler.setClearAuthentication(true);
		return logoutHandler;
	}

	@Bean
	public SimpleUrlLogoutSuccessHandler successLogoutHandler() {
		SimpleUrlLogoutSuccessHandler successLogoutHandler = new SimpleUrlLogoutSuccessHandler();
		successLogoutHandler.setDefaultTargetUrl(getSamlSuccessLogoutUrl());
		return successLogoutHandler;
	}

	// Filter processing incoming logout messages
	// First argument determines URL user will be redirected to after successful
	// global logout
	@Bean
	public SAMLLogoutProcessingFilter samlLogoutProcessingFilter() {
		return new SAMLLogoutProcessingFilter(successLogoutHandler(), logoutHandler());
	}

	// Overrides default logout processing filter with the one processing SAML
	// messages
	@Bean
	public SAMLLogoutFilter samlLogoutFilter() {
		return new SAMLLogoutFilter(successLogoutHandler(), new LogoutHandler[] { logoutHandler() },
		        new LogoutHandler[] { logoutHandler() });
	}

	@Bean
	public SAMLRelayStateSuccessHandler successRedirectHandler() {
		SAMLRelayStateSuccessHandler handler = new SAMLRelayStateSuccessHandler();
		handler.setAlwaysUseDefaultTargetUrl(false);
		return handler;
	}

	// Handler deciding where to redirect user after failed login
	@Bean
	public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
		SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
		failureHandler.setUseForward(true);
		failureHandler.setDefaultFailureUrl(getSamlAuthenticationFailureUrl());
		return failureHandler;
	}

	@Bean
	public SAMLProcessingFilter samlWebSSOProcessingFilter() throws Exception {
		SAMLProcessingFilter samlWebSSOProcessingFilter = new SAMLProcessingFilter();
		samlWebSSOProcessingFilter.setAuthenticationManager(authenticationManager());
		samlWebSSOProcessingFilter.setAuthenticationSuccessHandler(successRedirectHandler());
		samlWebSSOProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
		return samlWebSSOProcessingFilter;
	}

	@Bean
	public SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter() throws Exception {
		SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter = new SAMLWebSSOHoKProcessingFilter();
		samlWebSSOHoKProcessingFilter.setAuthenticationSuccessHandler(successRedirectHandler());
		samlWebSSOHoKProcessingFilter.setAuthenticationManager(authenticationManager());
		samlWebSSOHoKProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
		return samlWebSSOHoKProcessingFilter;
	}

	@Bean
	public SingleLogoutProfile logoutprofile() {
		return new SingleLogoutProfileImpl();
	}

	@Bean
	public FilterChainProxy samlFilter() throws Exception {
		List<SecurityFilterChain> chains = new ArrayList<SecurityFilterChain>();
		// chains.add(new DefaultSecurityFilterChain(new
		// AntPathRequestMatcher("/saml/login/**"), samlEntryPoint()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/logout/**"), samlLogoutFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/metadata/**"),
		        metadataDisplayFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSO/**"),
		        samlWebSSOProcessingFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSOHoK/**"),
		        samlWebSSOHoKProcessingFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SingleLogout/**"),
		        samlLogoutProcessingFilter()));
		return new FilterChainProxy(chains);
	}

	protected String getSslHostnameVerification() {
		return "allowAll";
	}

	protected abstract String getSamlAuthenticationFailureUrl();

	protected abstract String getSamlSuccessLogoutUrl();
}
