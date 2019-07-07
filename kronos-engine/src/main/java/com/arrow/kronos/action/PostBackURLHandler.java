package com.arrow.kronos.action;

import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.arrow.acs.AcsSystemException;
import com.arrow.acs.JsonUtils;
import com.arrow.kronos.DeviceActionTypeConstants.PostBackURL;
import com.arrow.kronos.DeviceActionTypeConstants.PostBackURL.ContentType;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceEvent;
import com.arrow.kronos.data.TelemetryItem;

public class PostBackURLHandler extends ActionHandlerAbstract {

    private static final ContentType DEFAULT_CONTENT_TYPE = ContentType.APPLICATION_FORM_URLENCODED;
    private static final String PAYLOAD = "{payload}";

    private RestTemplate restTemplate;

    public PostBackURLHandler() {
        super();
        restTemplate = new RestTemplate(requestFactory());
        restTemplate.setMessageConverters(messageConverters());
    }

    @Override
    public void handle(DeviceEvent deviceEvent, TelemetryWrapper wrapper, Device device, DeviceAction action) {
        String method = "handle";

        String url = action.getParameters().get(PostBackURL.PARAMETER_URL);
        Assert.hasText(url, "url is not defined in action");

        HttpHeaders httpHeaders = new HttpHeaders();

        ContentType contentType = DEFAULT_CONTENT_TYPE;
        String contentTypeValue = action.getParameters().get(PostBackURL.PARAMETER_CONTENT_TYPE);
        if (StringUtils.hasText(contentTypeValue)) {
            contentType = ContentType.fromValue(contentTypeValue);
        }
        MediaType mediaType = MediaType.valueOf(contentType.getValue());
        httpHeaders.setContentType(mediaType);

        String headers = action.getParameters().get(PostBackURL.PARAMETER_HEADERS);
        populateHeaders(httpHeaders, headers);

        Map<String, Object> payload = buildModel(wrapper, device, action);

        HttpEntity<?> request = null;
        String requestBody = action.getParameters().get(PostBackURL.PARAMETER_REQUEST_BODY);
        switch (contentType) {
        case APPLICATION_FORM_URLENCODED: {
            if (StringUtils.hasText(requestBody)) {
                MultiValueMap<String, String> model = new LinkedMultiValueMap<String, String>();
                populateRequestBody(model, requestBody, payload);
                logDebug(method, "%s", model);
                request = new HttpEntity<>(model, httpHeaders);
            } else {
                request = new HttpEntity<>(JsonUtils.toJson(payload), httpHeaders);
            }
            break;
        }
        case APPLICATION_JSON: {
            String body;
            if (StringUtils.hasText(requestBody)) {
                body = requestBody.replace(PAYLOAD, JsonUtils.toJson(payload));
            } else {
                body = JsonUtils.toJson(payload);
            }
            request = new HttpEntity<>(body, httpHeaders);
            break;
        }
        case APPLICATION_XML: {
            String body = "<![CDATA[" + JsonUtils.toJson(payload).replace("]]>", "]]]]><![CDATA[>") + "]]>";
            if (StringUtils.hasText(requestBody)) {
                body = requestBody.replace(PAYLOAD, body);
            }
            request = new HttpEntity<>(body, httpHeaders);
            break;
        }
        }

        logInfo(method, "post event %s to: %s", request, url);
        String response = restTemplate.postForObject(url, request, String.class);
        logInfo(method, "response: %s", response);

        deviceEvent.addInformation(PostBackURL.PARAMETER_REQUEST_BODY, requestBody);
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

    private Map<String, Object> buildModel(TelemetryWrapper wrapper, Device device, DeviceAction action) {
        Map<String, Object> model = new HashMap<>();
        model.put("criteria", action.getCriteria());
        model.put("applicationId", device.getApplicationId());
        model.put("deviceId", wrapper.getDeviceId());
        model.put("deviceName", device.getName());
        model.put("deviceUid", device.getUid());
        if (device.getRefDeviceType() != null) {
            model.put("deviceTypeName", device.getRefDeviceType().getName());
        }
        model.put("timestamp", Instant.ofEpochMilli(wrapper.getTimestamp()).toString());
        Map<String, Object> telemetryItems = new HashMap<>();
        if (wrapper.getItems() != null) {
            for (TelemetryItem item : wrapper.getItems()) {
                telemetryItems.put(item.getName(), item.value());
            }
        }
        model.put("telemetry", telemetryItems);
        return model;
    }

    private HttpHeaders populateHeaders(HttpHeaders httpHeaders, String headers) {
        String method = "populateHeaders";
        if (httpHeaders == null) {
            httpHeaders = new HttpHeaders();
        }
        if (StringUtils.hasText(headers)) {
            Pattern headersPattern = Pattern.compile("(('\\S+'|\"\\S+\")\\s*:\\s*('(.*?)'|\"(.*?)\"))(\\s*,\\s*)?");
            Pattern headerPattern = Pattern.compile("['\"](\\S+)['\"]\\s*:\\s*['\"](.*?)['\"]");
            Matcher headersMatcher = headersPattern.matcher(headers);
            while (headersMatcher.find()) {
                String header = headersMatcher.group(1);
                Matcher headerMatcher = headerPattern.matcher(header);
                if (headerMatcher.matches()) {
                    String headerName = headerMatcher.group(1);
                    String headerValue = headerMatcher.group(2);
                    logDebug(method, "headerName=%s headerValue=%s", headerName, headerValue);
                    if (StringUtils.hasText(headerName) && StringUtils.hasText(headerValue)) {
                        httpHeaders.add(StringUtils.trimWhitespace(headerName),
                                StringUtils.trimWhitespace(headerValue));
                    }
                }
            }
        }
        return httpHeaders;
    }

    private List<HttpMessageConverter<?>> messageConverters() {
        List<HttpMessageConverter<?>> result = new ArrayList<>();
        result.add(new FormHttpMessageConverter());
        result.add(new StringHttpMessageConverter());
        result.add(new MappingJackson2HttpMessageConverter());
        return result;
    }

    private MultiValueMap<String, String> populateRequestBody(final MultiValueMap<String, String> body, String value,
            Object payload) {
        String method = "populateRequestBody";
        try {
            Pattern pairPattern = Pattern.compile("(\\S+)\\s*=\\s*(.*?)");
            String[] pairs = value.split("\\s*&\\s*");
            Arrays.stream(pairs).forEach(p -> {
                Matcher pairMatcher = pairPattern.matcher(p);
                if (pairMatcher.matches()) {
                    String paramName = pairMatcher.group(1);
                    String paramValue = pairMatcher.group(2);
                    if (paramValue != null && paramValue.equals(PAYLOAD)) {
                        paramValue = JsonUtils.toJson(payload);
                    }
                    body.add(paramName, paramValue);
                }
            });
        } catch (Exception e) {
            logError(method, e);
        }
        return body;
    }
}
