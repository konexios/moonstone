package com.arrow.pegasus.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.dashboard.data.WidgetType;
import com.arrow.pegasus.dashboard.repo.WidgetTypeRepository;
import com.arrow.pegasus.service.ServiceAbstract;

@Service
public class WidgetTypeService extends DashboardServiceAbstract {

	@Autowired
	private WidgetTypeRepository widgetTypeRepository;

	public WidgetTypeService() {
		super();
	}

	public WidgetTypeRepository getWidgetTypeRepository() {
		return widgetTypeRepository;
	}

	public WidgetType create(WidgetType widgetType, String who) {

		Assert.notNull(widgetType, "widgetType is null");
		Assert.hasText(who, "who is empty");

		return widgetTypeRepository.doInsert(widgetType, who);
	}

	public WidgetType update(WidgetType widgetType, String who) {
		Assert.notNull(widgetType, "WidgetType is null");
		Assert.hasText(who, "who is empty");

		return widgetTypeRepository.doSave(widgetType, who);
	}

	public void delete(WidgetType widgetType, String who) {
		Assert.notNull(widgetType, "WidgetType is null");
		// Assert.hasText(who, "who is empty");

		widgetTypeRepository.delete(widgetType);
	}
}