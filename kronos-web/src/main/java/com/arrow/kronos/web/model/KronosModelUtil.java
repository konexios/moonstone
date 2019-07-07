package com.arrow.kronos.web.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.web.model.DeviceActionModels.DeviceActionModel;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.CoreCacheService;
import com.arrow.pegasus.service.CryptoService;

@Component
public class KronosModelUtil {

	public static final String DEFAULT_LAST_MODIFIED_BY = "Unknown";
	public static final String LAST_MODIFIED_BY_ADMIN = "admin";

	@Autowired
	private CryptoService cryptoService;
	@Autowired
	private CoreCacheService coreCacheService;

	public List<TelemetryItemModels.TelemetryItemModel> populateTelemetryItemModels(
	        List<TelemetryItem> telemetryItems) {

		if (telemetryItems == null)
			return null;

		List<TelemetryItemModels.TelemetryItemModel> models = new ArrayList<>();
		for (TelemetryItem item : telemetryItems)
			models.add(populateTelemetryItemModel(item));

		return models;
	}

	public TelemetryItemModels.TelemetryItemModel populateTelemetryItemModel(TelemetryItem telemetryItem) {

		if (telemetryItem == null)
			return null;

		return new TelemetryItemModels.TelemetryItemModel(telemetryItem);
	}

	public List<DeviceActionModel> populateDeviceActionModels(List<DeviceAction> deviceActions) {

		if (deviceActions == null)
			return null;

		List<DeviceActionModel> models = new ArrayList<>(deviceActions.size());
		for (DeviceAction deviceAction : deviceActions)
			models.add(populateDeviceActionModel(deviceAction));

		return models;
	}

	public DeviceActionModel populateDeviceActionModel(DeviceAction deviceAction) {

		if (deviceAction == null)
			return null;

		return new DeviceActionModels.DeviceActionModel(deviceAction);
	}

	public String populateDecryptedLogin(User user) {
		if (user == null)
			return null;

		String result = null;
		try {
			result = cryptoService.getCrypto().internalDecrypt(user.getLogin());
		} catch (Throwable e) {
			throw new AcsLogicalException("Unable to decrypt user's login. User Id: " + user.getId(), e);
		}

		return result;
	}

	public String buildLastModifiedBy(String lastModifiedBy) {
		if (lastModifiedBy == null) {
			return null;
		}
		User user = coreCacheService.findUserById(lastModifiedBy);
		String result = lastModifiedBy;
		if (user != null && user.getContact() != null && user.getContact().fullName() != null) {
			result = user.getContact().fullName();
		} else if (!lastModifiedBy.equals(LAST_MODIFIED_BY_ADMIN)) {
			result = DEFAULT_LAST_MODIFIED_BY;
		}
		return result;
	}
}
