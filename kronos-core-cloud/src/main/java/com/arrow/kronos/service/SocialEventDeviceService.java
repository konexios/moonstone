package com.arrow.kronos.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.SocialEventDevice;
import com.arrow.kronos.repo.SocialEventDeviceRepository;
import com.arrow.kronos.repo.SocialEventDeviceSearchParams;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;

import moonstone.acs.AcsLogicalException;
import moonstone.acs.AcsRuntimeException;

@Service
public class SocialEventDeviceService extends KronosServiceAbstract {

    @Autowired
    private SocialEventDeviceRepository socialEventDeviceRepository;

    public SocialEventDeviceRepository getSocialEventDeviceRepository() {
        return socialEventDeviceRepository;
    }

    public SocialEventDevice create(SocialEventDevice socialEventDevice, String who) {
        String method = "create";

        // logical checks
        if (socialEventDevice == null) {
            logInfo(method, "socialEventDevice is null");
            throw new AcsLogicalException("socialEventDevice is null");
        }

        if (StringUtils.isEmpty(who)) {
            logInfo(method, "who is empty");
            throw new AcsLogicalException("who is empty");
        }

        // insert
        socialEventDevice = socialEventDeviceRepository.doInsert(socialEventDevice, who);

        // write audit log
        getAuditLogService().save(AuditLogBuilder.create()
                .type(KronosAuditLog.SocialEventDevice.CreateSocialEventDevice).productName(ProductSystemNames.KRONOS)
                .objectId(socialEventDevice.getId()).by(who).parameter("macAddress", socialEventDevice.getMacAddress())
                .parameter("deviceTypeId", socialEventDevice.getDeviceTypeId()));

        return socialEventDevice;
    }

    public SocialEventDevice update(SocialEventDevice socialEventDevice, String who) {
        String method = "update";

        // logical checks
        if (socialEventDevice == null) {
            logInfo(method, "socialEventDevice is null");
            throw new AcsLogicalException("socialEventDevice is null");
        }

        if (StringUtils.isEmpty(who)) {
            logInfo(method, "who is empty");
            throw new AcsLogicalException("who is empty");
        }

        // update
        socialEventDevice = socialEventDeviceRepository.doSave(socialEventDevice, who);

        // write audit log
        getAuditLogService().save(AuditLogBuilder.create()
                .type(KronosAuditLog.SocialEventDevice.UpdateSocialEventDevice).productName(ProductSystemNames.KRONOS)
                .objectId(socialEventDevice.getId()).by(who).parameter("macAddress", socialEventDevice.getMacAddress())
                .parameter("deviceTypeId", socialEventDevice.getDeviceTypeId()));

        // clear cache
        SocialEventDevice cachedSocialEventDevice = getKronosCache()
                .findSocialEventDeviceById(socialEventDevice.getId());
        if (cachedSocialEventDevice != null) {
            getKronosCache().clearSocialEventDevice(cachedSocialEventDevice);
        }

        return socialEventDevice;
    }

    public SocialEventDevice populateRefs(SocialEventDevice socialEventDevice) {

        if (socialEventDevice != null) {
            if (socialEventDevice.getRefDeviceType() == null
                    && !StringUtils.isEmpty(socialEventDevice.getDeviceTypeId())) {
                socialEventDevice
                        .setRefDeviceType(getKronosCache().findDeviceTypeById(socialEventDevice.getDeviceTypeId()));
            }
        }

        return socialEventDevice;
    }

    public Page<SocialEventDevice> findSocialEventDevices(Pageable pageable, SocialEventDeviceSearchParams params) {
        return socialEventDeviceRepository.findSocialEventDevices(pageable, params);
    }

    public List<SocialEventDevice> findSocialEventDevices(SocialEventDeviceSearchParams params) {
        return socialEventDeviceRepository.findSocialEventDevices(params);
    }

    public List<String> importSpreadsheet(InputStream inputStream, String deviceTypeName, String who) {
        String method = "importSpreadsheet";
        Assert.notNull(inputStream, "inputStream is null");
        Assert.hasText(who, "who is empty");
        Assert.hasText(deviceTypeName, "deviceTypeName is empty");
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.rowIterator();
            // assume the 1st row is a header
            if (rows.hasNext()) {
                rows.next();
            }
            List<String> log = new ArrayList<>();
            int total = 0, skipped = 0;
            Pattern macAddressPattern = Pattern.compile("^([0-9A-Fa-f]{2}[:]){5}([0-9A-Fa-f]{2})$");
            while (rows.hasNext()) {
                Row row = rows.next();
                total++;
                Cell pinCodeCell = row.getCell(0); // 1st column - pinCode
                Cell macAddressCell = row.getCell(1); // 2nd column - macAddress
                if (pinCodeCell == null || macAddressCell == null) {
                    skipped++;
                    continue;
                }
                String pinCode = pinCodeCell.getStringCellValue();
                String macAddress = macAddressCell.getStringCellValue();
                logDebug(method, "pinCode: %s, macAddress: %s", pinCode, macAddress);
                if (!StringUtils.isNotBlank(pinCode) || !StringUtils.isNoneBlank(macAddress)) {
                    skipped++;
                    continue;
                }
                macAddress = macAddress.trim();
                pinCode = pinCode.trim();
                if (!macAddressPattern.matcher(macAddress).matches()) {
                    log.add("Row: " + row.getRowNum() + " - WARN: invalid MAC address=" + macAddress);
                }
                SocialEventDevice socialEventDevice = socialEventDeviceRepository
                        .findByDeviceTypeIdAndMacAddressAndPinCode(deviceTypeName, macAddress, pinCode);
                if (socialEventDevice != null) {
                    skipped++;
                    continue;
                }
                try {
                    socialEventDevice = new SocialEventDevice();
                    socialEventDevice.setMacAddress(macAddress);
                    socialEventDevice.setPinCode(pinCode);
                    socialEventDevice.setDeviceTypeId(deviceTypeName);
                    create(socialEventDevice, who);
                } catch (Exception e) {
                    log.add("Row: " + row.getRowNum() + " - WARN: " + e.getMessage());
                    skipped++;
                }
            }
            log.add("Total rows: " + total + ", skipped: " + skipped + ", inserted: " + (total - skipped));
            return log;
        } catch (EncryptedDocumentException | IOException e) {
            throw new AcsRuntimeException("Error reading spreadsheet", e);
        }
    }
}
