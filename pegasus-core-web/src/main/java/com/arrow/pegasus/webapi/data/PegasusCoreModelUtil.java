package com.arrow.pegasus.webapi.data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.data.profile.Address;
import com.arrow.pegasus.data.profile.Contact;
import com.arrow.pegasus.data.profile.LoginPolicy;
import com.arrow.pegasus.data.profile.PasswordPolicy;
import com.arrow.pegasus.web.model.AddressModel;
import com.arrow.pegasus.web.model.ContactModel;
import com.arrow.pegasus.web.model.LoginPolicyModel;
import com.arrow.pegasus.web.model.PasswordPolicyModel;

@Component
public class PegasusCoreModelUtil {

	public final static DateTimeFormatter MM_DD_YYYY = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	public final static DateTimeFormatter MM_DD_YYYY_HH_MM_MERIDIAN = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

	public LocalDate toLocalDate(String date, DateTimeFormatter formatter) {

		if (StringUtils.isEmpty(date) || formatter == null)
			return null;

		return LocalDate.parse(date, formatter);
	}

	public LocalDate toISOLocalDate(String date) {

		return toLocalDate(date, DateTimeFormatter.ISO_LOCAL_DATE);
	}

	public Instant toInstant(String date, DateTimeFormatter formatter) {

		if (StringUtils.isEmpty(date) || formatter == null)
			return null;

		return formatter.parse(date, ZonedDateTime::from).toInstant();
	}

	public String toString(LocalDate date, DateTimeFormatter formatter) {
		if (date == null || formatter == null)
			return null;

		return formatter.format(date);
	}

	public String toString(Instant date, DateTimeFormatter formatter) {
		if (date == null || formatter == null)
			return null;

		return formatter.withZone(ZoneOffset.UTC).format(date);
	}

	public String toISOLocalDateString(Instant date) {
		return toString(date, DateTimeFormatter.ISO_LOCAL_DATE);
	}

	public ContactModel toContactModel(Contact contact) {
		if (contact == null)
			return null;

		ContactModel model = new ContactModel().withFirstName(contact.getFirstName())
		        .withLastName(contact.getLastName()).withSipUri(contact.getSipUri()).withEmail(contact.getEmail())
		        .withHome(contact.getHome()).withOffice(contact.getOffice()).withCell(contact.getCell())
		        .withFax(contact.getFax()).withMonitorExt(contact.getMonitorExt());

		return model;
	}

	public Contact toContact(ContactModel model) {
		return toContact(model, new Contact());
	}

	public Contact toContact(ContactModel model, Contact contact) {
		if (model == null)
			return null;

		if (contact == null)
			contact = new Contact();

		contact.setFirstName(model.getFirstName());
		contact.setLastName(model.getLastName());
		contact.setSipUri(model.getSipUri());
		contact.setEmail(model.getEmail());
		contact.setHome(model.getHome());
		contact.setOffice(model.getOffice());
		contact.setCell(model.getCell());
		contact.setFax(model.getFax());
		contact.setMonitorExt(model.getMonitorExt());

		return contact;
	}

	public Contact populateContact(CoreContactModels.ContactModel model) {
		if (model == null)
			return null;

		Contact contact = new Contact();
		contact.setFirstName(model.getFirstName());
		contact.setLastName(model.getLastName());
		contact.setSipUri(model.getSipUri());
		contact.setEmail(model.getEmail());
		contact.setHome(model.getHome());
		contact.setOffice(model.getOffice());
		contact.setCell(model.getCell());
		contact.setFax(model.getFax());
		contact.setMonitorExt(model.getMonitorExt());

		return contact;
	}

	public AddressModel toAddressModel(Address address) {
		if (address == null)
			return null;

		AddressModel model = new AddressModel().withAddress1(address.getAddress1()).withAddress2(address.getAddress2())
		        .withCity(address.getCity()).withState(address.getState()).withZip(address.getZip())
		        .withCountry(address.getCountry());

		return model;
	}

	public Address toAddress(AddressModel model) {
		return toAddress(model, new Address());
	}

	public Address toAddress(AddressModel model, Address address) {
		if (model == null)
			return null;

		if (address == null)
			address = new Address();

		address.setAddress1(model.getAddress1());
		address.setAddress2(model.getAddress2());
		address.setCity(model.getCity());
		address.setState(model.getState());
		address.setZip(model.getZip());
		address.setCountry(model.getCountry());

		return address;
	}

	public Address populateAddress(CoreAddressModels.AddressModel model) {
		if (model == null)
			return null;

		Address address = new Address();
		address.setAddress1(model.getAddress1());
		address.setAddress2(model.getAddress2());
		address.setCity(model.getCity());
		address.setState(model.getState());
		address.setZip(model.getZip());
		address.setCountry(model.getCountry());

		return address;
	}

	public LastLocation populateLastLocation(LastLocationModel model) {
		if (model == null)
			return null;

		LastLocation lastLocation = new LastLocation();
		lastLocation.setObjectType(model.getObjectType());
		lastLocation.setObjectId(model.getObjectId());
		lastLocation.setLatitude(model.getLatitude());
		lastLocation.setLongitude(model.getLongitude());

		return lastLocation;
	}

	public PasswordPolicyModel toPasswordPolicyModel(PasswordPolicy passwordPolicy) {
		if (passwordPolicy == null)
			return null;

		PasswordPolicyModel model = new PasswordPolicyModel().withAllowWhitespace(passwordPolicy.isAllowWhitespace())
		        .withHistorical(passwordPolicy.getHistorical()).withMaxLength(passwordPolicy.getMaxLength())
		        .withMinDigit(passwordPolicy.getMinDigit()).withMinLength(passwordPolicy.getMinLength())
		        .withMinLowerCase(passwordPolicy.getMinLowerCase()).withMinSpecial(passwordPolicy.getMinSpecial())
		        .withMinUpperCase(passwordPolicy.getMinUpperCase());

		return model;
	}

	public PasswordPolicy toPasswordPolicy(PasswordPolicyModel model) {
		return toPasswordPolicy(model, new PasswordPolicy());
	}

	public PasswordPolicy toPasswordPolicy(PasswordPolicyModel model, PasswordPolicy passwordPolicy) {
		if (model == null)
			return null;

		if (passwordPolicy == null)
			passwordPolicy = new PasswordPolicy();

		passwordPolicy.setMinUpperCase(model.getMinUpperCase());
		passwordPolicy.setMinLowerCase(model.getMinLowerCase());
		passwordPolicy.setMinDigit(model.getMinDigit());
		passwordPolicy.setMinLength(model.getMinLength());
		passwordPolicy.setMaxLength(model.getMaxLength());
		passwordPolicy.setHistorical(model.getHistorical());
		passwordPolicy.setMinSpecial(model.getMinSpecial());
		passwordPolicy.setAllowWhitespace(model.isAllowWhitespace());

		return passwordPolicy;
	}

	public LoginPolicyModel toLoginPolicyModel(LoginPolicy loginPolicy) {
		if (loginPolicy == null)
			return null;

		LoginPolicyModel model = new LoginPolicyModel().withMaxFailedLogins(loginPolicy.getMaxFailedLogins())
		        .withLockTimeoutSecs(loginPolicy.getLockTimeoutSecs());

		return model;
	}

	public LoginPolicy toLoginPolicy(LoginPolicyModel model) {
		return toLoginPolicy(model, new LoginPolicy());
	}

	public LoginPolicy toLoginPolicy(LoginPolicyModel model, LoginPolicy loginPolicy) {
		if (model == null)
			return null;

		if (loginPolicy == null)
			loginPolicy = new LoginPolicy();

		loginPolicy.setMaxFailedLogins(model.getMaxFailedLogins());
		loginPolicy.setLockTimeoutSecs(model.getLockTimeoutSecs());

		return loginPolicy;
	}

}
