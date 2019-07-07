package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.ConfigBackup;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface ConfigBackupRepositoryExtension extends RepositoryExtension<ConfigBackup> {

	Page<ConfigBackup> findConfigBackups(Pageable pageable, ConfigBackupSearchParams params);

	List<ConfigBackup> findConfigBackups(ConfigBackupSearchParams params);
}