package com.arrow.rhea.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.FileStore;
import com.arrow.pegasus.repo.params.FileStoreSearchParams;
import com.arrow.pegasus.service.FileStoreService;
import com.arrow.rhea.RheaAuditLog;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.repo.SoftwareReleaseRepository;

@Service
public class SoftwareReleaseService extends RheaServiceAbstract {

    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    private SoftwareReleaseRepository softwareReleaseRepository;

    public SoftwareReleaseRepository getSoftwareReleaseRepository() {
        return softwareReleaseRepository;
    }

    public SoftwareRelease create(SoftwareRelease softwareRelease, String who) {
        Assert.notNull(softwareRelease, "software release is null");
        Assert.hasText(softwareRelease.getSoftwareProductId(), "softwareId is empty");
        Assert.hasText(who, "who is empty");

        String method = "create";
        logInfo(method, "...");

        // make sure the software release is not enabled because it was created
        // without a file
        softwareRelease.setEnabled(false);

        // persist
        softwareRelease = softwareReleaseRepository.doInsert(softwareRelease, who);

        softwareRelease = populateRefs(softwareRelease);

        getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.SoftwareRelease.CreateSoftwareRelease)
                .productName(ProductSystemNames.RHEA).objectId(softwareRelease.getId()).by(who));

        return softwareRelease;
    }

    public SoftwareRelease createWithFile(SoftwareRelease softwareRelease, String fileName, String contentType,
            byte[] content, String who) {
        Assert.notNull(softwareRelease, "software release is null");
        Assert.hasText(softwareRelease.getSoftwareProductId(), "softwareId is empty");
        Assert.hasText(who, "who is empty");
        Assert.hasText(fileName, "fileName is empty");
        Assert.hasText(contentType, "contentType is empty");
        Assert.notNull(content, "content is null");
        Assert.isTrue(content.length > 0, "content is empty");

        String method = "createWithFile";
        logInfo(method, "...");
        logInfo(method, "fileName: " + fileName + " contentType: " + contentType);

        if (fileName.indexOf("/") != -1) {
            fileName = fileName.substring(fileName.lastIndexOf("/"));
        }

        // persist
        softwareRelease = softwareReleaseRepository.doInsert(softwareRelease, who);

        // build fileStore
        FileStore fileStore = new FileStore().withCompanyId(softwareRelease.getCompanyId())
                .withCategory(ProductSystemNames.RHEA).withRelatedId(softwareRelease.getId())
                .withContentType(contentType).withName(fileName).withCreatedBy(who);

        // persist file store
        fileStore = fileStoreService.create(fileStore, content);

        // update softwareRelease with fileStoreId
        softwareRelease.setFileStoreId(fileStore.getId());
        softwareReleaseRepository.doSave(softwareRelease, who);

        softwareRelease = populateRefs(softwareRelease);

        getAuditLogService().save(AuditLogBuilder.create()
                .type(RheaAuditLog.SoftwareRelease.CreateSoftwareReleaseWithFirmware)
                .productName(ProductSystemNames.RHEA).objectId(softwareRelease.getId())
                .parameter("fileName", fileStore.getName()).parameter("fileContentType", fileStore.getContentType())
                .parameter("fileSize", fileStore.getSize() + "").parameter("fileMd5", fileStore.getMd5()).by(who));

        return softwareRelease;
    }

    public SoftwareRelease update(SoftwareRelease softwareRelease, String who) {
        Assert.notNull(softwareRelease, "software release is null");
        Assert.hasText(who, "who is empty");

        String method = "update";
        logInfo(method, "...");

        // persist
        softwareRelease = softwareReleaseRepository.doSave(softwareRelease, who);
        softwareRelease = populateRefs(softwareRelease);

        getAuditLogService().save(AuditLogBuilder.create().type(RheaAuditLog.SoftwareRelease.UpdateSoftwareRelease)
                .productName(ProductSystemNames.RHEA).objectId(softwareRelease.getId()).by(who));

        getRheaCacheService().clearSoftwareRelease(softwareRelease);

        return softwareRelease;
    }

    public SoftwareRelease updateWithFile(SoftwareRelease softwareRelease, String fileName, String contentType,
            byte[] content, String who) {
        Assert.notNull(softwareRelease, "software release is null");
        Assert.hasText(softwareRelease.getSoftwareProductId(), "softwareId is empty");
        Assert.hasText(who, "who is empty");
        Assert.hasText(fileName, "fileName is empty");
        Assert.hasText(contentType, "contentType is empty");
        Assert.notNull(content, "content is null");
        Assert.isTrue(content.length > 0, "content is empty");

        String method = "update";
        logInfo(method, "...");

        // persist
        softwareRelease = softwareReleaseRepository.doSave(softwareRelease, who);

        // lookup software release to ensure it is fully populated
        softwareRelease = softwareReleaseRepository.findById(softwareRelease.getId()).orElse(null);
        softwareRelease = populateRefs(softwareRelease);

        // capture existing file if has one
        FileStore deleteFile = null;
        if (softwareRelease.getRefFileStore() != null)
            deleteFile = softwareRelease.getRefFileStore();

        // build fileStore
        FileStore fileStore = new FileStore().withCompanyId(softwareRelease.getCompanyId())
                .withCategory(ProductSystemNames.RHEA).withRelatedId(softwareRelease.getId())
                .withContentType(contentType).withName(fileName).withCreatedBy(who);

        // persist file store
        fileStore = fileStoreService.create(fileStore, content);

        // update softwareRelease with fileStoreId
        softwareRelease.setFileStoreId(fileStore.getId());
        softwareReleaseRepository.doSave(softwareRelease, who);

        softwareRelease = populateRefs(softwareRelease);

        // build audit log
        AuditLogBuilder auditLog = AuditLogBuilder.create()
                .type(RheaAuditLog.SoftwareRelease.CreateSoftwareReleaseWithFirmware)
                .productName(ProductSystemNames.RHEA).objectId(softwareRelease.getId())
                .parameter("fileName", fileStore.getName()).parameter("fileContentType", fileStore.getContentType())
                .parameter("fileSize", fileStore.getSize() + "").parameter("fileMd5", fileStore.getMd5()).by(who);

        // delete old file and append to audit log
        if (deleteFile != null) {
            fileStoreService.delete(deleteFile);

            auditLog.parameter("oFileName", deleteFile.getName())
                    .parameter("oFileContentType", deleteFile.getContentType())
                    .parameter("oFileSize", deleteFile.getSize() + "").parameter("oFileMd5", deleteFile.getMd5());
        }

        // persist audit log
        getAuditLogService().save(auditLog);

        getRheaCacheService().clearSoftwareRelease(softwareRelease);

        return softwareRelease;
    }

    public SoftwareRelease populateRefs(SoftwareRelease softwareRelease) {
        if (softwareRelease != null) {
            if (softwareRelease.getRefCompany() == null && !StringUtils.isEmpty(softwareRelease.getCompanyId())) {
                softwareRelease.setRefCompany(getCoreCacheService().findCompanyById(softwareRelease.getCompanyId()));
            }

            if (softwareRelease.getRefSoftwareProduct() == null
                    && !StringUtils.isEmpty(softwareRelease.getSoftwareProductId())) {
                softwareRelease.setRefSoftwareProduct(
                        getRheaCacheService().findSoftwareProductById(softwareRelease.getSoftwareProductId()));
            }

            if (softwareRelease.getRefFileStore() == null && !StringUtils.isEmpty(softwareRelease.getFileStoreId())) {
                FileStoreSearchParams params = new FileStoreSearchParams();
                params.addIds(softwareRelease.getFileStoreId());

                List<FileStore> result = fileStoreService.findBy(params);
                if (result.size() == 1)
                    softwareRelease.setRefFileStore(result.get(0));
            }
        }

        return softwareRelease;
    }
}