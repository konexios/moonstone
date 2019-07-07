package com.arrow.pegasus.data;

import org.springframework.util.Assert;

public class AuditLogBuilder {
    private AuditLog auditLog = new AuditLog();

    public static AuditLogBuilder create() {
        return new AuditLogBuilder();
    }

    private AuditLogBuilder() {
    }

    public AuditLogBuilder productName(String productName) {
        Assert.hasLength(productName, "productName is empty");
        auditLog.setProductName(productName);
        return this;
    }

    public AuditLogBuilder type(String type) {
        Assert.hasLength(type, "type is empty");
        auditLog.setType(type);
        return this;
    }

    public AuditLogBuilder applicationId(String applicationId) {
        Assert.hasLength(applicationId, "applicationId is empty");
        auditLog.setApplicationId(applicationId);
        return this;
    }

    public AuditLogBuilder objectId(String objectId) {
        Assert.hasLength(objectId, "objectId is empty");
        auditLog.setObjectId(objectId);
        return this;
    }

    public AuditLogBuilder relatedId(String... relatedIds) {
        if (relatedIds != null && relatedIds.length > 0) {
            for (String relatedId : relatedIds) {
                Assert.hasLength(relatedId, "relatedId is empty");
                auditLog.getRelatedIds().add(relatedId);
            }
        }
        return this;
    }

    public AuditLogBuilder parameter(String name, String value) {
        Assert.hasLength(name, "name is empty");
        auditLog.getParameters().put(name, value);
        return this;
    }

    public AuditLogBuilder by(String who) {
        Assert.hasLength(who, "who is empty");
        auditLog.setCreatedBy(who);
        return this;
    }

    public AuditLog build() {
        return auditLog;
    }
}
