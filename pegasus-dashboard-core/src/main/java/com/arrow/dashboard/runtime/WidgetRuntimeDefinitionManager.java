package com.arrow.dashboard.runtime;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import com.arrow.dashboard.runtime.model.WidgetDimensionsRuntimeInstance;
import com.arrow.dashboard.runtime.model.WidgetRuntimeDefinition;
import com.arrow.dashboard.runtime.model.WidgetSizesRuntimeInstance;
import com.arrow.dashboard.widget.annotation.Widget;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.dashboard.data.WidgetDimensions;
import com.arrow.pegasus.dashboard.data.WidgetSizes;
import com.arrow.pegasus.dashboard.data.WidgetType;
import com.arrow.pegasus.dashboard.service.WidgetTypeService;

public class WidgetRuntimeDefinitionManager extends RuntimeManagerAbstract<WidgetRuntimeDefinition> {

	@Autowired
	private WidgetTypeService widgetTypeService;

	/**
	 * Method to scan classpath for widgets
	 */
	public void scan() {

		String method = "scan";
		logInfo(method, "...");
		logDebug(method, "Widget scanner start");

		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Widget.class));

		// TODO future enhancement, support other class paths
		scanner.findCandidateComponents("com.arrow.widget").forEach(this::loadWidgetRuntimeDefinition);
	}

	public List<WidgetRuntimeDefinition> getWidgetDefinitions() {
		return getRuntimeInstances();
	}

	public WidgetRuntimeDefinition getWidgetRuntimeDefinitionById(String widgetRuntimeDefinitionId) {
		return getRuntimeInstance(widgetRuntimeDefinitionId);
	}

	public boolean widgetRuntimeDefinitionExists(String widgetRuntimeDefinitionId) {
		return runtimeInstanceExists(widgetRuntimeDefinitionId);
	}

	private void loadWidgetRuntimeDefinition(BeanDefinition beanDefinition) {
		Assert.notNull(beanDefinition, "beanDefinition is null");

		String method = "loadWidgetRuntimeDefinition";
		logInfo(method, "...");

		String beanClassName = beanDefinition.getBeanClassName();
		logDebug(method, "found widget: %s", beanClassName);

		WidgetRuntimeDefinition widgetRuntimeDefinition = new WidgetRuntimeDefinition(beanDefinition);
		if (widgetRuntimeDefinition.hasError()) {
			logError(method, "Widget scanner skips widget definition for " + beanClassName + ", due to error: "
			        + widgetRuntimeDefinition.getException().getMessage());
		} else {
			WidgetType widgetType = widgetTypeService.getWidgetTypeRepository()
			        .findByClassName(widgetRuntimeDefinition.getWidgetClassName());

			if (widgetType == null)
				widgetType = create(widgetRuntimeDefinition);
			else
				widgetType = update(widgetType, widgetRuntimeDefinition);

			widgetRuntimeDefinition.setId(widgetType.getId());
			registerRuntimeInstance(widgetType.getId(), widgetRuntimeDefinition);
		}
	}

	private WidgetType create(WidgetRuntimeDefinition widgetRuntimeDefinition) {
		Assert.notNull(widgetRuntimeDefinition, "widgetRuntimeDefinition is null");

		String method = "create";
		logInfo(method, "...");

		WidgetType widgetType = new WidgetType();
		widgetType.setClassName(widgetRuntimeDefinition.getWidgetClass().getName());
		widgetType.setName(widgetRuntimeDefinition.getWidgetName());
		widgetType.setDescription(widgetRuntimeDefinition.getWidgetDescription());
		widgetType.setDirective(widgetRuntimeDefinition.getDirective());
		widgetType.setSizes(populateWidgetSizes(widgetRuntimeDefinition.getSizes()));

		widgetType = widgetTypeService.create(widgetType, CoreConstant.ADMIN_USER);

		return widgetType;
	}

	public WidgetType update(WidgetType widgetType, WidgetRuntimeDefinition widgetRuntimeDefinition) {
		Assert.notNull(widgetRuntimeDefinition, "widgetRuntimeDefinition is null");

		String method = "update";
		logInfo(method, "...");

		widgetType.setName(widgetRuntimeDefinition.getWidgetName());
		widgetType.setDescription(widgetRuntimeDefinition.getWidgetDescription());
		widgetType.setDirective(widgetRuntimeDefinition.getDirective());
		widgetType.setSizes(populateWidgetSizes(widgetRuntimeDefinition.getSizes()));

		widgetType = widgetTypeService.update(widgetType, CoreConstant.ADMIN_USER);

		return widgetType;
	}

	private WidgetSizes populateWidgetSizes(WidgetSizesRuntimeInstance widgetSizesRuntimeInstance) {

		WidgetSizes widgetSizes = new WidgetSizes();

		if (widgetSizesRuntimeInstance != null) {
			widgetSizes.setSmall(populateWidgetDimensions(widgetSizesRuntimeInstance.getSmall()));
			widgetSizes.setMedium(populateWidgetDimensions(widgetSizesRuntimeInstance.getMedium()));
			widgetSizes.setLarge(populateWidgetDimensions(widgetSizesRuntimeInstance.getLarge()));
			widgetSizes.setXtraLarge(populateWidgetDimensions(widgetSizesRuntimeInstance.getXtraLarge()));
		}

		return widgetSizes;
	}

	private WidgetDimensions populateWidgetDimensions(WidgetDimensionsRuntimeInstance widgetDimensionsRuntimeInstance) {
		if (widgetDimensionsRuntimeInstance == null)
			return null;

		return new WidgetDimensions(widgetDimensionsRuntimeInstance.getWidth(),
		        widgetDimensionsRuntimeInstance.getHeight());
	}
}
