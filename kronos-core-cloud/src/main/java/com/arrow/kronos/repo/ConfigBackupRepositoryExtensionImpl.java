package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.kronos.data.ConfigBackup;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class ConfigBackupRepositoryExtensionImpl extends RepositoryExtensionAbstract<ConfigBackup>
        implements ConfigBackupRepositoryExtension {

	public ConfigBackupRepositoryExtensionImpl() {
		super(ConfigBackup.class);
	}

	@Override
	public Page<ConfigBackup> findConfigBackups(Pageable pageable, ConfigBackupSearchParams params) {
		String method = "findConfigBackups";
		logInfo(method, "...");
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<ConfigBackup> findConfigBackups(ConfigBackupSearchParams params) {
		String method = "findConfigBackups";
		logInfo(method, "...");
		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(ConfigBackupSearchParams params) {
		List<Criteria> criteria = new ArrayList<>();
		if (params != null) {
			criteria = addCriteria(criteria, "objectId", params.getObjectIds());
			criteria = addCriteria(criteria, "type", params.getTypes());
			criteria = addCriteria(criteria, "name", params.getNames());
			if (params.getCreatedBefore() != null || params.getCreatedAfter() != null) {
				criteria = addCriteria(criteria, "createdDate", params.getCreatedAfter(), params.getCreatedBefore());
			}
		}
		return criteria;
	}
}