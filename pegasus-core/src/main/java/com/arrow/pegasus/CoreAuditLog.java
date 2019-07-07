package com.arrow.pegasus;

public interface CoreAuditLog {

	public interface ApplicationEngine {
		public final static String APPLICATION_ENGINE_STARTED = "ApplicationEngineStarted";
		public final static String APPLICATION_ENGINE_STOPPED = "ApplicationEngineStopped";
	}

	public interface User {
		public final static String CREATE_USER = "CreateUser";
		public final static String UPDATE_USER = "UpdateUser";
		public final static String LOGIN_OK = "LoginOK";
		public final static String LOGIN_FAILED = "LoginFailed";
		public final static String DELETE_USER = "DeleteUser";
	}

	public interface Role {
		public final static String CREATE_ROLE = "CreateRole";
		public final static String UPDATE_ROLE = "UpdateRole";
		public final static String DELETE_ROLE = "DeleteRole";
	}

	public interface Region {
		public final static String CREATE_REGION = "CreateRegion";
		public final static String UPDATE_REGION = "UpdateRegion";
		public final static String DELETE_REGION = "DeleteRegion";
	}

	public interface Zone {
		public final static String CREATE_ZONE = "CreateZone";
		public final static String UPDATE_ZONE = "UpdateZone";
		public final static String DELETE_ZONE = "DeleteZone";
	}

	public interface Application {
		public final static String CREATE_APPLICATION = "CreateApplication";
		public final static String UPDATE_APPLICATION = "UpdateApplication";
		public final static String DELETE_APPLICATION = "DeleteApplication";
	}

	public interface Cache {
		public final static String CLEAR_ALL_CORE_CACHES = "ClearAllCoreCaches";
	}

	public interface Dashboard {
		public final static String CREATE_DASHBOARD = "CreateDashboard";
		public final static String UPDATE_DASHBOARD = "UpdateDashboard";
		public final static String DELETE_DASHBOARD = "DeleteDashboard";
	}

	public interface Widget {
		public final static String CREATE_WIDGET = "CreateWidget";
		public final static String UPDATE_WIDGET = "UpdateWidget";
		public final static String DELETE_WIDGET = "DeleteWidget";
	}

	public interface WidgetType {
		public final static String CREATE_WIDGET_TYPE = "CreateWidgetType";
		public final static String UPDATE_WIDGET_TYPE = "UpdateWidgetType";
		public final static String DELETE_WIDGET_TYPE = "DeleteWidgetType";
	}

	public interface MigrationTask {
		public final static String CREATE_COMPLETE = "CreateComplete";
		public final static String UPDATE_COMPLETE = "UpdateComplete";
		public final static String CREATE_ERROR = "CreateError";
		public final static String UPDATE_ERROR = "UpdateError";
	}

	public interface ProductExtension {
		public final static String CREATE_PRODUCT_EXTENSION = "CreateProductExtension";
		public final static String UPDATE_PRODUCT_EXTENSION = "UpdateProductExtension";
		public final static String DELETE_PRODUCT_EXTENSION = "DeleteProductExtension";
	}

	public interface ApplicationExtension {
		public final static String CREATE_APPLICATION_EXTENSION = "CreateApplicationExtension";
		public final static String UPDATE_APPLICATION_EXTENSION = "UpdateApplicationExtension";
	}

	public interface Auth {
		public final static String CREATE_AUTH = "CreateAuth";
		public final static String UPDATE_AUTH = "UpdateAuth";
		public final static String DELETE_AUTH = "DeleteAuth";
	}

	public interface Subscription {
		public final static String CREATE_SUBSCRIPTION = "CreateSubscription";
		public final static String UPDATE_SUBSCRIPTION = "UpdateSubscription";
		public final static String DELETE_SUBSCRIPTION = "DeleteSubscription";
	}

	public interface Company {
		public final static String CREATE_COMPANY = "CreateCompany";
		public final static String UPDATE_COMPANY = "UpdateCompany";
		public final static String DELETE_COMPANY = "DeleteCompany";
	}

	public interface SocialEvent {
		public final static String CREATE_SOCIAL_EVENT = "CreateSocialEvent";
		public final static String UPDATE_SOCIAL_EVENT = "UpdateSocialEvent";
		public final static String DELETE_SOCIAL_EVENT = "DeleteSocialEvent";
	}
}
