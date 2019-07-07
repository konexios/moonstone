package com.arrow.kronos.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acn.client.model.TelemetryItemModel;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.EsTelemetryItem;
import com.arrow.kronos.repo.SpringDataEsTelemetryItemRepository;

@Service
public class SpringDataEsTelemetryItemService extends KronosServiceAbstract {

	@Autowired
	private SpringDataEsTelemetryItemRepository itemRepository;

	public SpringDataEsTelemetryItemRepository getItemRepository() {
		return itemRepository;
	}

	public List<TelemetryItemModel> populate(List<EsTelemetryItem> list) {
		if (list == null || list.size() == 0) {
			return Collections.emptyList();
		}
		List<TelemetryItemModel> result = new ArrayList<>();
		Map<String, Device> cache = new HashMap<>();
		for (EsTelemetryItem item : list) {
			TelemetryItemModel model = item.toModel();
			String deviceId = item.getDeviceId();
			Device device = cache.get(deviceId);
			if (device == null) {
				cache.put(deviceId, device = getKronosCache().findDeviceById(deviceId));
			}
			model.setDeviceHid(device.getHid());
			result.add(model);
		}
		return result;
	}

	public void deleteBy(Device device) {
		String method = "deleteBy";
		Assert.notNull(device, "device is null");
		long numDeleted = itemRepository.deleteByDeviceId(device.getId());
		logInfo(method, "Elastic search telemetry items have been deleted for device id=" + device.getId() + ", total "
		        + numDeleted);
	}

	public long deleteByApplicationId(String applicationId) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		long numDeleted = itemRepository.deleteByApplicationId(applicationId);
		logInfo(method, "Elastic search telemetry items have been deleted for aplicationId: %s, numDeleted: %d",
		        applicationId, numDeleted);
		return numDeleted;
	}

	public List<String> deleteAll(int numDaysToKeep) {
		String method = "deleteAll";
		Assert.isTrue(numDaysToKeep >= 0, "numDaysToKeep must be positive");
		List<String> result = new ArrayList<>();

		Instant date = Instant.now().truncatedTo(ChronoUnit.DAYS).minus(numDaysToKeep, ChronoUnit.DAYS);
		logDebug(method, "Delete telemetry items older than %s", date);

		long count = itemRepository.deleteByTimestampLessThan(date.toEpochMilli());
		logDebug(method, "Telemetry items deleted: %s", count);

		result.add("Telemetry items deleted: " + count);
		return result;
	}
}
