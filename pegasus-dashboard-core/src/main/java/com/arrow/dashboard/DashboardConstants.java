package com.arrow.dashboard;

public interface DashboardConstants {

	public static final String MESSAGE_BROKER = "/topic";
	public static final String APPLICATION_PREFIX = "/app";
	public static final String MESSAGE_ENDPOINT = "/endpoint";
	public static final String URL_BASE = "/acs/dd/runtime";
	public static final String URL_BOARDS_BASE = URL_BASE + "/boards";

	public interface SessionAttribute {
		public static final String HTTP_SESSION_ID_ATTRIBUTE = "com.arrow.dashboard.web.controller.sessionId";
		public static final String RUNTIME_BOARD = "com.arrow.dashboard.web.runtime.board";
	}

	public interface Url {
		// core board runtime controller
		public static final String REGISTER_BOARD = URL_BOARDS_BASE + "/register";
		public static final String OPEN_BOARD = URL_BOARDS_BASE + "/open";
		public static final String CLOSE_BOARD = URL_BOARDS_BASE + "/close";
		public static final String UNREGISTER_BOARD = URL_BOARDS_BASE + "/unregister";
		public static final String BOARD_ADD_WIDGET = URL_BOARDS_BASE + "/{boardRuntimeId}/widgets/add";
		public static final String BOARD_REMOVE_WIDGET = URL_BOARDS_BASE + "/{boardRuntimeId}/widgets/remove";
		public static final String BOARD_UPDATE_WIDGET = URL_BOARDS_BASE + "/{boardRuntimeId}/widgets/update";
		public static final String BOARD_UPDATE_LAYOUT = URL_BOARDS_BASE + "/{boardRuntimeId}/layout/update";

		// core widget runtime controller
		/**
		 * Activation url used to register a new WMService<br>
		 * WMServices are not tracked by the application. this is just a way to
		 * divide messages NOTE: activation performed by http GET request, not
		 * via web sockets
		 */
		public static final String URL_WIDGET_CONTROLLER_ACTIVATION = "/widgetcontroller/activate/";

		public static final String NEW_WIDGET_REQUEST = URL_BOARDS_BASE + "/widgets/new";
		public static final String REGISTER_WIDGET_TO_PARENT_REQUEST = URL_BOARDS_BASE + "/widgets/register";
		public static final String CANCEL_NEW_WIDGET_REQUEST = URL_BOARDS_BASE + "/widgets/cancel";

		public static final String URL_WIDGET_CONTROLLER_ADD_WIDGET = "/widgetcontroller/dashboard/{boardRuntimeId}/add";

		public static final String CHANGE_WIDGET_STATE_TO_CREATE = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/notify/create";
		public static final String CHANGE_WIDGET_STATE_TO_LOADING = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/notify/loading";
		public static final String CHANGE_WIDGET_STATE_TO_READY = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/notify/ready";
		public static final String CHANGE_WIDGET_STATE_TO_RUNNING = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/notify/running";
		public static final String CHANGE_WIDGET_STATE_TO_STOPPED = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/notify/stopped";
		public static final String CHANGE_WIDGET_STATE_TO_ERROR = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/notify/error";
		public static final String CHANGE_WIDGET_STATE_TO_CORRECTION = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/notify/correction";
		public static final String CHANGE_WIDGET_STATE_TO_DESTROYED = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/notify/destroyed";
		public static final String PROCESS_WIDGET_MESSAGE_WITH_PARAMETERS = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/message/{endpoint}";
		public static final String PROCESS_WIDGET_MESSAGE_NO_PARAMETERS = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/message/empty/{endpoint}";
		public static final String INITIALIZE_WIDGET_CONFIGURATION = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/configuration/init";
		public static final String PROCESS_INBOUND_WIDGET_CONFIGURATION = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/configuration/process";
		public static final String SAVE_INBOUND_WIDGET_CONFIGURATION = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/configuration/save";
		public static final String INITIALIZE_WIDGET_FAST_CONFIGURATION = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/configuration/fast/init";
		public static final String PROCESS_INBOUND_WIDGET_FAST_CONFIGURATION = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/configuration/fast/process";

		public static final String EDIT_WIDGET = URL_BOARDS_BASE + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/edit";
		public static final String UPDATE_WIDGET = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/update";
		public static final String CANCEL_EDIT_WIDGET = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/cancel";
	}

	public interface Topic {
		// core board runtime controller
		public static final String BOARD_REGISTERED = MESSAGE_BROKER + URL_BOARDS_BASE + "/registered";
		public static final String BOARD_OPENED = MESSAGE_BROKER + URL_BOARDS_BASE + "/opened";
		public static final String BOARD_CLOSED = MESSAGE_BROKER + URL_BOARDS_BASE + "/closed";
		public static final String BOARD_UNREGISTERED = MESSAGE_BROKER + URL_BOARDS_BASE + "/unregistered";
		public static final String BOARD_ADD_WIDGET = MESSAGE_BROKER + Url.BOARD_ADD_WIDGET;
		public static final String BOARD_REMOVE_WIDGET = MESSAGE_BROKER + Url.BOARD_REMOVE_WIDGET;
		public static final String BOARD_UPDATE_WIDGET = MESSAGE_BROKER + Url.BOARD_UPDATE_WIDGET;

		// core widget runtime controller
		public static final String TOPIC_WIDGET_CONTROLLER_OUTGOING_NEW_DASHBOARD_MESSAGE = "/widgetcontroller/{serviceId}/dashboard/new";
		public static final String TOPIC_WIDGET_CONTROLLER_CREATE_WIDGET = MESSAGE_BROKER + Url.NEW_WIDGET_REQUEST;
		public static final String TOPIC_WIDGET_CONTROLLER_ACCEPT_WIDGET = MESSAGE_BROKER
		        + Url.REGISTER_WIDGET_TO_PARENT_REQUEST;
		public static final String TOPIC_WIDGET_CONTROLLER_ADD_WIDGET = "/widgetcontroller/dashboard/{boardRuntimeId}/instance";

		// widget runtime
		public static final String WIDGET_INBOUND_CONFIGURATION = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/configuration";
		public static final String WIDGET_INBOUND_FAST_CONFIGURATION = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/configuration/fast";
		public static final String UPDATE_WIDGET_META_DATA = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/metadata/update";
		public static final String WIDGET_OUTBOUND_MESSAGE = URL_BOARDS_BASE
		        + "/{boardRuntimeId}/widgets/{widgetRuntimeId}/{endpoint}";
	}
}
