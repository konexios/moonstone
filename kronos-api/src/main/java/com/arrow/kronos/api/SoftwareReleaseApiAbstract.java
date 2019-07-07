package com.arrow.kronos.api;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.data.BaseDeviceAbstract;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.service.SoftwareReleaseScheduleService;
import com.arrow.kronos.service.SoftwareReleaseTransService;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.DocumentAbstract;
import com.arrow.rhea.client.api.ClientCacheApi;
import com.arrow.rhea.data.SoftwareRelease;

public abstract class SoftwareReleaseApiAbstract extends BaseApiAbstract {

	// protected static final String DEVICE_CATEGORY_DEVICE = "DEVICE";
	// protected static final String DEVICE_CATEGORY_GATEWAY = "GATEWAY";

	@Autowired
	private SoftwareReleaseScheduleService softwareReleaseScheduleService;
	@Autowired
	private SoftwareReleaseTransService softwareReleaseTransService;
	@Autowired
	private ClientCacheApi clientCacheApi;

	public SoftwareReleaseScheduleService getSoftwareReleaseScheduleService() {
		return softwareReleaseScheduleService;
	}

	public SoftwareReleaseTransService getSoftwareReleaseTransService() {
		return softwareReleaseTransService;
	}

	public ClientCacheApi getClientCacheApi() {
		return clientCacheApi;
	}

	protected String findSoftwareReleaseId(String hid) {
		Assert.notNull(hid, "softwareReleaseHid is null");
		SoftwareRelease softwareRelease = clientCacheApi.findSoftwareReleaseByHid(hid);
		Assert.notNull(softwareRelease, "software release is not found");
		return softwareRelease.getId();
	}

	protected DocumentAbstract findObject(Function<String, DocumentAbstract> func, String id) {
		DocumentAbstract obj = func.apply(id);
		Assert.notNull(obj, "object is not found");
		return obj;
	}

	protected Function<String, DocumentAbstract> findByHid(AcnDeviceCategory category) {
		switch (category) {
		case DEVICE:
			return hid -> getKronosCache().findDeviceByHid(hid);
		case GATEWAY:
			return hid -> getKronosCache().findGatewayByHid(hid);
		default:
			throw new AcsLogicalException("Unsupported category! category=" + category.name());
		}
	}

	protected Function<String, DocumentAbstract> findById(AcnDeviceCategory category) {
		switch (category) {
		case DEVICE:
			return hid -> getKronosCache().findDeviceById(hid);
		case GATEWAY:
			return hid -> getKronosCache().findGatewayById(hid);
		default:
			throw new AcsLogicalException("Unsupported category! category=" + category.name());
		}
	}

	protected Function<String, AccessKey> validateAccessKey(AcnDeviceCategory category) {
		switch (category) {
		case DEVICE:
			return this::validateCanUpdateDevice;
		case GATEWAY:
			return this::validateCanWriteGateway;
		default:
			throw new AcsLogicalException("Unsupported category! category=" + category.name());
		}
	}

	protected boolean canWrite(AcnDeviceCategory category, AccessKey accessKey, BaseDeviceAbstract asset) {
		switch (category) {
		case DEVICE:
			Device device = (Device) asset;
			return accessKey.canWrite(accessKey.getRefApplication())
					|| accessKey.canWrite(getKronosCache().findGatewayById(device.getGatewayId()))
					|| accessKey.canWrite(device);
		case GATEWAY:
			return accessKey.canWrite(accessKey.getRefApplication()) || accessKey.canWrite(asset);
		default:
			throw new AcsLogicalException("Unsupported category! category=" + category.name());
		}
	}

	protected boolean canWrite(AcnDeviceCategory category, AccessKey accessKey, String objectId) {
		return canWrite(category, accessKey, (BaseDeviceAbstract) findById(category).apply(objectId));
	}

	protected boolean canWrite(SoftwareReleaseSchedule softwareReleaseSchedule, AccessKey accessKey) {
		return accessKey.canWrite(accessKey.getRefApplication()) || softwareReleaseSchedule.getObjectIds().stream()
				.allMatch(objectId -> canWrite(softwareReleaseSchedule.getDeviceCategory(), accessKey, objectId));
	}
}
