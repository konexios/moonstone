package com.arrow.rhea;

public class RheaCoreConstants {
	public final static String RHEA_PRI = "rha";

	public interface RheaPri {
		public final static String DEVICE_MANUFACTURER = "dvc-mnf";
		public final static String DEVICE_PRODUCT = "dvc-pdt";
		public final static String DEVICE_CATEGORY = "dvc-cat";
		public final static String DEVICE_TYPE = "dvc-typ";
		public final static String SOFTWARE_VENDOR = "sft-vdr";
		public final static String SOFTWARE_PRODUCT = "sft-pdt";
		public final static String SOFTWARE_RELEASE = "sft-rls";
		public final static String RTU_REQUEST = "rtu-rqt";
	}

	public interface PageResult {
		public final static int DEFAULT_PAGE = 0;
		public final static int DEFAULT_SIZE = 100;
		public final static int MAX_SIZE = 200;
	}
}
