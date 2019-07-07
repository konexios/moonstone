package com.arrow.kronos.web.model;

import java.io.Serializable;

public class HomeWsModels {

	public static class HomePageOptions implements Serializable {

		private static final long serialVersionUID = -7697110174200805603L;

		private int refreshInterval;

		private SearchFilterModels.MyDevicesSearchFilterModel paginationDevice;
		private SearchFilterModels.MyGatewaySearchFilterModel paginationGateways;
		private SearchFilterModels.MyDeviceEventSearchFilterModel paginationDeviceEvents;

		public SearchFilterModels.MyDevicesSearchFilterModel getPaginationDevice() {
			return paginationDevice;
		}

		public SearchFilterModels.MyGatewaySearchFilterModel getPaginationGateways() {
			return paginationGateways;
		}

		public SearchFilterModels.MyDeviceEventSearchFilterModel getPaginationDeviceEvents() {
			return paginationDeviceEvents;
		}

		public void setPaginationDevice(SearchFilterModels.MyDevicesSearchFilterModel paginationDevice) {
			this.paginationDevice = paginationDevice;
		}

		public void setPaginationGateways(SearchFilterModels.MyGatewaySearchFilterModel paginationGateways) {
			this.paginationGateways = paginationGateways;
		}

		public void setPaginationDeviceEvents(SearchFilterModels.MyDeviceEventSearchFilterModel paginationDeviceEvents) {
			this.paginationDeviceEvents = paginationDeviceEvents;
		}

		public int getRefreshInterval() {
			return refreshInterval;
		}

		public void setRefreshInterval(int refreshInterval) {
			this.refreshInterval = refreshInterval;
		}

		@Override
		public String toString() {
			return "HomePageOptions [refreshInterval=" + refreshInterval + "]";
		}
	}

	public static final class HomePageModel implements Serializable {

		private static final long serialVersionUID = 3538856483574787170L;

		private SearchResultModels.MyDeviceSearchResultModel myDevices;
		private SearchResultModels.MyGatewaySearchResultModel myGateways;
		private SearchResultModels.MyDeviceEventSearchResultModel myDeviceEvents;

		public HomePageModel(SearchResultModels.MyDeviceSearchResultModel myDeviceSearchResultModel,
							 SearchResultModels.MyGatewaySearchResultModel myGateways,
							 SearchResultModels.MyDeviceEventSearchResultModel myDeviceEvents) {
			this.myDevices = myDeviceSearchResultModel;
			this.myGateways = myGateways;
			this.myDeviceEvents = myDeviceEvents;
		}

		public SearchResultModels.MyDeviceSearchResultModel getMyDevices() {
			return myDevices;
		}

		public SearchResultModels.MyGatewaySearchResultModel getMyGateways() {
			return myGateways;
		}

		public SearchResultModels.MyDeviceEventSearchResultModel getMyDeviceEvents() {
			return myDeviceEvents;
		}

		@Override
		public String toString() {
			return "HomePageModel [myDevices=" + myDevices + ", myGateways=" + myGateways + ", myDeviceEvents="
					+ myDeviceEvents + "]";
		}
	}
}
