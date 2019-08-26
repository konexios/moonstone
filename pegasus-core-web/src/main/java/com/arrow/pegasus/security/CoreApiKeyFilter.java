package com.arrow.pegasus.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.service.CoreCacheHelper;
import com.arrow.pegasus.service.CoreCacheService;
import com.arrow.pegasus.service.CryptoService;

import moonstone.acs.AcsUtils;
import moonstone.acs.ApiHeaders;
import moonstone.acs.ApiRequestSigner;
import moonstone.acs.Loggable;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class CoreApiKeyFilter extends OncePerRequestFilter {

	private static Loggable LOG = new Loggable() {
	};

	@Autowired
	private CoreCacheService coreCacheService;
	@Autowired
	private CoreCacheHelper coreCacheHelper;
	@Autowired
	private CryptoService cryptoService;

	private final List<Pattern> exceptionPatterns;
	private final boolean secured;

	public CoreApiKeyFilter(boolean secured, List<String> exceptionPaths) {
		String method = getClass().getSimpleName();
		this.secured = secured;
		exceptionPatterns = new ArrayList<>();
		if (exceptionPaths != null) {
			for (String path : exceptionPaths) {
				try {
					LOG.logDebug(method, "adding pattern %s", path);
					exceptionPatterns.add(Pattern.compile(path));
				} catch (Exception e) {
					LOG.logError(method, e);
				}
			}
		}
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String method = "doFilter";

		if (RequestMethod.valueOf(request.getMethod()) == RequestMethod.OPTIONS) {
			// allow OPTIONS requests for CORS pre-flight
			completeDoFilter(request, response, chain, true, null);
			return;
		}

		// check if exception path
		for (Pattern pattern : exceptionPatterns) {
			if (pattern.matcher(request.getRequestURI()).matches()) {
				LOG.logDebug(method, "found match of exception pattern: %s", pattern.pattern());
				completeDoFilter(request, response, chain, true, null);
				return;
			}
		}

		LOG.logInfo(method, "method: %s, uri: %s", request.getMethod(), request.getRequestURI());

		String error = "";
		boolean authorized = false;
		String payload = "";
		AccessKey accessKey = null;
		if (secured) {

			boolean checkHmac = true;
			String apiKey = request.getHeader(ApiHeaders.X_ARROW_APIKEY);

			if (StringUtils.isEmpty(apiKey)) {
				apiKey = request.getHeader(ApiHeaders.X_AUTH_TOKEN);
				checkHmac = false;
			}

			LOG.logDebug(method, "apiKey: %s", apiKey);

			if (checkHmac && LOG.isDebugEnabled()) {
				debugRequest(request);
			}

			if (StringUtils.isBlank(apiKey)) {
				error = "missing apiKey";
				LOG.logWarn(method, error);
				completeDoFilter(request, response, chain, false, error);
				return;
			}

			accessKey = coreCacheHelper.populateAccessKey(
					coreCacheService.findAccessKeyByHashedApiKey(cryptoService.getCrypto().internalHash(apiKey)));
			if (accessKey == null) {
				error = "invalid apiKey";
				LOG.logWarn(method, error);
				completeDoFilter(request, response, chain, false, error);
				return;
			}

			if (!accessKey.getExpiration().isAfter(Instant.now())) {
				error = "apiKey is disabled";
				LOG.logWarn(method, error);
				completeDoFilter(request, response, chain, false, error);
				return;
			}

			Company company = coreCacheHelper.populateCompany(accessKey.getRefCompany());
			if (company == null) {
				error = "company not found " + accessKey.getCompanyId();
				LOG.logWarn(method, error);
				completeDoFilter(request, response, chain, false, error);
				return;
			} else if (company.getStatus() != CompanyStatus.Active) {
				error = String.format("company not active: %s / %s", company.getId(), company.getName());
				LOG.logWarn(method, error);
				completeDoFilter(request, response, chain, false, error);
				return;
			}

			Application application = coreCacheHelper.populateApplication(accessKey.getRefApplication());
			if (application != null && !application.isEnabled()) {
				error = "application is disabled";
				LOG.logWarn(method, error);
				completeDoFilter(request, response, chain, false, error);
				return;
			}

			LOG.logDebug(method, "found accessKey: %s / %s / %s", company.getName(),
					(application == null ? "" : application.getName()), accessKey.getExpiration());

			boolean apiSigningRequired = application != null && application.checkApiSigningRequired();
			if (apiSigningRequired && !checkHmac) {
				error = "apiSigningRequired is TRUE but HMAC is not provided";
				LOG.logWarn(method, error);
				completeDoFilter(request, response, chain, false, error);
				return;
			}

			if (apiSigningRequired) {
				try {
					// verify date
					String dateStr = request.getHeader(ApiHeaders.X_ARROW_DATE);
					ZonedDateTime local = ZonedDateTime.now();
					ZonedDateTime date = ZonedDateTime.parse(dateStr);
					long seconds = Duration.between(local, date).abs().getSeconds();
					LOG.logDebug(method, "seconds: %d", seconds);

					ApiRequestSigner builder = ApiRequestSigner
							.create(cryptoService.decrypt(application.getId(), accessKey.getEncryptedSecretKey()))
							.method(request.getMethod()).canonicalUri(request.getRequestURI()).apiKey(apiKey)
							.timestamp(dateStr);

					// add query parameters
					Enumeration<String> enu = request.getParameterNames();
					while (enu.hasMoreElements()) {
						String name = enu.nextElement();
						LOG.logDebug(method, "---> adding parameter: %s", name);
						String[] values = request.getParameterValues(name);
						for (String value : values) {
							builder.parameter(name, value);
						}
					}

					payload = AcsUtils.streamToString(request.getInputStream(), StandardCharsets.UTF_8);
					if (StringUtils.isNotEmpty(payload)) {
						builder.payload(payload);
					}

					LOG.logDebug(method, "payload: %s", payload);
					String verifying = null;
					String version = request.getHeader(ApiHeaders.X_ARROW_VERSION);
					if (StringUtils.isEmpty(version)) {
						// backward compatible
						verifying = builder.signV0();
					} else if (version.equals(ApiHeaders.X_ARROW_VERSION_1)) {
						verifying = builder.signV1();
					} else if (version.equals(ApiHeaders.X_ARROW_VERSION_2)) {
						verifying = builder.signV2();
					} else {
						LOG.logError(method, "version not supported: %s", version);
					}
					LOG.logDebug(method, "verifying: %s", verifying);

					String signature = request.getHeader(ApiHeaders.X_ARROW_SIGNATURE);
					LOG.logDebug(method, "signature: %s", signature);

					authorized = seconds < 600 && StringUtils.equals(verifying, signature);
					LOG.logDebug(method, "---> signing request result: %s", authorized);
				} catch (Throwable t) {
					error = t.getMessage();
					LOG.logError(method, "error checking signature", t);
				}
			} else {
				payload = AcsUtils.streamToString(request.getInputStream(), StandardCharsets.UTF_8);
				authorized = true;
			}
		} else {
			payload = AcsUtils.streamToString(request.getInputStream(), StandardCharsets.UTF_8);
			authorized = true;
		}

		if (authorized) {
			RequestContextHolder.currentRequestAttributes().setAttribute(CoreConstant.API_PAYLOAD, payload,
					RequestAttributes.SCOPE_REQUEST);
			if (accessKey != null) {
				RequestContextHolder.currentRequestAttributes().setAttribute(CoreConstant.API_KEY_CONTEXT, accessKey,
						RequestAttributes.SCOPE_REQUEST);
				SecurityContextHolder.getContext().setAuthentication(
						new UsernamePasswordAuthenticationToken(accessKey, accessKey, Collections.emptyList()));
			}
		} else {
			error = "Not Authorized";
		}
		completeDoFilter(request, response, chain, authorized, error);
	}

	private void completeDoFilter(ServletRequest request, ServletResponse response, FilterChain chain,
			boolean authorized, String error) throws IOException, ServletException {
		String method = "completeDoFilter";
		if (!authorized) {
			LOG.logWarn(method, "not authorized");
			((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(), error);
		} else {
			chain.doFilter(request, response);
		}
	}

	private void debugRequest(HttpServletRequest request) {
		String method = "debugRequest";
		StringBuffer sb = new StringBuffer("\n");
		sb.append(request.getMethod() + " " + request.getRequestURI() + "\n");
		sb.append("queryString: " + request.getQueryString() + "\n");
		List<String> list = Collections.list(request.getHeaderNames());
		Collections.sort(list);
		for (String name : list) {
			sb.append(name + ": " + request.getHeader(name) + "\n");
		}
		LOG.logDebug(method, sb.toString());
	}
}
