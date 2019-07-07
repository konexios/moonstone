package com.arrow.pegasus.repo;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.LastLogin;

public interface AuditLogRepositoryExtension extends RepositoryExtension<AuditLog> {

	public Page<AuditLog> findAuditLogs(Pageable pageable, String[] productName, String[] applicationId, String[] type,
	        String[] objectId, String[] relatedIds, Instant createdDateFrom, Instant createdDateTo, String[] createdBy);

	public Page<AuditLog> findAuditLogs(Pageable pageable, AuditLogSearchParams params);
	
	public List<AuditLog> findAuditLogs(AuditLogSearchParams params);
	
	public long findAuditLogCount(AuditLogSearchParams params);
	
	public List<DistinctCountResult> countDistinctProperty(AuditLogSearchParams params, String property);

	public LastLogin findLastLogin(String userId, String applicationId, String productName);
}
