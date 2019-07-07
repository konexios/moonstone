package com.arrow.kronos.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.AWSIotClientBuilder;
import com.amazonaws.services.iot.model.AttachPrincipalPolicyRequest;
import com.amazonaws.services.iot.model.AttachThingPrincipalRequest;
import com.amazonaws.services.iot.model.AttributePayload;
import com.amazonaws.services.iot.model.CertificateStatus;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;
import com.amazonaws.services.iot.model.CreatePolicyRequest;
import com.amazonaws.services.iot.model.CreateThingRequest;
import com.amazonaws.services.iot.model.CreateThingResult;
import com.amazonaws.services.iot.model.DescribeEndpointRequest;
import com.amazonaws.services.iot.model.DescribeEndpointResult;
import com.amazonaws.services.iot.model.GetPolicyRequest;
import com.amazonaws.services.iot.model.GetPolicyResult;
import com.amazonaws.services.iot.model.ResourceAlreadyExistsException;
import com.amazonaws.services.iot.model.UpdateCertificateRequest;
import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.AcsSystemException;
import com.arrow.acs.AcsUtils;
import com.arrow.kronos.KronosCloudConstants.Aws;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.AwsAccount;
import com.arrow.kronos.data.AwsThing;
import com.arrow.kronos.data.Gateway;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.profile.Application;

@Service
public class AwsIntegrationService extends KronosServiceAbstract {

	@Autowired
	private AwsAccountService accountService;
	@Autowired
	private AwsThingService thingService;
	@Autowired
	private GatewayService gatewayService;

	public boolean checkAndCreateDefaultPolicy(AwsAccount account) {
		String method = "checkAndCreateDefaultPolicy";
		Assert.notNull(account, "account is null");

		if (!account.isEnabled()) {
			throw new AcsLogicalException("aws account is disabled");
		}

		boolean update = false;
		// sanity check
		if (StringUtils.isEmpty(account.getDefaultPolicyArn())) {
			AWSIot client = connect(account);
			CreatePolicyRequest request = new CreatePolicyRequest();
			try {
				request.setPolicyDocument(AcsUtils.streamToString(
						getClass().getResourceAsStream(Aws.DEFAULT_POLICY_FILE), StandardCharsets.UTF_8));
			} catch (IOException e) {
				throw new AcsSystemException("unable to load default policy file");
			}
			request.setPolicyName(Aws.DEFAULT_POLICY_NAME);
			try {
				String arn = client.createPolicy(request).getPolicyArn();
				logInfo(method, "created new policy, arn: " + arn);
				account.setDefaultPolicyArn(arn);
				account.setDefaultPolicyName(Aws.DEFAULT_POLICY_NAME);
				update = true;
			} catch (ResourceAlreadyExistsException e) {
				account.setDefaultPolicyName(Aws.DEFAULT_POLICY_NAME);
				account.setDefaultPolicyArn("TBD");

				// search for policy
				GetPolicyResult policyResult = client
						.getPolicy(new GetPolicyRequest().withPolicyName(Aws.DEFAULT_POLICY_NAME));
				account.setDefaultPolicyArn(policyResult.getPolicyArn());
				update = true;
				logInfo(method, "policy already exists!");
			} catch (Throwable t) {
				throw new AcsLogicalException("unable to create default policy", t);
			}
		}

		if (update) {
			accountService.getAwsAccountRepository().doSave(account, null);
			getKronosCache().clearAwsAccount(account);
		}
		return update;
	}

	public AwsThing checkAndCreateAwsThing(Gateway gateway) {
		String method = "checkAndCreateAwsThing";

		AwsThing result = thingService.findBy(gateway);
		if (result == null) {
			AwsAccount account = accountService.findActiveAccount(gateway);
			if (account != null) {
				// create new aws device
				result = new AwsThing();
				result.setGatewayId(gateway.getId());
				result.setApplicationId(gateway.getApplicationId());
				result.setEnabled(true);
				result.setAwsAccountId(account.getId());

				AWSIot client = connect(account);

				// create thing
				logInfo(method, "create thing ...");
				AttributePayload attrs = new AttributePayload();
				attrs.addAttributesEntry(KronosConstants.AwsDevice.ATTR_HID, gateway.getHid());
				attrs.addAttributesEntry(KronosConstants.AwsDevice.ATTR_UID, fixAttribute(gateway.getUid()));
				attrs.addAttributesEntry(KronosConstants.AwsDevice.ATTR_NAME, fixAttribute(gateway.getName()));
				CreateThingResult createResult = client.createThing(
						new CreateThingRequest().withThingName(gateway.getUid()).withAttributePayload(attrs));
				result.setThingArn(createResult.getThingArn());
				result.setThingName(createResult.getThingName());

				// get host info
				logInfo(method, "get endpoint info ...");
				DescribeEndpointResult epResult = client.describeEndpoint(new DescribeEndpointRequest());
				result.setHost(epResult.getEndpointAddress());

				// create cert
				logInfo(method, "create new cert ...");
				Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
				checkEnabled(application, "application");
				CreateKeysAndCertificateResult certResult = client
						.createKeysAndCertificate(new CreateKeysAndCertificateRequest());
				result.setCertArn(certResult.getCertificateArn());
				result.setCertId(certResult.getCertificateId());
				result.setCertPem(getCryptoService().encrypt(application.getId(), certResult.getCertificatePem()));
				result.setPublicKey(
						getCryptoService().encrypt(application.getId(), certResult.getKeyPair().getPublicKey()));
				result.setPrivateKey(
						getCryptoService().encrypt(application.getId(), certResult.getKeyPair().getPrivateKey()));

				// check/create default policy
				if (StringUtils.isEmpty(account.getDefaultPolicyArn())) {
					checkAndCreateDefaultPolicy(account);
				}

				// attach cert to policy
				logInfo(method, "attach cert to policy ...");
				client.attachPrincipalPolicy(new AttachPrincipalPolicyRequest()
						.withPolicyName(account.getDefaultPolicyName()).withPrincipal(certResult.getCertificateArn()));

				logInfo(method, "attach cert to thing ...");
				client.attachThingPrincipal(new AttachThingPrincipalRequest().withThingName(gateway.getUid())
						.withPrincipal(result.getCertArn()));

				// activate cert
				logInfo(method, "activate cert ...");
				client.updateCertificate(new UpdateCertificateRequest().withCertificateId(certResult.getCertificateId())
						.withNewStatus(CertificateStatus.ACTIVE));

				thingService.getAwsThingRepository().doInsert(result, null);
				logInfo(method, "created new aws device: %s", result.getPri());
			} else {
				logInfo(method, "no active AWS account found");
			}
		}
		// update gateway with externalId
		if (result != null && StringUtils.isEmpty(gateway.getExternalId())) {
			gateway.setExternalId(gateway.getUid());
			gatewayService.update(gateway, CoreConstant.ADMIN_USER);
			logInfo(method, "updated gateway with externalId: %s", gateway.getUid());
		}
		return result;
	}

	private AWSIot connect(AwsAccount account) {
		Assert.notNull(account, "account is null");

		Application application = getCoreCacheService().findApplicationById(account.getApplicationId());
		checkEnabled(application, "application");

		// create credentials
		AWSCredentials creds = new BasicAWSCredentials(
				getCryptoService().decrypt(application.getId(), account.getAccessKey()),
				getCryptoService().decrypt(application.getId(), account.getSecretKey()));

		return AWSIotClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
				.withRegion(account.getRegion()).build();
		// return new
		// AWSIotClient(creds).withRegion(RegionUtils.getRegion(account.getRegion()));
	}

	private String fixAttribute(String value) {
		return value.replace(' ', '_');
	}
}
