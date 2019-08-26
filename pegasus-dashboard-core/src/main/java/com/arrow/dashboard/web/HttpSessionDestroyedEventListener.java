package com.arrow.dashboard.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import com.arrow.dashboard.DashboardConstants;
import com.arrow.dashboard.runtime.BoardRuntimeManager;
import com.arrow.dashboard.runtime.WidgetRuntimeManager;
import com.arrow.dashboard.runtime.model.BoardRuntimeInstance;
import com.arrow.dashboard.runtime.model.WidgetRuntimeInstance;
import com.arrow.dashboard.runtime.model.WidgetStateChangeResponse;
import com.arrow.dashboard.web.model.session.SessionBoardRuntimeModel;

import moonstone.acs.Loggable;

@Component
public class HttpSessionDestroyedEventListener implements ApplicationListener<HttpSessionDestroyedEvent> {

	private Loggable logger = new Loggable() {
	};

	@Autowired
	private BoardRuntimeManager boardRuntimeManager;
	@Autowired
	private WidgetRuntimeManager widgetRuntimeManager;

	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event) {

		String method = "onApplicationEvent";
		logger.logDebug(method, "...");

		if (event.getSession() != null) {
			SessionBoardRuntimeModel sessionBoardRuntimeModel = (SessionBoardRuntimeModel) event.getSession()
			        .getAttribute(DashboardConstants.SessionAttribute.RUNTIME_BOARD);
			if (sessionBoardRuntimeModel != null) {
				logger.logDebug(method, "boardId: %s, boardRuntimeId: %s", sessionBoardRuntimeModel.getBoardId(),
				        sessionBoardRuntimeModel.getBoardRuntimeId());

				BoardRuntimeInstance boardRuntimeInstance = boardRuntimeManager
				        .getBoardRuntimeInstance(sessionBoardRuntimeModel.getBoardRuntimeId());
				if (boardRuntimeInstance != null) {
					String boardRuntimeId = boardRuntimeInstance.getBoardRuntimeId();
					logger.logDebug(method, "boardRuntimeId: %s", boardRuntimeId);

					logger.logDebug(method, "close board");
					// close board
					List<WidgetRuntimeInstance> widgetRuntimeInstances = boardRuntimeManager.closeBoard(boardRuntimeId);
					logger.logDebug(method, "# of widgets: %s", widgetRuntimeInstances.size());

					if (!widgetRuntimeInstances.isEmpty()) {
						// notify widgets
						String removeWidgetToBoardTopic = UrlUtils.withBoardRuntimeId(
						        DashboardConstants.Topic.BOARD_REMOVE_WIDGET, boardRuntimeInstance.getBoardRuntimeId());
						logger.logDebug(method, "Removing widgets from board. topic: %s", removeWidgetToBoardTopic);

						for (WidgetRuntimeInstance widgetRuntimeInstance : widgetRuntimeInstances) {

							// notify widget of destroy state
							WidgetStateChangeResponse notifyOnDestroyedResult = widgetRuntimeManager
							        .notifyOnDestroyed(widgetRuntimeInstance);
							if (notifyOnDestroyedResult.isNotified()) {
								logger.logDebug(method, "Removing widget from board. widgetId: %s, widgetRuntimeId: %s",
								        widgetRuntimeInstance.getWidgetId(),
								        widgetRuntimeInstance.getWidgetRuntimeId());
							} else {
								logger.logError(method, "NotifyOnDestroyed failed!",
								        notifyOnDestroyedResult.getError());
							}
						}
					}

					logger.logDebug(method, "unregister board");
					// unregister board
					boardRuntimeInstance = boardRuntimeManager.unregisterBoard(boardRuntimeId);
				}
			}
		}
	}
}
