package com.arrow.pegasus.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arrow.acs.Loggable;
import com.arrow.pegasus.CoreAuditLog;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.service.AuditLogService;
import com.arrow.pegasus.service.CoreCacheService;

@Component
public class CoreCacheUtil extends Loggable {

    @Autowired
    private CoreCacheService coreCacheService;

    @Autowired
    private AuditLogService auditLogService;

    public CoreCacheUtil() {
        logInfo(getClass().getSimpleName(), "...");
    }

    public void clearAll(String who) {
        String method = "clearAll";

        logInfo(method, "who: %s", who);
        coreCacheService.clearUsers();
        coreCacheService.clearApplications();
        coreCacheService.clearAuths();
        coreCacheService.clearCompanies();
        coreCacheService.clearProducts();
        coreCacheService.clearRolesAndPrivileges();
        coreCacheService.clearSubscriptions();
        coreCacheService.clearApplicationEngines();

        // write audit log
        auditLogService.save(AuditLogBuilder.create().type(CoreAuditLog.Cache.CLEAR_ALL_CORE_CACHES)
                .productName(ProductSystemNames.PEGASUS).by(who));
    }
}
