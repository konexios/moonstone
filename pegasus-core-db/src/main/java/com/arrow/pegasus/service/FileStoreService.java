package com.arrow.pegasus.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.data.FileStore;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.repo.CriteriaUtils;
import com.arrow.pegasus.repo.params.FileStoreSearchParams;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class FileStoreService extends ServiceAbstract {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private CoreCacheService coreCache;

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private MongoDbFactory factory;

    public GridFsTemplate getTemplate() {
        return template;
    }

    public FileStore create(FileStore fileStore, byte[] content) {
        Assert.isTrue(content != null && content.length > 0, "content is empty");
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(content)) {
            return create(fileStore, inputStream);
        } catch (Exception e) {
            throw new AcsLogicalException("Error saving to fileStore", e);
        }
    }

    public FileStore create(FileStore fileStore, InputStream inputStream) {
        String method = "create";
        Assert.notNull(fileStore, "fileStore is null");
        Assert.notNull(inputStream, "inputStream is null");

        logInfo(method, "name: %s, contentType: %s", fileStore.getName(), fileStore.getContentType());

        if (StringUtils.isNotEmpty(fileStore.getApplicationId())) {
            Application application = coreCache.findApplicationById(fileStore.getApplicationId());
            checkEnabled(application, "application");
            fileStore.setCompanyId(application.getCompanyId());
        } else if (StringUtils.isNotEmpty(fileStore.getCompanyId())) {
            Company company = coreCache.findCompanyById(fileStore.getCompanyId());
            Assert.isTrue(company != null && company.getStatus() == CompanyStatus.Active,
                    "company not found or not active: " + fileStore.getCompanyId());
        } else {
            throw new AcsLogicalException("Either applicationId or companyId must be provided");
        }

        Assert.hasText(fileStore.getName(), "fileStore name is missing");
        Assert.hasText(fileStore.getContentType(), "fileStore contentType is missing");
        Assert.hasText(fileStore.getCreatedBy(), "fileStore createdBy is missing");

        fileStore.setHid(cryptoService.getCrypto().hashId(cryptoService.getCrypto().randomToken()));
        ObjectId objectId = template.store(inputStream, fileStore.getName(), fileStore.getContentType(),
                fileStore.getProperties());
        logInfo(method, "objectId: %s", objectId);
        populate(fileStore, template.findOne(new Query(Criteria.where("_id").is(objectId))));
        logInfo(method, "created id: %s, md5: %s", fileStore.getId(), fileStore.getMd5());

        return fileStore;
    }

    public List<FileStore> findBy(FileStoreSearchParams params) {
        String method = "findBy";
        List<FileStore> result = new ArrayList<>();
        for (GridFSFile file : template.find(CriteriaUtils.createQuery(buildCriteria(params)))) {
            result.add(populate(new FileStore(), file));
        }
        logInfo(method, "result size: %d", result.size());
        return result;
    }

    public void delete(FileStore fileStore) {
        String method = "delete";
        logInfo(method, "deleting id: %s, name: %s", fileStore.getId(), fileStore.getName());
        template.delete(new Query(Criteria.where("_id").is(fileStore.getId())));
    }

    public byte[] readFile(FileStore fileStore) {
        String method = "readFile";
        logInfo(method, "reading file id: %s, name: %s", fileStore.getId(), fileStore.getName());
        try {
            GridFSFile file = template.findOne(new Query(Criteria.where("_id").is(fileStore.getId())));
            if (file != null) {
                logInfo(method, "found GridFsFile: %s, length: %d, uploadDate: %s", file.getFilename(),
                        file.getLength(), file.getUploadDate());
                try (InputStream inputStream = getGridFs().openDownloadStream(file.getObjectId())) {
                    byte[] result = StreamUtils.copyToByteArray(inputStream);
                    logInfo(method, "result size: %d", result == null ? 0 : result.length);
                    return result;
                }
            } else {
                throw new AcsLogicalException("file not found: " + fileStore.getId() + " / " + fileStore.getName());
            }
        } catch (AcsLogicalException e) {
            throw e;
        } catch (Exception e) {
            throw new AcsLogicalException("Error reading to fileStore", e);
        }
    }

    private List<Criteria> buildCriteria(FileStoreSearchParams params) {
        List<Criteria> criteria = new ArrayList<Criteria>();
        if (params != null) {
            criteria = CriteriaUtils.addCriteria(criteria, "metadata.applicationId", params.getApplicationIds());
            criteria = CriteriaUtils.addCriteria(criteria, "metadata.category", params.getCategories());
            criteria = CriteriaUtils.addCriteria(criteria, "metadata.companyId", params.getCompanyIds());
            criteria = CriteriaUtils.addCriteria(criteria, "contentType", params.getContentTypes());
            criteria = CriteriaUtils.addCriteria(criteria, "metadata.hid", params.getHids());
            criteria = CriteriaUtils.addCriteria(criteria, "_id", params.getIds());
            criteria = CriteriaUtils.addCriteria(criteria, "filename", params.getNames());
            criteria = CriteriaUtils.addCriteria(criteria, "metadata.relatedId", params.getRelatedIds());
        }
        return criteria;
    }

    @SuppressWarnings("deprecation")
    private FileStore populate(FileStore fileStore, GridFSFile file) {
        Document metadata = file.getMetadata();
        fileStore.setApplicationId(getString(metadata, "applicationId"));
        fileStore.setCategory(getString(metadata, "category"));
        fileStore.setCompanyId(getString(metadata, "companyId"));
        String contentType = getString(metadata, "contentType");
        if (StringUtils.isEmpty(contentType)) {
            contentType = file.getContentType();
        }
        fileStore.setContentType(contentType);
        fileStore.setCreatedBy(getString(metadata, "createdBy"));
        fileStore.setHid(getString(metadata, "hid"));
        fileStore.setId(file.getObjectId().toString());
        fileStore.setMd5(file.getMD5());
        fileStore.setName(file.getFilename());
        fileStore.setRelatedId(getString(metadata, "relatedId"));
        fileStore.setSize(file.getLength());
        fileStore.setUploadDate(file.getUploadDate().toInstant());
        return fileStore;
    }

    private String getString(Document object, String field) {
        Object result = object.get(field);
        return result == null ? null : result.toString();
    }

    private GridFSBucket getGridFs() {
        return GridFSBuckets.create(factory.getDb());
    }
}
