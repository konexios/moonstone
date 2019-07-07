package com.arrow.kronos;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.AWSIotClientBuilder;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.amazonaws.services.iot.model.DescribeEndpointRequest;
import com.amazonaws.services.iot.model.DescribeEndpointResult;
import com.arrow.kronos.data.AwsAccount;
import com.arrow.kronos.data.AwsThing;
import com.arrow.kronos.service.AwsAccountService;
import com.arrow.kronos.service.AwsThingService;
import com.arrow.kronos.util.RetriableTask;
import com.arrow.pegasus.data.profile.Application;

public class AwsMqttTelemetryListener extends TelemetryLoopbackAbstract {
	private static final char[] PASSWORD = new char[0];

	@Value("${aws.mqtt.topic}")
	private String topic;

	@Autowired
	private AwsAccountService accountService;

	@Autowired
	private AwsThingService thingService;

	private ConcurrentHashMap<String, AWSIotMqttClient> clients = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, String> localEndpointCache = new ConcurrentHashMap<>();

	@Override
	protected Runnable createAccountMonitor() {
		return new AccountMonitor();
	}

	boolean connectClient(AwsAccount account) {
		String method = "connectClient";
		String endpoint = localEndpointCache.get(account.getHid());
		logInfo(method, "accountId: %s, endpoint: %s", account.getId(), endpoint);
		if (!atomicModifyConnectingClient(endpoint, true)) {
			logWarn(method, "connection already in progress for endpoint: %s", endpoint);
			return false;
		}

		close(clients.remove(endpoint));
		AWSIotMqttClient client = createClient(account);
		if (client != null) {
			clients.put(endpoint, client);
			atomicModifyConnectingClient(endpoint, false);
		} else {
			logWarn(method, "skipping connect!");
		}
		return client != null;
	}

	private AWSIotMqttClient createClient(AwsAccount account) {
		String method = "createClient";

		String endpoint = localEndpointCache.get(account.getHid());
		logInfo(method, "account hid: %s, endpoint: %s", account.getHid(), endpoint);

		RetriableTask<AWSIotMqttClient> task = new RetriableTask<AWSIotMqttClient>(getRetryStrategy()) {

			AWSIotMqttClient client;

			@Override
			public AWSIotMqttClient execute() throws Exception {
				String method = "RetriableTask.execute";
				Application app = getContext().getCoreCacheService().findApplicationById(account.getApplicationId());
				checkEnabled(app, "application");

				String endpoint = localEndpointCache.get(account.getHid());
				logInfo(method, "endpoint: %s", endpoint);
				List<AwsThing> awsThings = thingService.findAllByAwsAccountIdAndHost(account.getId(), endpoint);
				if (awsThings.isEmpty()) {
					logInfo(method, "no active gateways found");
					return null;
				}

				AwsThing awsThing = awsThings.get(0);
				KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
				ks.load(null, null);

				try (PEMParser cert = new PEMParser(
						new StringReader(getContext().getCryptoService().decrypt(app.getId(), awsThing.getCertPem())));
						PEMParser key = new PEMParser(new StringReader(
								getContext().getCryptoService().decrypt(app.getId(), awsThing.getPrivateKey())))) {
					X509Certificate x509 = new JcaX509CertificateConverter().setProvider("BC")
							.getCertificate((X509CertificateHolder) cert.readObject());
					ks.setCertificateEntry("certificate", x509);
					ks.setKeyEntry("private-key",
							BouncyCastleProvider.getPrivateKey(((PEMKeyPair) key.readObject()).getPrivateKeyInfo()),
							PASSWORD, new Certificate[] { x509 });
				}
				client = new AWSIotMqttClient(endpoint, MqttClient.generateClientId(), ks, "") {
					@Override
					public void onConnectionClosed() {
						String method = "onConnectionClosed";
						super.onConnectionClosed();
						logInfo(method, "connection closed");
						connectClient(account);
					}
				};
				logInfo(method, "connecting to: %s", endpoint);
				client.connect();
				logInfo(method, "connected");

				logInfo(method, "subscribing to: %s", topic);
				client.subscribe(new TelemetryTopic(topic, account));
				return client;
			}

			@Override
			public void onError() {
				close(client);
			}
		};
		return task.call();
	}

	@Override
	protected void preDestroy() {
		super.preDestroy();
		clients.values().forEach(this::close);
	}

	@Override
	protected void postConstruct() {
		super.postConstruct();
		if (isEnabled()) {
			Assert.hasText(topic, "aws.mqtt.topic is empty");
		}
	}

	private void close(AWSIotMqttClient client) {
		String method = "close";
		try {
			if (client != null) {
				logInfo(method, "closing: %s", client.getClientEndpoint());
				client.disconnect();
			}
		} catch (Throwable ignored) {
		}
	}

	private String getEndpointAddress(Application application, AwsAccount account) {
		String accessKey = getContext().getCryptoService().decrypt(application.getId(), account.getAccessKey());
		String secretKey = getContext().getCryptoService().decrypt(application.getId(), account.getSecretKey());
		AWSIot awsIot = AWSIotClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.withRegion(account.getRegion()).build();
		DescribeEndpointResult result = awsIot.describeEndpoint(new DescribeEndpointRequest());
		return result.getEndpointAddress();
	}

	private class AccountMonitor implements Runnable {
		@Override
		public void run() {
			String method = "AccountWorker.run";
			try {
				List<AwsAccount> accounts = accountService.getAwsAccountRepository().findAll();
				Set<String> activeEndpoints = new HashSet<>();
				for (AwsAccount account : accounts) {
					String endpoint = localEndpointCache.get(account.getHid());
					if (endpoint == null) {
						try {
							endpoint = getEndpointAddress(
									getContext().getCoreCacheService().findApplicationById(account.getApplicationId()),
									account);
							localEndpointCache.put(account.getHid(), endpoint);
						} catch (Throwable t) {
							logError(method, "error processing account: " + account.getId(), t);
						}
					}
					if (endpoint != null && account.isEnabled() && checkLoopbackEnabled(account.getApplicationId())) {
						activeEndpoints.add(endpoint);
					}
				}
				for (AwsAccount account : accounts) {
					String endpoint = localEndpointCache.get(account.getHid());
					if (!StringUtils.isEmpty(endpoint)) {
						logInfo(method, "processing endpoint: %s, enabled: %s", endpoint, account.isEnabled());
						if (account.isEnabled() && checkLoopbackEnabled(account.getApplicationId())) {
							try {
								if (clients.containsKey(endpoint)) {
									logInfo(method, "endpoint already added: %s", endpoint);
								} else {
									logInfo(method, "creating new client for endpoint: %s", endpoint);
									new Thread(() -> connectClient(account)).start();
								}
							} catch (Throwable t) {
								logError(method, t);
							}
						} else if (endpoint != null && !activeEndpoints.contains(endpoint)) {
							AWSIotMqttClient client = clients.remove(endpoint);
							if (client == null) {
								logInfo(method, "endpoint already removed: %s", endpoint);
							} else {
								close(client);
							}
						}
					}
				}
			} catch (Throwable t) {
				logError(method, "error refreshing accounts", t);
			}
		}
	}

	private class TelemetryTopic extends AWSIotTopic {
		private final AwsAccount account;

		public TelemetryTopic(String topic, AwsAccount account) {
			super(topic, AWSIotQos.QOS0);
			this.account = account;
		}

		@Override
		public void onMessage(AWSIotMessage message) {
			String method = "TelemetryTopic.onMessage";
			super.onMessage(message);
			try {
				logInfo(method, "applicationId: %s, topic: %s, message size: %s", account.getApplicationId(), topic,
						message.getPayload().length);
				if (!isTerminating()) {
					getService().execute(new RabbitWorker(new String(message.getPayload(), StandardCharsets.UTF_8)));
				}
			} catch (Throwable t) {
				logError(method, "error sending message to worker thread", t);
			}
		}

		@Override
		public void onSuccess() {
			String method = "TelemetryTopic.onSuccess";
			super.onSuccess();
			logInfo(method, "subscribed");
		}
	}
}
