package com.arrow.rhea;

public interface RheaAuditLog {

	public interface Api {
		public final static String ApiStarted = "ApiStarted";
		public final static String ApiStopped = "ApiStopped";
	}

	public interface DeviceManufacturer {
		public final static String CreateDeviceManufacturer = "CreateManufacturer";
		public final static String UpdateDeviceManufacturer = "UpdateManufacturer";
	}

	public interface DeviceProduct {
		public final static String CreateDeviceProduct = "CreateDeviceProduct";
		public final static String UpdateDeviceProduct = "UpdateDeviceProduct";
	}

	public interface DeviceCategory {
		public final static String CreateDeviceCategory = "CreateDeviceCategory";
		public final static String UpdateDeviceCategory = "UpdateDeviceCategory";
	}

	public interface DeviceType {
		public final static String CreateDeviceType = "CreateDeviceType";
		public final static String UpdateDeviceType = "UpdateDeviceType";
	}

	public interface SoftwareVendor {
		public final static String CreateSoftwareVendor = "CreateSoftwareVendor";
		public final static String UpdateSoftwareVendor = "UpdateSoftwareVendor";
	}

	public interface SoftwareProduct {
		public final static String CreateSoftwareProduct = "CreateSoftwareProduct";
		public final static String UpdateSoftwareProduct = "UpdateSoftwareProduct";
	}

	public interface SoftwareRelease {
		public final static String CreateSoftwareRelease = "CreateSoftwareRelease";
		public final static String UpdateSoftwareRelease = "UpdateSoftwareRelease";
		public final static String CreateSoftwareReleaseWithFirmware = "CreateSoftwareReleaseWithFirmware";
		public final static String UpdateSoftwareReleaseWithFirmware = "UpdateSoftwareReleaseWithFirmware";
	}
	
	public interface RTURequest {
		public final static String CreateRTURequest = "CreateRTURequest";
		public final static String UpdateRTURequest = "UpdateRTURequest";
	}
}