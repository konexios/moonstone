package com.arrow.dashboard.web.model.runtime;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

import com.arrow.dashboard.DashboardConstants;
import com.arrow.dashboard.runtime.model.MessageEndpoint;
import com.arrow.dashboard.runtime.model.WidgetRuntimeInstance;
import com.arrow.dashboard.web.UrlUtils;

public class ConvertRuntimeModelUtils {

	/**
	 * Method to build {@link WidgetRuntimeModels.WidgetRuntimeModel} from
	 * {@link WidgetRuntimeInstance} <br>
	 * widget runtime contains 'communication protocol definition' for the
	 * widget instance<br>
	 * Widget runtime object is used by front end to send messages to back end,
	 * so urls depends on controller configuration, ids of widget and dashboard
	 * and system messaging settings
	 * 
	 * @return
	 */
	public static WidgetRuntimeModels.WidgetRuntimeModel toWidgetRuntimeModel(String boardId,
	        WidgetRuntimeInstance widgetRuntimeInstance) {
		Assert.hasText(boardId, "boardId is empty");
		Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

		String widgetId = widgetRuntimeInstance.getWidgetRuntimeId();
		WidgetRuntimeModels.WidgetRuntimeModel model = new WidgetRuntimeModels.WidgetRuntimeModel();

		// message endpoints
		Map<String, String> messageEndpoints = widgetRuntimeInstance.getWidgetRuntimeDefinition().getMessageEndpoints()
		        .stream().collect(Collectors.toMap(me -> "/" + me.getEndpoint(), me -> {
			        if (me.isParametrizied()) {
				        return UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.APPLICATION_PREFIX,
		                        DashboardConstants.Url.PROCESS_WIDGET_MESSAGE_WITH_PARAMETERS, boardId, widgetId,
		                        me.getEndpoint());
			        } else {
				        return UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.APPLICATION_PREFIX,
		                        DashboardConstants.Url.PROCESS_WIDGET_MESSAGE_NO_PARAMETERS, boardId, widgetId,
		                        me.getEndpoint());
			        }
		        },(p1,p2) -> p1));

		// topic providers
		Map<String, String> topicProviders = widgetRuntimeInstance.getWidgetRuntimeDefinition().getMessageEndpoints()
		        .stream().filter(MessageEndpoint::isAnswering).map(MessageEndpoint::getTopicProvider).collect(
		                Collectors.toMap(tp -> "/" + tp.getTopic(),
		                        tp -> UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.MESSAGE_BROKER,
		                                DashboardConstants.Topic.WIDGET_OUTBOUND_MESSAGE, boardId, widgetId,
		                                tp.getTopic()),(p1,p2) -> p1));
		topicProviders
		        .putAll(widgetRuntimeInstance.getWidgetRuntimeDefinition().getFieldTopicProviders().stream()
		                .collect(Collectors.toMap(tp -> "/" + tp.getTopic(),
		                        tp -> UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.MESSAGE_BROKER,
		                                DashboardConstants.Topic.WIDGET_OUTBOUND_MESSAGE, boardId, widgetId,
		                                tp.getTopic()),(p1,p2) -> p1)));

		// @formatter:off
		model.withId(widgetId)
			.withWidgetTypeId(widgetRuntimeInstance.getWidgetRuntimeDefinition().getId())
			.withParentId(widgetRuntimeInstance.getParentId())
			.withDirective(widgetRuntimeInstance.getWidgetRuntimeDefinition().getDirective())
			.withLayout(widgetRuntimeInstance.getLayout())
			.withSocketEndpoint(DashboardConstants.MESSAGE_ENDPOINT)
			.withConfigurationInit(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.APPLICATION_PREFIX,
					DashboardConstants.Url.INITIALIZE_WIDGET_CONFIGURATION, boardId, widgetId))
			.withConfigurationProcess(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.APPLICATION_PREFIX,
					DashboardConstants.Url.PROCESS_INBOUND_WIDGET_CONFIGURATION, boardId, widgetId))
			.withSaveConfiguration(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.APPLICATION_PREFIX,
					DashboardConstants.Url.SAVE_INBOUND_WIDGET_CONFIGURATION, boardId, widgetId))
			.withFastConfigurationInit(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.APPLICATION_PREFIX,
					DashboardConstants.Url.INITIALIZE_WIDGET_FAST_CONFIGURATION, boardId, widgetId))
			.withFastConfigurationProcess(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.APPLICATION_PREFIX,
					DashboardConstants.Url.PROCESS_INBOUND_WIDGET_FAST_CONFIGURATION, boardId, widgetId))
			.withMetaDataUpdate(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.APPLICATION_PREFIX,
					DashboardConstants.Topic.UPDATE_WIDGET_META_DATA, boardId, widgetId))
			.withConfigurationTopic(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.MESSAGE_BROKER,
					DashboardConstants.Topic.WIDGET_INBOUND_CONFIGURATION, boardId, widgetId))
			.withFastConfigurationTopic(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.MESSAGE_BROKER,
					DashboardConstants.Topic.WIDGET_INBOUND_FAST_CONFIGURATION, boardId, widgetId))
			.withMetaDataUpdateTopic(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.MESSAGE_BROKER,
					DashboardConstants.Topic.UPDATE_WIDGET_META_DATA, boardId, widgetId))
			.withMessageEndpoints(messageEndpoints)
			.withTopicProviders(topicProviders)

			.withEditWidgetUrl(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.APPLICATION_PREFIX,
					DashboardConstants.Url.EDIT_WIDGET, boardId, widgetId))
			.withUpdateWidgetUrl(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.APPLICATION_PREFIX,
					DashboardConstants.Url.UPDATE_WIDGET, boardId, widgetId))
			.withCancelEditWidgetUrl(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.APPLICATION_PREFIX,
					DashboardConstants.Url.CANCEL_EDIT_WIDGET, boardId, widgetId))
			
			.withDeleteUrl(UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.APPLICATION_PREFIX,
					DashboardConstants.Url.BOARD_REMOVE_WIDGET, boardId, widgetId))
			.withWidgetMetaData(new WidgetRuntimeModels.WidgetMetaData()
					.withName(widgetRuntimeInstance.getName())
					.withDescription(widgetRuntimeInstance.getDescription()));
		// @formatter:on

		return model;
	}
}
