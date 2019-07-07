package com.arrow.pegasus.data;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class FileStore implements Serializable {
    private static final long serialVersionUID = 7552580684904135248L;

    private String id;
    private String name;
    private String contentType;
    private long size;
    private String md5;
    private Instant uploadDate;

    private Map<String, String> properties = new HashMap<>();

    public FileStore withId(String id) {
        setId(id);
        return this;
    }

    public FileStore withHid(String hid) {
        setHid(hid);
        return this;
    }

    public FileStore withCompanyId(String companyId) {
        setCompanyId(companyId);
        return this;
    }

    public FileStore withApplicationId(String applicationId) {
        setApplicationId(applicationId);
        return this;
    }

    public FileStore withCategory(String category) {
        setCategory(category);
        return this;
    }

    public FileStore withRelatedId(String relatedId) {
        setRelatedId(relatedId);
        return this;
    }

    public FileStore withCreatedBy(String createdBy) {
        setCreatedBy(createdBy);
        return this;
    }

    public FileStore withName(String name) {
        setName(name);
        return this;
    }

    public FileStore withContentType(String contentType) {
        setContentType(contentType);
        return this;
    }

    public FileStore withSize(long size) {
        setSize(size);
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHid() {
        return properties.get("hid");
    }

    public void setHid(String hid) {
        properties.put("hid", hid);
    }

    public String getCompanyId() {
        return properties.get("companyId");
    }

    public void setCompanyId(String companyId) {
        properties.put("companyId", companyId);
    }

    public String getApplicationId() {
        return properties.get("applicationId");
    }

    public void setApplicationId(String applicationId) {
        properties.put("applicationId", applicationId);
    }

    public String getCategory() {
        return properties.get("category");
    }

    public void setCategory(String category) {
        properties.put("category", category);
    }

    public String getRelatedId() {
        return properties.get("relatedId");
    }

    public void setRelatedId(String relatedId) {
        properties.put("relatedId", relatedId);
    }

    public String getCreatedBy() {
        return properties.get("createdBy");
    }

    public void setCreatedBy(String createdBy) {
        properties.put("createdBy", createdBy);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
        properties.put("contentType", contentType);
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Instant getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void addProperty(String name, String value) {
        properties.put(name, value);
    }
}
