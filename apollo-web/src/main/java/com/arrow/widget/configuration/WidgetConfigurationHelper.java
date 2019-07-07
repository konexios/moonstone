package com.arrow.widget.configuration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.arrow.dashboard.properties.PictureUrlPropertyView;
import com.arrow.dashboard.properties.bool.BooleanPropertyBuilder;
import com.arrow.dashboard.properties.bool.SimpleBooleanView;
import com.arrow.dashboard.properties.integer.ChoiceKeyValueIntegerPropertyView;
import com.arrow.dashboard.properties.integer.IntegerKeyValue;
import com.arrow.dashboard.properties.integer.IntegerPropertyBuilder;
import com.arrow.dashboard.properties.integer.SimpleIntegerView;
import com.arrow.dashboard.properties.string.ChoiceKeyValueStringPropertyView;
import com.arrow.dashboard.properties.string.StringKeyValue;
import com.arrow.dashboard.properties.string.StringPropertyBuilder;
import com.arrow.dashboard.widget.configuration.Configuration;
import com.arrow.dashboard.widget.configuration.Page;
import com.arrow.dashboard.widget.configuration.PageProperty;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceTelemetry;
import com.arrow.pegasus.data.SocialEvent;
import com.arrow.widget.WidgetConstants;

public class WidgetConfigurationHelper {

	public static PageProperty buildBasePageProperty(String name, String label, String description, boolean required) {
		Assert.hasText(name, "name is empty");
		Assert.hasText(label, "label is empty");

		// @formatter:off
		PageProperty pageProperty = new PageProperty().withName(name).withLabel(label).withRequired(required);
		// @formatter:on

		if (!StringUtils.isEmpty(description))
			pageProperty.withDescription(description);

		return pageProperty;
	}

	/**
	 * @param name
	 * @param label
	 * @param trueLabel
	 * @param falseLabel
	 * @param description
	 * @param required
	 * @param value
	 * @return
	 */
	public static PageProperty buildSimpleBooleanPageProperty(String name, String label, String trueLabel,
			String falseLabel, String description, boolean required, boolean value) {
		Assert.hasText(name, "name is empty");
		Assert.hasText(label, "label is empty");

		PageProperty pageProperty = buildBasePageProperty(name, label, description, required);

		// @formatter:off
		SimpleBooleanView view = new SimpleBooleanView();
		if (!StringUtils.isEmpty(trueLabel))
			view.withTrueLabel(trueLabel);
		if (!StringUtils.isEmpty(falseLabel))
			view.withFalseLabel(falseLabel);

		pageProperty.withProperty(new BooleanPropertyBuilder().withValue(value).withView(view).build());
		// @formatter:on

		return pageProperty;
	}

	public static PageProperty buildSimpleIntegerPageProperty(String name, String label, String description,
			boolean required, int value) {
		Assert.hasText(name, "name is empty");
		Assert.hasText(label, "label is empty");

		PageProperty pageProperty = buildBasePageProperty(name, label, description, required);

		// @formatter:off
		pageProperty
				.withProperty(new IntegerPropertyBuilder().withValue(value).withView(new SimpleIntegerView()).build());
		// @formatter:on

		return pageProperty;
	}

	public static PageProperty buildChoiceStringKeyValuePageProperty(String name, String label, String description,
			boolean required, List<StringKeyValue> options, String value) {
		Assert.hasText(name, "name is empty");
		Assert.hasText(label, "label is empty");
		Assert.notNull(options, "options is null");
		Assert.notEmpty(options, "options is empty");

		return buildChoiceStringKeyValuePageProperty(buildBasePageProperty(name, label, description, required), options,
				value);
	}

	public static PageProperty buildChoiceStringKeyValuePageProperty(PageProperty basePageProperty,
			List<StringKeyValue> options, String value) {
		Assert.notNull(basePageProperty, "basePageProperty is null");
		Assert.notNull(options, "options is null");
		Assert.notEmpty(options, "options is empty");

		// @formatter:off
		basePageProperty.withProperty(new StringPropertyBuilder().withValue(value)
				.withView(new ChoiceKeyValueStringPropertyView(options)).build());
		// @formatter:on

		return basePageProperty;
	}

	public static PageProperty buildPictureUrlStringPageProperty(PageProperty basePageProperty, String value) {
		Assert.notNull(basePageProperty, "basePageProperty is null");

		// @formatter:off
		basePageProperty.withProperty(
				new StringPropertyBuilder().withValue(value).withView(new PictureUrlPropertyView()).build());
		// @formatter:on

		return basePageProperty;
	}

	public static PageProperty buildChoiceIntegerKeyValuePageProperty(String name, String label, String description,
			boolean required, List<IntegerKeyValue> options, int value) {
		Assert.hasText(name, "name is empty");
		Assert.hasText(label, "label is empty");
		Assert.notNull(options, "options is null");
		Assert.notEmpty(options, "options is empty");

		return buildChoiceIntegerKeyValuePageProperty(buildBasePageProperty(name, label, description, required),
				options, value);
	}

	public static PageProperty buildChoiceIntegerKeyValuePageProperty(PageProperty basePageProperty,
			List<IntegerKeyValue> options, int value) {
		Assert.notNull(basePageProperty, "basePageProperty is null");
		Assert.notNull(options, "options is null");
		Assert.notEmpty(options, "options is empty");

		// @formatter:off
		basePageProperty.withProperty(new IntegerPropertyBuilder().withValue(value)
				.withView(new ChoiceKeyValueIntegerPropertyView(options)).build());
		// @formatter:on

		return basePageProperty;
	}

	// SETTINGS PAGE

	public static PageProperty buildRefreshRatePageProperty(int value) {
		return buildSimpleIntegerPageProperty(WidgetConstants.Property.Name.REFRESH_RATE,
				WidgetConstants.Property.Label.REFRESH_RATE, "Enter the number of seconds", true, value);
	}

	public static PageProperty buildMinValuePageProperty(int value) {
		return buildSimpleIntegerPageProperty(WidgetConstants.Property.Name.MIN_VALUE,
				WidgetConstants.Property.Label.MIN_VALUE, null, true, value);
	}

	public static PageProperty buildMaxValuePageProperty(int value) {
		return buildSimpleIntegerPageProperty(WidgetConstants.Property.Name.MAX_VALUE,
				WidgetConstants.Property.Label.MAX_VALUE, null, true, value);
	}

	public static Configuration buildSingleDeviceConfiguration(String configurationName, String configurationLabel,
			List<Device> devices, String value) {

		if (StringUtils.isEmpty(configurationName))
			configurationName = WidgetConstants.Configuration.Name.SINGLE_DEVICE;

		if (StringUtils.isEmpty(configurationLabel))
			configurationLabel = WidgetConstants.Configuration.Label.SINGLE_DEVICE;

		// @formatter:off
		Configuration configuration = new Configuration().withName(configurationName).withLabel(configurationLabel)
				.setChangedPage(0).setCurrentPage(0).addPage(buildSingleDeviceSelectionPage(devices, value));
		// @formatter:on

		return configuration;
	}

	public static Configuration buildErrorPageConfiguration(String configurationName, String configurationLabel,
			String pageLabel, String error) {

		if (StringUtils.isEmpty(configurationName))
			configurationName = WidgetConstants.Configuration.Name.DEFAULT;

		if (StringUtils.isEmpty(configurationLabel))
			configurationLabel = WidgetConstants.Configuration.Label.DEFAULT;

		// @formatter:off
		Configuration configuration = new Configuration().withName(configurationName).withLabel(configurationLabel)
				.setChangedPage(0).setCurrentPage(0).addPage(buildErrorPage(pageLabel, error));
		// @formatter:on

		return configuration;
	}
	
	public static Page buildSingleDeviceSelectionPage(List<Device> devices, String value) {

		Page page = new Page().withName(WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION)
				.withLabel(WidgetConstants.Page.Label.SINGLE_DEVICE_SELECTION);
		page.addProperty(buildSingleDeviceSelectionPageProperty(devices, value));

		return page;
	}
	
	public static Page buildErrorPage(String pageLabel, String error) {

		if (StringUtils.isEmpty(pageLabel))
			pageLabel = WidgetConstants.Page.Label.DEFAULT;
		
		Page page = new Page().withName(WidgetConstants.Page.Name.DEFAULT)
				.withLabel(pageLabel).withError(error);

		return page;
	}

	public static PageProperty buildUseTelemetryNamePageProperty(boolean value) {

		PageProperty pageProperty = buildSimpleBooleanPageProperty(WidgetConstants.Property.Name.USE_TELEMETRY_RAW_NAME,
				WidgetConstants.Property.Label.USE_TELEMETRY_RAW_NAME, "Raw Telemetry Name", "Readable Telemetry Name",
				null, true, value);

		return pageProperty;
	}

	public static PageProperty buildSingleDeviceSelectionPageProperty(List<Device> devices, String value) {

		PageProperty basePageProperty = buildBasePageProperty(WidgetConstants.Property.Name.DEVICE_UID,
				WidgetConstants.Property.Label.DEVICE, "Select a device", true);

		if (devices != null && !devices.isEmpty()) {

			List<StringKeyValue> deviceOptions = new ArrayList<>();
			for (Device device : devices)
				deviceOptions.add(new StringKeyValue(device.getUid(), device.getName() + " (" + device.getUid() + ")"));
			deviceOptions.sort(Comparator.comparing(StringKeyValue::getValue, String.CASE_INSENSITIVE_ORDER));

			basePageProperty = buildChoiceStringKeyValuePageProperty(basePageProperty, deviceOptions, value);
		} else {
			// TODO show property of sorts that indicates no devices are
			// available
		}

		return basePageProperty;
	}

	public static PageProperty buildSingleSocialEventSelectionPageProperty(List<SocialEvent> events, String value) {

		PageProperty basePageProperty = buildBasePageProperty(WidgetConstants.Property.Name.SOCIAL_EVENT_ID,
				WidgetConstants.Property.Label.SOCIAL_EVENT, "Select a social event", true);

		if (events != null && !events.isEmpty()) {

			List<StringKeyValue> deviceOptions = new ArrayList<>();
			for (SocialEvent event : events)
				deviceOptions.add(new StringKeyValue(event.getId(), event.getName() + " (" + event.getHid() + ")"));
			deviceOptions.sort(Comparator.comparing(StringKeyValue::getValue, String.CASE_INSENSITIVE_ORDER));

			basePageProperty = buildChoiceStringKeyValuePageProperty(basePageProperty, deviceOptions, value);
		} else {
			// TODO show property of sorts that indicates no social events are
			// available
		}

		return basePageProperty;
	}

	public static PageProperty buildTimeZomeSelectionPageProperty(String zoneId) {

		PageProperty basePageProperty = buildBasePageProperty(WidgetConstants.Property.Name.TIME_ZONE_ID,
				WidgetConstants.Property.Label.TIME_ZONE_ID, "Select a time zone", true);

		String[] ids = TimeZone.getAvailableIDs();
		List<StringKeyValue> timeZoneOptions = new ArrayList<>();
		String value = null;
		for (String id : ids) {
			TimeZone timeZone = TimeZone.getTimeZone(id);
			String propValue = timeZoneValue(timeZone);
			timeZoneOptions.add(new StringKeyValue(id, propValue));
			if (id.equals(zoneId)) {
				value = zoneId;
			}
		}

		basePageProperty = buildChoiceStringKeyValuePageProperty(basePageProperty, timeZoneOptions, value);
		return basePageProperty;
	}

	public static PageProperty buildSingleTelemetrySelectionPageProperty(List<DeviceTelemetry> deviceTelemetries,
			String value) {

		PageProperty basePageProperty = buildBasePageProperty(WidgetConstants.Property.Name.SINGLE_TELEMETRY_SELECTION,
				WidgetConstants.Property.Label.SINGLE_TELEMETRY_SELECTION, "Select telemetry", true);

		if (deviceTelemetries != null && !deviceTelemetries.isEmpty()) {
			List<StringKeyValue> telemetryOptions = new ArrayList<>();
			for (DeviceTelemetry deviceTelemetry : deviceTelemetries)
				telemetryOptions.add(new StringKeyValue(deviceTelemetry.getName(), deviceTelemetry.getDescription()));
			telemetryOptions.sort(Comparator.comparing(StringKeyValue::getValue, String.CASE_INSENSITIVE_ORDER));

			basePageProperty = buildChoiceStringKeyValuePageProperty(basePageProperty, telemetryOptions, value);
		} else {
			// TODO show property of sorts that indicates no telemetries are
			// available
		}

		return basePageProperty;
	}

	public static PageProperty buildPicUrlPageProperty(String value) {

		PageProperty basePageProperty = buildBasePageProperty(WidgetConstants.Property.Name.LOGO_URL,
				WidgetConstants.Property.Label.LOGO_URL, "Specify url of picture", true);

		basePageProperty = buildPictureUrlStringPageProperty(basePageProperty, value);

		return basePageProperty;
	}

	private static String timeZoneValue(TimeZone timeZone) {
		long hours = TimeUnit.MILLISECONDS.toHours(timeZone.getRawOffset());
		long minutes = TimeUnit.MILLISECONDS.toMinutes(timeZone.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours);
		minutes = Math.abs(minutes);

		String propValue = "";
		if (hours > 0) {
			propValue = String.format("(GMT+%d:%02d) %s", hours, minutes, timeZone.getID());
		} else {
			propValue = String.format("(GMT%d:%02d) %s", hours, minutes, timeZone.getID());
		}
		return propValue;
	}

}
