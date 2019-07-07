package com.arrow.kronos;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.acn.AcnEventNames;
import com.arrow.acn.MqttConstants;
import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.AcsUtils;
import com.arrow.acs.ApiHeaders;
import com.arrow.acs.GatewayPayloadSigner;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.api.ConnectionManager;
import com.arrow.acs.client.model.CloudRequestMethodName;
import com.arrow.acs.client.model.CloudRequestModel;
import com.arrow.acs.client.model.CloudRequestParameters;
import com.arrow.acs.client.model.CloudResponseModel;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.service.GatewayCommandService;
import com.arrow.pegasus.LifeCycleAbstract;
import com.arrow.pegasus.client.api.ClientAccessKeyApi;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventBuilder;
import com.arrow.pegasus.data.event.EventParameter;
import com.arrow.pegasus.data.event.EventStatus;
import com.arrow.pegasus.service.CoreCacheHelper;
import com.arrow.pegasus.service.EventService;

@Component
public class ApiRequestProcessor extends LifeCycleAbstract {

	private enum CloudResponseStatus {
		OK, ERROR
	}

	@Autowired
	private ConnectionFactory connectionFactory;
	@Autowired
	private EventService eventService;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private KronosEngineContext context;
	@Autowired
	private ClientAccessKeyApi clientAccessKeyApi;
	@Autowired
	private CoreCacheHelper coreCacheHelper;

	private RabbitAdmin rabbitAdmin;

	@Override
	protected void postConstruct() {
		super.postConstruct();
		String method = "postConstruct";

		rabbitAdmin = new RabbitAdmin(connectionFactory);
		logDebug(method, "declaring topic exchange %s", MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
		TopicExchange exchange = new TopicExchange(MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
		rabbitAdmin.declareExchange(exchange);
	}

	public void process(String gatewayHid, String baseUrl, CloudRequestModel request) throws Exception {
		String method = "process";
		logDebug(method, "...");

		// find gateway and make sure it is enabled
		Assert.hasText(gatewayHid, "gatewayHid is empty");
		Gateway gateway = context.getKronosCache().findGatewayByHid(gatewayHid);
		checkEnabled(gateway, "gateway");

		String who = gateway.getPri();

		// create event
		Event event = createEvent(request, gateway, who);

		// find gateway owner keys
		final AccessKey gatewayOwnerKey = clientAccessKeyApi.findOwnerKey(gateway.getPri());
		Assert.notNull(gatewayOwnerKey, "gatewayOwnerKey not found");
		logDebug(method, "%s %s", gateway.getPri(), gatewayOwnerKey);
		String apiKey = context.getCryptoService().decrypt(gatewayOwnerKey.getApplicationId(),
				gatewayOwnerKey.getEncryptedApiKey());
		String secretKey = context.getCryptoService().decrypt(gatewayOwnerKey.getApplicationId(),
				gatewayOwnerKey.getEncryptedSecretKey());

		CloudResponseModel response = null;
		try {
			validateCloudRequestModel(request);
			validateRequestSignature(request, apiKey, secretKey);
			if (request.isEncrypted()) {
				// TODO decrypt parameters
				throw new AcsLogicalException("Encrypted requests are not supported yet!");
			}
			response = sendApiRequest(baseUrl, request);
		} catch (Exception e) {
			logError(method, e);
			response = buildErrorResponse(request, e.getMessage());
		}
		if (response != null) {
			signResponse(response, apiKey, secretKey);
			event = updateEvent(event, response, who);
			logDebug(method, "response %s", JsonUtils.toJson(response));

			String routingKey = MqttConstants.serverToGatewayMqttApiRouting(gatewayHid);
			MessageBuilder messageBuilder = MessageBuilder.withBody(JsonUtils.toJsonBytes(response));
			messageBuilder.setExpiration(String.valueOf(GatewayCommandService.DEFAULT_MESSAGE_EXPIRATION));
			rabbitTemplate.convertAndSend(MqttConstants.DEFAULT_RABBITMQ_EXCHANGE, routingKey, messageBuilder.build());

			if (isDebugEnabled()) {
				logDebug(method, "sent to routing: %s, json: %s, messageExpiration: %s", routingKey,
						JsonUtils.toJson(response), GatewayCommandService.DEFAULT_MESSAGE_EXPIRATION);
				logDebug(method, "gatewayHid: %s", gateway.getHid());
			}
		}
	}

	private Event createEvent(CloudRequestModel request, Gateway gateway, String who) {
		Assert.notNull(request, "request is null");
		Assert.notNull(gateway, "gateway is null");
		Assert.hasText(who, "who is empty");
		EventBuilder eventBuilder = EventBuilder.create().name(request.getEventName())
				.applicationId(gateway.getApplicationId())
				.parameter(EventParameter.InString("gatewayHid", gateway.getHid()))
				.parameter(EventParameter.InString("requestId", request.getRequestId()))
				.parameter(EventParameter.InBoolean("encrypted", request.isEncrypted()));
		if (request.getParameters() != null) {
			request.getParameters()
					.forEach((name, value) -> eventBuilder.parameter(EventParameter.InString(name, value)));
		}
		if (StringUtils.isNotBlank(request.getSignature())) {
			eventBuilder.parameter(EventParameter.InString("signature", request.getSignature()));
		}
		if (StringUtils.isNotBlank(request.getSignatureVersion())) {
			eventBuilder.parameter(EventParameter.InString("signatureVersion", request.getSignatureVersion()));
		}
		Event event = eventBuilder.build();
		event.setStatus(EventStatus.Received);
		event = eventService.getEventRespository().doInsert(event, who);
		return event;
	}

	private Event updateEvent(Event event, CloudResponseModel response, String who) {
		if (event != null && event.getParameters() != null && response.getParameters() != null) {
			response.getParameters()
					.forEach((name, value) -> event.getParameters().add(EventParameter.OutString(name, value)));
			if (StringUtils.equals(response.getParameters().get(CloudResponseModel.STATUS_PARAMETER_NAME),
					CloudResponseStatus.OK.name())) {
				event.setStatus(EventStatus.Succeeded);
			} else {
				event.setStatus(EventStatus.Failed);
			}
			eventService.getEventRespository().doSave(event, who);
		}
		return event;
	}

	private void validateCloudRequestModel(CloudRequestModel request) {
		Assert.notNull(request, "request is null");
		if (!StringUtils.equals(request.getEventName(), AcnEventNames.GatewayToServer.API_REQUEST)) {
			throw new AcsLogicalException("Unsupported event name " + request.getEventName());
		}
		if (request.getParameters() == null) {
			throw new AcsLogicalException("Invalid request parameters");
		}
		if (StringUtils.isBlank(request.getParameters().get(CloudRequestParameters.METHOD_PARAMETER_NAME))) {
			throw new AcsLogicalException("Parameter required: method");
		}
		if (StringUtils.isBlank(request.getParameters().get(CloudRequestParameters.URI_PARAMETER_NAME))) {
			throw new AcsLogicalException("Parameter required: uri");
		}
		if (StringUtils.isNotBlank(request.getSignature())) {
			if (StringUtils.isBlank(request.getSignatureVersion())) {
				throw new AcsLogicalException("signatureVersion is required");
			}
		}
	}

	private void validateRequestSignature(CloudRequestModel request, String apiKey, String secretKey) {
		Assert.notNull(request, "request is null");
		String method = "validateRequestSignature";
		logDebug(method, "...");

		if (StringUtils.isEmpty(request.getSignature())) {
			logDebug(method, "signature is empty!");
			return;
		}
		GatewayPayloadSigner signer = GatewayPayloadSigner.create(secretKey).withApiKey(apiKey)
				.withHid(request.getRequestId()).withName(request.getEventName()).withEncrypted(request.isEncrypted());
		if (request.getParameters() != null) {
			request.getParameters().forEach((name, value) -> signer.withParameter(name, value));
		}
		String signature = null;
		switch (request.getSignatureVersion()) {
		case GatewayPayloadSigner.PAYLOAD_SIGNATURE_VERSION_1:
			signature = signer.signV1();
			break;
		default:
			throw new AcsLogicalException("signatureVersion " + request.getSignatureVersion() + " not supported");
		}
		if (!StringUtils.equals(signature, request.getSignature())) {
			throw new AcsLogicalException("invalid signature");
		}
	}

	private CloudResponseModel signResponse(CloudResponseModel response, String apiKey, String secretKey) {
		Assert.notNull(response, "response is null");
		String method = "signResponse";
		logDebug(method, "...");

		GatewayPayloadSigner signer = GatewayPayloadSigner.create(secretKey).withApiKey(apiKey)
				.withHid(response.getRequestId()).withName(response.getEventName())
				.withEncrypted(response.isEncrypted());
		if (response.getParameters() != null) {
			response.getParameters().forEach((name, value) -> signer.withParameter(name, value));
		}
		response.setSignatureVersion(GatewayPayloadSigner.PAYLOAD_SIGNATURE_VERSION_1);
		response.setSignature(signer.signV1());
		return response;
	}

	private CloudResponseModel buildResponse(CloudRequestModel request, CloudResponseStatus status, String message,
			String payload) {
		Assert.notNull(request, "request is null");
		Assert.notNull(status, "status is null");
		CloudResponseModel response = new CloudResponseModel();
		response.setEventName(request.getEventName());
		response.setRequestId(request.getRequestId());
		response.setEncrypted(false);
		Map<String, String> parameters = new LinkedHashMap<>();
		parameters.put(CloudResponseModel.STATUS_PARAMETER_NAME, status.toString());
		if (message != null) {
			parameters.put(CloudResponseModel.MESSAGE_PARAMETER_NAME, message);
		}
		if (payload != null) {
			parameters.put(CloudResponseModel.PAYLOAD_PARAMETER_NAME, payload);
		}
		response.setParameters(parameters);
		return response;
	}

	private CloudResponseModel buildErrorResponse(CloudRequestModel request, String message, String payload) {
		return buildResponse(request, CloudResponseStatus.ERROR, message, payload);
	}

	private CloudResponseModel buildErrorResponse(CloudRequestModel request, String message) {
		return buildErrorResponse(request, message, (String) null);
	}

	private CloudResponseModel buildSuccessResponse(CloudRequestModel request, String payload) {
		return buildResponse(request, CloudResponseStatus.OK, (String) null, payload);
	}

	private CloudResponseModel sendApiRequest(String baseUrl, CloudRequestModel request) throws Exception {
		Assert.hasText(baseUrl, "baseUrl is empty");
		Assert.notNull(request, "request is null");
		String method = "sendApiRequest";
		logDebug(method, "baseUrl: %s", baseUrl);

		CloudRequestMethodName requestMethod = CloudRequestMethodName
				.valueOf(request.getParameters().get(CloudRequestParameters.METHOD_PARAMETER_NAME));
		String uri = request.getParameters().get(CloudRequestParameters.URI_PARAMETER_NAME);

		HttpRequestBase httpRequest = null;
		URI apiUri = new URI(baseUrl + uri).normalize();
		switch (requestMethod) {
		case GET:
			httpRequest = new HttpGet(apiUri);
			break;
		case POST:
			httpRequest = addRequestBody(new HttpPost(apiUri), request);
			break;
		case PUT:
			httpRequest = addRequestBody(new HttpPut(apiUri), request);
			break;
		case DELETE:
			httpRequest = new HttpDelete(apiUri);
			break;
		default:
			throw new AcsLogicalException("Unsupported request method: " + requestMethod);
		}
		addRequestHeaders(httpRequest, request);

		logDebug(method, "httpRequest: %s, headers: %s", httpRequest, Arrays.asList(httpRequest.getAllHeaders()));

		try (CloseableHttpResponse httpResponse = ConnectionManager.getInstance().getSharedClient()
				.execute(httpRequest)) {
			logDebug(method, "httpResponse: %s", httpResponse);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			String content = AcsUtils.streamToString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
			if (statusCode != HttpStatus.SC_OK) {
				String message = String.format("error response: %d - %s", statusCode,
						httpResponse.getStatusLine().getReasonPhrase());
				return buildErrorResponse(request, message, content);
			} else {
				return buildSuccessResponse(request, content);
			}
		}
	}

	private HttpEntityEnclosingRequestBase addRequestBody(HttpEntityEnclosingRequestBase httpRequest,
			CloudRequestModel request) {
		Assert.notNull(httpRequest, "httpRequest is null");
		Assert.notNull(request, "request is null");
		if (request.getParameters() != null) {
			String body = request.getParameters().get(CloudRequestParameters.BODY_PARAMETER_NAME);
			if (StringUtils.isNotBlank(body)) {
				httpRequest.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
			}
		}
		return httpRequest;
	}

	private HttpRequestBase addRequestHeaders(HttpRequestBase httpRequest, CloudRequestModel request) {
		Assert.notNull(httpRequest, "httpRequest is null");
		Assert.notNull(request, "request is null");
		httpRequest.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		httpRequest.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
		httpRequest.addHeader(ApiHeaders.X_ARROW_APIKEY,
				request.getParameters().get(CloudRequestParameters.API_KEY_PARAMETER_NAME));
		String timestamp = request.getParameters().get(CloudRequestParameters.TIMESTAMP_PARAMETER_NAME);
		if (StringUtils.isNotBlank(timestamp)) {
			httpRequest.addHeader(ApiHeaders.X_ARROW_DATE, timestamp);
		}
		String signatureVersion = request.getParameters()
				.get(CloudRequestParameters.API_REQUEST_SIGNATURE_VERSION_PARAMETER_NAME);
		if (StringUtils.isNotBlank(signatureVersion)) {
			httpRequest.addHeader(ApiHeaders.X_ARROW_VERSION, signatureVersion);
		}
		String signature = request.getParameters().get(CloudRequestParameters.API_REQUEST_SIGNATURE_PARAMETER_NAME);
		if (StringUtils.isNotBlank(signature)) {
			httpRequest.addHeader(ApiHeaders.X_ARROW_SIGNATURE, signature);
		}
		return httpRequest;
	}
}
