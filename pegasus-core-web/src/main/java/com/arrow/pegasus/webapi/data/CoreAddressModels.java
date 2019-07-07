package com.arrow.pegasus.webapi.data;

import java.io.Serializable;

import com.arrow.pegasus.data.profile.Address;

public class CoreAddressModels {

	public static class AddressModel implements Serializable {
		private static final long serialVersionUID = 3758274152444483162L;

		private String address1;
		private String address2;
		private String city;
		private String state;
		private String zip;
		private String country;

		public AddressModel() {
		}

		public AddressModel(Address address) {
			this.address1 = address.getAddress1();
			this.address2 = address.getAddress2();
			this.city = address.getCity();
			this.state = address.getState();
			this.zip = address.getZip();
			this.country = address.getCountry();
		}

		public String getAddress1() {
			return address1;
		}

		public String getAddress2() {
			return address2;
		}

		public String getCity() {
			return city;
		}

		public String getState() {
			return state;
		}

		public String getZip() {
			return zip;
		}

		public String getCountry() {
			return country;
		}
	}
}
