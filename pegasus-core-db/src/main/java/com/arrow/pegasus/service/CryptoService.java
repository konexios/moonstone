package com.arrow.pegasus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.security.Crypto;

import moonstone.acs.AcsUtils;

@Service
public class CryptoService extends ServiceAbstract {

	@Autowired
	private CoreCacheService coreCacheService;

	@Autowired
	private Crypto crypto;

	public String encrypt(String data) {
		return crypto.internalEncrypt(data);
	}

	public String encrypt(String applicationId, String data) {
		if (AcsUtils.isEmpty(applicationId)) {
			return encrypt(data);
		} else {
			return crypto.encrypt(data, decrypt(findOwnerKey(applicationId).getEncryptedSecretKey()));
		}
	}

	public String decrypt(String data) {
		AcsUtils.notEmpty(data, "data is empty");
		return crypto.internalDecrypt(data);
	}

	public String decrypt(String applicationId, String data) {
		if (AcsUtils.isEmpty(applicationId)) {
			return decrypt(data);
		} else {
			return crypto.decrypt(data, decrypt(findOwnerKey(applicationId).getEncryptedSecretKey()));
		}
	}

	public Crypto getCrypto() {
		return crypto;
	}

	private AccessKey findOwnerKey(String applicationId) {
		AcsUtils.notEmpty(applicationId, "applicationId is empty");
		Application application = coreCacheService.findApplicationById(applicationId);
		checkEnabled(application, "application");
		AccessKey accessKey = coreCacheService.findOwnerAccessKeyByPri(application.getPri());
		AcsUtils.notNull(accessKey, "owner key not found");
		return accessKey;
	}
}
