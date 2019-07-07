package com.arrow.pegasus.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.JsonUtils;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AccessPrivilege;
import com.arrow.pegasus.data.AccessPrivilege.AccessLevel;
import com.arrow.pegasus.repo.AccessKeyRepository;

@Service
public class AccessKeyService extends BaseServiceAbstract {

	public final static long DEFAULT_EXPIRATION_DAYS = 20 * 365; // 20 years

	@Autowired
	private AccessKeyRepository accessKeyRepository;

	public AccessKey createOwnerKey(String companyId, String subscriptionId, String applicationId, String name,
			String pri, String who) {
		String method = "createOwnerKey";
		logInfo(method, "creating owner key for pri: %s", pri);
		return create(companyId, subscriptionId, applicationId, name, null,
				Collections.singletonList(new AccessPrivilege(pri, AccessLevel.OWNER)), true, who);
	}

	public AccessKey create(String companyId, String subscriptionId, String applicationId,
			List<AccessPrivilege> privileges, boolean appOwnerKey, String who) {
		return create(companyId, subscriptionId, applicationId, null, null, privileges, appOwnerKey, who);
	}

	public AccessKey create(String companyId, String subscriptionId, String applicationId, String name,
			Instant expiration, List<AccessPrivilege> privileges, boolean appOwnerKey, String who) {
		return create(companyId, subscriptionId, applicationId, getCryptoService().getCrypto().randomToken(),
				getCryptoService().getCrypto().createPrivateKey(), name, expiration, privileges, appOwnerKey, who);
	}

	public AccessKey create(String companyId, String subscriptionId, String applicationId, String apiKey,
			String secretKey, Instant expiration, List<AccessPrivilege> privileges, boolean appOwnerKey, String who) {
		return create(companyId, subscriptionId, applicationId, apiKey, secretKey, null, expiration, privileges,
				appOwnerKey, who);
	}

	public AccessKey create(String companyId, String subscriptionId, String applicationId, String apiKey,
			String secretKey, String name, Instant expiration, List<AccessPrivilege> privileges, boolean appOwnerKey,
			String who) {
		String method = "create";

		Assert.hasText(apiKey, "apiKey is required");
		Assert.hasText(secretKey, "secretKey is required");
		AccessKey accessKey = new AccessKey();

		if (!StringUtils.isEmpty(applicationId) && !appOwnerKey) {
			accessKey.setEncryptedApiKey(getCryptoService().encrypt(applicationId, apiKey));
			accessKey.setEncryptedSecretKey(getCryptoService().encrypt(applicationId, secretKey));
		} else if (!StringUtils.isEmpty(companyId)) {
			accessKey.setEncryptedApiKey(getCryptoService().getCrypto().internalEncrypt(apiKey));
			accessKey.setEncryptedSecretKey(getCryptoService().getCrypto().internalEncrypt(secretKey));
		}

		accessKey.setCompanyId(companyId);
		accessKey.setSubscriptionId(subscriptionId);
		accessKey.setApplicationId(applicationId);
		accessKey.setName(name);
		accessKey.setHashedApiKey(getCryptoService().getCrypto().internalHash(apiKey));

		if (expiration == null) {
			expiration = Instant.now().plus(DEFAULT_EXPIRATION_DAYS, ChronoUnit.DAYS);
		}
		accessKey.setExpiration(expiration);

		if (privileges != null) {
			accessKey.setPrivileges(privileges);
		} else {
			logWarn(method, "privileges are missing!");
		}

		accessKey = accessKeyRepository.doInsert(accessKey, who);
		logInfo(method, "created key: %s", JsonUtils.toJson(accessKey));

		return accessKey;
	}

	public AccessKey update(AccessKey accessKey, String who) {
		String method = "update";

		Assert.notNull(accessKey, "accessKey is required");
		accessKey = accessKeyRepository.doSave(accessKey, who);
		logInfo(method, "updated access key %s", accessKey.getId());

		getCoreCacheService().clearAccessKey(accessKey);

		return accessKey;
	}

	public AccessKey delete(AccessKey accessKey, String who) {
		String method = "delete";

		Assert.notNull(accessKey, "accessKey is required");
		accessKeyRepository.delete(accessKey);
		logInfo(method, "deleted access key %s by %s", accessKey.getId(), who);

		getCoreCacheService().clearAccessKey(accessKey);

		return accessKey;
	}

	public void expireNow(String id, String who) {
		String method = "expire";
		AccessKey key = accessKeyRepository.findById(id).orElse(null);
		if (key == null) {
			logError(method, "key not found: %s", id);
		} else {
			key.setExpiration(Instant.now());
			accessKeyRepository.doSave(key, who);
			logInfo(method, "expired key: %s", id);
		}
	}

	public AccessKey findOwnerKey(String pri) {
		AccessKey result = null;
		for (AccessKey key : accessKeyRepository.findByPri(pri)) {
			for (AccessPrivilege priv : key.findByPri(pri)) {
				if (priv.getLevel() == AccessLevel.OWNER) {
					result = key;
					break;
				}
			}
			if (result != null)
				break;
		}
		return getCoreCacheHelper().populateAccessKey(result);
	}

	public Long deleteByCompanyId(String companyId, String who) {
		String method = "deleteByCompanyId";
		logInfo(method, "companyId: %s, who: %s", companyId, who);
		return accessKeyRepository.deleteByCompanyId(companyId);
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return accessKeyRepository.deleteByApplicationId(applicationId);
	}

	public AccessKeyRepository getAccessKeyRepository() {
		return accessKeyRepository;
	}
}
