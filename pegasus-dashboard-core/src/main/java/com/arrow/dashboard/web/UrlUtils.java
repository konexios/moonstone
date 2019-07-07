package com.arrow.dashboard.web;

import org.apache.commons.lang3.StringUtils;

public class UrlUtils {

	public static String withEndpoint(String url, String endpoint) {
		String result = url;

		if (!StringUtils.isEmpty(endpoint))
			result = withReplace(result, "\\{endpoint\\}", endpoint);

		return result;
	}

	public static String withServiceId(String url, String serviceId) {
		String result = url;

		if (!StringUtils.isEmpty(serviceId))
			result = withReplace(result, "\\{serviceId\\}", serviceId);

		return result;
	}

	public static String withBoardRuntimeId(String url, String boardRuntimeId) {
		String result = url;

		if (!StringUtils.isEmpty(boardRuntimeId))
			result = withReplace(result, "\\{boardRuntimeId\\}", boardRuntimeId);

		return result;
	}

	public static String withWidgetRuntimeId(String url, String widgetId) {
		String result = url;

		if (!StringUtils.isEmpty(widgetId))
			result = withReplace(result, "\\{widgetRuntimeId\\}", widgetId);

		return result;
	}

	private static String withReplace(String url, String whatToReplace, String replaceWith) {
		String result = url;

		if (!StringUtils.isEmpty(replaceWith))
			result = result.replaceAll(whatToReplace, replaceWith);

		return result;
	}

	public static String fromControllerUrlToWidgetRuntimeUrl(String prefix, String controllerUrl,
	        String boardRuntimeId) {
		return fromControllerUrlToWidgetRuntimeUrl(prefix, controllerUrl, boardRuntimeId, null, null);
	}

	public static String fromControllerUrlToWidgetRuntimeUrl(String prefix, String controllerUrl, String boardRuntimeId,
	        String widgetId) {
		return fromControllerUrlToWidgetRuntimeUrl(prefix, controllerUrl, boardRuntimeId, widgetId, null);
	}

	public static String fromControllerUrlToWidgetRuntimeUrl(String prefix, String controllerUrl, String boardRuntimeId,
	        String widgetRuntimeId, String endpoint) {

		String result = controllerUrl;

		if (!StringUtils.isEmpty(boardRuntimeId))
			result = withBoardRuntimeId(result, boardRuntimeId);

		if (!StringUtils.isEmpty(widgetRuntimeId))
			result = withWidgetRuntimeId(result, widgetRuntimeId);

		if (!StringUtils.isEmpty(endpoint))
			result = withEndpoint(result, endpoint);

		return prefix + result;
	}

	public static String fromControllerUrlToServiceUrl(String prefix, String controllerUrl, String serviceId) {
		return prefix + withServiceId(controllerUrl, serviceId);
	}
}
