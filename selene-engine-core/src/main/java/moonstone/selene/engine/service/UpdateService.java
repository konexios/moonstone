package moonstone.selene.engine.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import moonstone.acn.client.api.SoftwareReleaseTransApi;
import moonstone.acn.client.model.SoftwareReleaseCommandModel;
import moonstone.acn.client.utils.MD5Util;
import moonstone.acs.client.model.DownloadFileInfo;
import moonstone.selene.Loggable;
import moonstone.selene.SeleneException;
import moonstone.selene.device.self.SelfModule;
import moonstone.selene.engine.DeviceModule;
import moonstone.selene.engine.DirectoryManager;

public class UpdateService extends Loggable {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final long DEFAULT_RESTART_SECONDS = 10;

    private static class SingletonHolder {
        static final UpdateService SINGLETON = new UpdateService();
    }

    public static UpdateService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    private Runnable task;

    private UpdateService() {
    }

    public void updateGatewaySoftware(SoftwareReleaseCommandModel model) {
        String method = "updateGatewaySoftware";
        if (task == null) {
            task = new UpdateGatewaySoftwareTask(model);
            new Thread(task).start();
        } else {
            logWarn(method, "UpdateGatewaySoftwareTask already running");
        }
    }

    public void updateDeviceSoftware(SoftwareReleaseCommandModel model, DeviceModule<?, ?, ?, ?> device) {
        new Thread(new UpdateDeviceSoftwareTask(model, device)).start();
    }

    File downloadFile(SoftwareReleaseCommandModel model, String deviceUid) throws Exception {
        String method = "downloadFile";
        SoftwareReleaseTransApi softwareReleaseTransApi = SelfModule.getInstance().getAcnClient()
                .getSoftwareReleaseTransApi();
        logInfo(method, "downloading file ...");
        DownloadFileInfo downloadInfo = softwareReleaseTransApi.downloadFile(model.getSoftwareReleaseTransHid(),
                model.getTempToken());
        logInfo(method, "downloaded fileName: %s, size: %d, tempFile: %s", downloadInfo.getFileName(),
                downloadInfo.getSize(), downloadInfo.getTempFile().getAbsolutePath());
        File downloadFolder = new File(DirectoryManager.getInstance().getDownload(), deviceUid);
        if (Files.exists(downloadFolder.toPath())) {
            if (!Files.isDirectory(downloadFolder.toPath())) {
                throw new SeleneException(String.format("file found instead of destination directory: %s",
                        downloadFolder.getAbsolutePath()));
            }
        } else {
            logInfo(method, "creation directory: '%s' for downloaded file ...", downloadFolder.getAbsolutePath());
            Files.createDirectory(downloadFolder.toPath());
        }
        File downloadFile = new File(downloadFolder,
                String.format("%s.%s", downloadInfo.getFileName(), SDF.format(new Date())));
        Files.move(downloadInfo.getTempFile().toPath(), downloadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        logInfo(method, "downloaded file renamed to: %s", downloadFile.getPath());
        checkMD5Sum(downloadFile, model.getMd5checksum());
        return downloadFile;
    }

    void upgradeGatewaySoftware(File file) throws IOException {
        String method = "updateGatewaySoftware";
        Validate.notNull(file, "file is null");
        logInfo(method, "updating new library ...");
        Files.copy(file.toPath(), new File("lib", file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
        logInfo(method, "software updated successfully");
    }

    void backupGatewaySoftware(File file, String timestamp) throws IOException {
        String method = "backupGatewaySoftware";
        Validate.notNull(file, "file is null");
        if (new File(DirectoryManager.getInstance().getLib(), file.getName()).exists()) {
            File backupFile = new File(DirectoryManager.getInstance().getBackup(),
                    String.format("%s.%s", file.getName(), timestamp));
            logInfo(method, "backing up old file to: %s ...", backupFile.getPath());
            Files.copy(file.toPath(), backupFile.toPath());
            logInfo(method, "old file backed up successfully");
        }
    }

    private void checkMD5Sum(File file, String md5Checksum) throws NoSuchAlgorithmException, IOException {
        String method = "checkMD5Sum";
        Validate.notNull(file, "file is null");
        Validate.notEmpty(md5Checksum, "md5Checksum is not defined");
        logInfo(method, "checking MD5 checksum ...");
        if (Objects.equals(MD5Util.calcMD5ChecksumString(file), md5Checksum)) {
            logInfo(method, "MD5 checksum is correct");
        } else {
            file.delete();
            throw new SeleneException("MD5 checksum is incorrect, removing the file downloaded");
        }
    }

    private class UpdateGatewaySoftwareTask implements Runnable {
        SoftwareReleaseCommandModel model;

        UpdateGatewaySoftwareTask(SoftwareReleaseCommandModel model) {
            this.model = model;
        }

        @Override
        public void run() {
            String method = "run";
            SoftwareReleaseTransApi softwareReleaseTransApi = SelfModule.getInstance().getAcnClient()
                    .getSoftwareReleaseTransApi();
            try {
                softwareReleaseTransApi.received(model.getSoftwareReleaseTransHid());
                String timestamp = SDF.format(new Date());
                File downloadFile = downloadFile(model, timestamp);
                backupGatewaySoftware(downloadFile, timestamp);
                upgradeGatewaySoftware(downloadFile);
                softwareReleaseTransApi.succeeded(model.getSoftwareReleaseTransHid());

                // terminate program
                logInfo(method, "Selene will be restarted in %d seconds ...", DEFAULT_RESTART_SECONDS);
                Thread.sleep(DEFAULT_RESTART_SECONDS * 1000L);
                new Thread(() -> SelfModule.getInstance().shutdown()).start();
            } catch (Exception e) {
                logError(method, e);
                softwareReleaseTransApi.failed(model.getSoftwareReleaseTransHid(), e.getMessage());
            }
        }
    }

    private class UpdateDeviceSoftwareTask implements Runnable {
        SoftwareReleaseCommandModel model;
        DeviceModule<?, ?, ?, ?> device;

        UpdateDeviceSoftwareTask(SoftwareReleaseCommandModel model, DeviceModule<?, ?, ?, ?> device) {
            this.model = model;
            this.device = device;
        }

        @Override
        public void run() {
            String method = "run";
            SoftwareReleaseTransApi softwareReleaseTransApi = SelfModule.getInstance().getAcnClient()
                    .getSoftwareReleaseTransApi();
            try {
                softwareReleaseTransApi.received(model.getSoftwareReleaseTransHid());
                device.upgradeDeviceSoftware(downloadFile(model, device.getInfo().getUid()));
                softwareReleaseTransApi.succeeded(model.getSoftwareReleaseTransHid());
            } catch (Exception e) {
                logError(method, e);
                softwareReleaseTransApi.failed(model.getSoftwareReleaseTransHid(), e.getMessage());
            }
        }
    }
}
