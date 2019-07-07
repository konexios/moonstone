package com.arrow.kronos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.GlobalTag;
import com.arrow.kronos.repo.GlobalTagRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;

@Service
public class GlobalTagService extends KronosServiceAbstract {

	@Autowired
	private GlobalTagRepository globalTagRepository;

	public GlobalTagRepository getGlobalTagRepository() {
		return globalTagRepository;
	}

	public GlobalTag create(GlobalTag globalTag, String who) {
		Assert.hasText(who, "who is empty");
		validateGlobalTag(globalTag);

		GlobalTag tag = globalTagRepository.findByName(globalTag.getName());
		Assert.isNull(tag, "name already exists");

		globalTag = globalTagRepository.doInsert(globalTag, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.GlobalTag.CreateGlobalTag)
		        .productName(ProductSystemNames.KRONOS).by(who).objectId(globalTag.getId())
		        .parameter("name", globalTag.getName()).parameter("tagType", globalTag.getTagType().toString())
		        .parameter("objectType", globalTag.getObjectType()));

		return globalTag;
	}

	public GlobalTag update(GlobalTag globalTag, String who) {
		Assert.hasText(who, "who is empty");
		validateGlobalTag(globalTag);
		Assert.notNull(globalTag.getId(), "globalTagId is null");

		GlobalTag tag = globalTagRepository.findById(globalTag.getId()).orElse(null);
		Assert.notNull(tag, "globalTag is not found");
		if (!tag.getName().equals(globalTag.getName())) {
			tag = globalTagRepository.findByName(globalTag.getName());
			Assert.isNull(tag, "name already exists");
		}

		globalTag = globalTagRepository.doSave(globalTag, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.GlobalTag.UpdateGlobalTag)
		        .productName(ProductSystemNames.KRONOS).by(who).objectId(globalTag.getId())
		        .parameter("name", globalTag.getName()).parameter("tagType", globalTag.getTagType().toString())
		        .parameter("objectType", globalTag.getObjectType()));

		return globalTag;
	}

	private GlobalTag validateGlobalTag(GlobalTag globalTag) {
		Assert.notNull(globalTag, "globalTag is null");
		Assert.hasText(globalTag.getName(), "name is empty");
		Assert.notNull(globalTag.getTagType(), "tagType is null");
		Assert.hasText(globalTag.getObjectType(), "objectType is empty");
		return globalTag;
	}
}
