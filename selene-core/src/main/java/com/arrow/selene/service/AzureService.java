package com.arrow.selene.service;

import java.util.List;

import com.arrow.selene.dao.AzureDao;
import com.arrow.selene.data.Azure;

public class AzureService extends ServiceAbstract {

	private static class SingletonHolder {
		static final AzureService SINGLETON = new AzureService();
	}

	public static AzureService getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private final AzureDao azureDao;

	protected AzureService() {
		azureDao = AzureDao.getInstance();
	}

	public Azure findOne() {
		List<Azure> all = azureDao.findAll();
		if (all.size() > 1) {
			throw new RuntimeException("ERROR: more than one Azure record found!");
		}
		if (all.size() == 1) {
			return all.get(0);
		} else {
			return null;
		}
	}

	protected AzureDao getAzureDao() {
		return azureDao;
	}
}
