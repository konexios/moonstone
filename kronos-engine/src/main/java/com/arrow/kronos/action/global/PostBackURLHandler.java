package com.arrow.kronos.action.global;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.arrow.acs.AcsSystemException;
import com.arrow.kronos.GlobalActionTypeConstants;
import com.arrow.kronos.GlobalActionTypeConstants.PostBackURL;
import com.arrow.kronos.GlobalActionTypeConstants.PostBackURL.ContentType;
import com.arrow.kronos.data.action.GlobalAction;
import com.arrow.kronos.data.action.GlobalActionProperty;
import com.arrow.kronos.data.action.ParameterValidation.ValidationType;

public class PostBackURLHandler extends GlobalActionHandlerAbstract {

	private static final Pattern MULTILINE_HEADER_PATTERN = Pattern.compile("\"?(.*?)\"?\\s*:\\s*\"?(.*?)\"?");

	@Override
	public void handle(DataMessageModel data, GlobalAction globalAction) {
		String method = "handle";
		logDebug(method, "...");

		logDebug(method, "clear optional not provided inputs");
		List<GlobalActionProperty> actionProperties = clearOptionalInputsIfNotProvided(globalAction, data.getPayload());

		// process URL
		String url = processUrl(data, actionProperties);

		// process Headers
		HttpHeaders httpHeaders = processHeaders(data, actionProperties);

		// process Content Type
		ContentType contentType = processContentTypeValue(data, actionProperties);
		MediaType mediaType = MediaType.valueOf(contentType.getValue());
		httpHeaders.setContentType(mediaType);

		// process Request Body
		HttpEntity<?> request = null;
		switch (contentType) {
		case APPLICATION_FORM_URLENCODED: {
			MultiValueMap<String, String> model = processRequestBodyUrlencoded(data, actionProperties);
			request = new HttpEntity<>(model, httpHeaders);
			break;
		}
		case APPLICATION_JSON: {
			String requestBody = processRequestBody(data, actionProperties);
			request = new HttpEntity<>(requestBody, httpHeaders);
			break;
		}
		case APPLICATION_XML: {
			String requestBody = processRequestBodyXml(data, actionProperties);
			request = new HttpEntity<>(requestBody, httpHeaders);
			break;
		}
		}

		RestTemplate restTemplate = new RestTemplate(requestFactory());
		restTemplate.setMessageConverters(messageConverters());

		logInfo(method, "post event %s to: %s", request, url);
		String response = restTemplate.postForObject(url, request, String.class);
		logInfo(method, "response: %s", response);
	}

	private List<HttpMessageConverter<?>> messageConverters() {
		List<HttpMessageConverter<?>> result = new ArrayList<>();
		result.add(new FormHttpMessageConverter());
		result.add(new StringHttpMessageConverter());
		result.add(new MappingJackson2HttpMessageConverter());
		return result;
	}

	private ClientHttpRequestFactory requestFactory() {
		try {
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			SSLContext sslContext = SSLContexts.custom()
			        .loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> true).build();
			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
			requestFactory.setHttpClient(httpClient);
			return requestFactory;
		} catch (Exception e) {
			throw new AcsSystemException("error creating ClientHttpRequestFactory", e);
		}
	}

	private String processUrl(DataMessageModel data, List<GlobalActionProperty> actionProperties) {
		String method = "processUrl";
		GlobalActionProperty urlProperty = GlobalActionUtils.getProperty(actionProperties,
		        GlobalActionTypeConstants.PostBackURL.PARAMETER_URL);
		logDebug(method,
		        "url property value: " + urlProperty.getParameterValue() + " [" + urlProperty.getParameterType() + "]");
		String url = replaceVariables(urlProperty, data.getPayload());
		logDebug(method, "url: %s", url);
		return url;
	}

	private HttpHeaders processHeaders(DataMessageModel data, List<GlobalActionProperty> actionProperties) {
		String method = "processHeaders";
		HttpHeaders httpHeaders = new HttpHeaders();
		GlobalActionProperty headersProperty = GlobalActionUtils.getProperty(actionProperties,
		        GlobalActionTypeConstants.PostBackURL.PARAMETER_HEADERS);
		logDebug(method, "headers property value: " + headersProperty.getParameterValue() + " ["
		        + headersProperty.getParameterType() + "]");
		String headers = headersProperty.getParameterValue();

		ValidationType parameterType = ValidationType.valueOf(headersProperty.getParameterType());
		switch (parameterType) {
		case KEY_VALUE_PAIRS:
			GlobalActionUtils.parseKeyValuePairs(headers, (headerName, headerValue) -> {
				httpHeaders.add(headerName, replaceVariables(headerValue, parameterType, data.getPayload()));
			});
			break;
		case MULTILINE_STRING:
			GlobalActionUtils.parseMultilineString(headers, (header) -> {
				Matcher matcher = MULTILINE_HEADER_PATTERN.matcher(header);
				if (matcher.matches()) {
					httpHeaders.add(matcher.group(1),
					        replaceVariables(matcher.group(2), parameterType, data.getPayload()));
				} else {
					logWarn(method, "Unexpected value format %s", header);
				}
			});
			break;
		default:
			logWarn(method, "Unsupported parameter type: %s, ignore headers", parameterType);
			break;
		}
		return httpHeaders;
	}

	private String processRequestBody(DataMessageModel data, List<GlobalActionProperty> actionProperties) {
		String method = "processRequestBody";
		GlobalActionProperty requestBodyProperty = GlobalActionUtils.getProperty(actionProperties,
		        PostBackURL.PARAMETER_REQUEST_BODY);
		logDebug(method, "request body property value: " + requestBodyProperty.getParameterValue() + " ["
		        + requestBodyProperty.getParameterType() + "]");
		String requestBody = replaceVariables(requestBodyProperty, data.getPayload());
		logDebug(method, "requestBody: %s", requestBody);
		return requestBody;
	}

	private String processRequestBodyXml(DataMessageModel data, List<GlobalActionProperty> actionProperties) {
		String method = "processRequestBodyXml";
		GlobalActionProperty requestBodyProperty = GlobalActionUtils.getProperty(actionProperties,
		        PostBackURL.PARAMETER_REQUEST_BODY);
		logDebug(method, "request body property value: " + requestBodyProperty.getParameterValue() + " ["
		        + requestBodyProperty.getParameterType() + "]");
		String requestBody = requestBodyProperty.getParameterValue();

		ValidationType parameterType = ValidationType.valueOf(requestBodyProperty.getParameterType());
		switch (parameterType) {
		case XML:
			requestBody = replaceVariables(requestBody, parameterType, data.getPayload(),
			        (s) -> "<![CDATA[" + s.replace("]]>", "]]]]><![CDATA[>") + "]]>");
			break;
		default:
			logWarn(method, "Unsupported parameterType %s", parameterType);
			break;
		}
		logDebug(method, "requestBody: %s", requestBody);
		return requestBody;
	}

	private MultiValueMap<String, String> processRequestBodyUrlencoded(DataMessageModel data,
	        List<GlobalActionProperty> actionProperties) {
		String method = "processRequestBodyUrlencoded";
		MultiValueMap<String, String> model = new LinkedMultiValueMap<String, String>();
		GlobalActionProperty requestBodyProperty = GlobalActionUtils.getProperty(actionProperties,
		        PostBackURL.PARAMETER_REQUEST_BODY);
		logDebug(method, "request body property value: " + requestBodyProperty.getParameterValue() + " ["
		        + requestBodyProperty.getParameterType() + "]");
		String requestBody = requestBodyProperty.getParameterValue();

		ValidationType parameterType = ValidationType.valueOf(requestBodyProperty.getParameterType());
		switch (parameterType) {
		case KEY_VALUE_PAIRS:
			GlobalActionUtils.parseKeyValuePairs(requestBody,
			        (key, value) -> model.add(key, replaceVariables(value, parameterType, data.getPayload())));
			break;
		default:
			break;
		}
		logDebug(method, "requestBody: %s", requestBody);
		return model;
	}

	private ContentType processContentTypeValue(DataMessageModel data, List<GlobalActionProperty> actionProperties) {
		String method = "processContentTypeValue";
		GlobalActionProperty contentTypeProperty = GlobalActionUtils.getProperty(actionProperties,
		        GlobalActionTypeConstants.PostBackURL.PARAMETER_CONTENT_TYPE);
		logDebug(method, "content type property value: " + contentTypeProperty.getParameterValue() + " ["
		        + contentTypeProperty.getParameterType() + "]");
		String contentTypeValue = replaceVariables(contentTypeProperty, data.getPayload());
		logDebug(method, "contentTypeValue: %s", contentTypeValue);
		ContentType contentType;
		if (StringUtils.hasText(contentTypeValue)) {
			contentType = ContentType.fromValue(contentTypeValue);
		} else {
			contentType = ContentType.APPLICATION_FORM_URLENCODED;
		}
		return contentType;
	}
}
