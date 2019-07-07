package com.arrow.pegasus.web.model;

import java.io.Serializable;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.webapi.data.CoreCompanyModels;
import com.arrow.pegasus.webapi.data.CoreContactModels;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;
import com.arrow.pegasus.webapi.data.CoreSubscriptionModels;
import com.arrow.pegasus.webapi.data.KeyValueOption;

public class SubscriptionModels extends CoreSubscriptionModels {

	public static class SubscriptionFilterOptions implements Serializable {
		private static final long serialVersionUID = 7226811110900953631L;

		private List<CompanyModels.CompanyOption> companyOptions;
		private List<KeyValueOption> enabledOptions;

		public SubscriptionFilterOptions() {
		}

		public List<CompanyModels.CompanyOption> getCompanyOptions() {
			return companyOptions;
		}

		public void setCompanyOptions(List<CompanyModels.CompanyOption> companyOptions) {
			this.companyOptions = companyOptions;
		}

		public SubscriptionFilterOptions withCompanyOptions(List<CompanyModels.CompanyOption> companyOptions) {
			setCompanyOptions(companyOptions);

			return this;
		}

		public List<KeyValueOption> getEnabledOptions() {
			return enabledOptions;
		}

		public void setEnabledOptions(List<KeyValueOption> enabledOptions) {
			this.enabledOptions = enabledOptions;
		}

		public SubscriptionFilterOptions withEnabledOptions(List<KeyValueOption> enabledOptions) {
			setEnabledOptions(enabledOptions);

			return this;
		}
	}

	public static class SubscriptionList extends CoreDocumentModel {
		private static final long serialVersionUID = -6140286242561358089L;

		private String name;
		private String description;
		private String companyName;
		private String startDate;
		private String endDate;
		private boolean enabled;

		public SubscriptionList(Subscription subscription, Company company) {
			super(subscription.getId(), subscription.getHid());
			this.name = subscription.getName();
			this.description = subscription.getDescription();
			this.companyName = company != null ? company.getName() : "UNKNOWN (" + subscription.getCompanyId() + ")";
			this.startDate = subscription.getStartDate() == null ? null
			        : DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneOffset.UTC).format(subscription.getStartDate());
			this.endDate = subscription.getEndDate() == null ? null
			        : DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneOffset.UTC).format(subscription.getEndDate());
			this.enabled = subscription.isEnabled();
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getCompanyName() {
			return companyName;
		}

		public String getStartDate() {
			return startDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public boolean isEnabled() {
			return enabled;
		}
	}

	public static class SubscriptionModel extends CoreDocumentModel {
		private static final long serialVersionUID = 5797241389571565436L;

		private String name;
		private String description;
		private boolean enabled;
		private String companyId;
		private String startDate;
		private String endDate;
		private CoreContactModels.ContactModel contact;
		private CoreContactModels.ContactModel billingContact;

		public SubscriptionModel() {
			super(null, null);
		}

		public SubscriptionModel(Subscription subscription, String startDate, String endDate) {
			super(subscription.getId(), subscription.getHid());
			this.name = subscription.getName();
			this.description = subscription.getDescription();
			this.enabled = subscription.isEnabled();
			this.companyId = subscription.getCompanyId();
			this.startDate = startDate;
			this.endDate = endDate;
			this.contact = subscription.getContact() == null ? new CoreContactModels.ContactModel()
			        : new CoreContactModels.ContactModel(subscription.getContact());
			this.billingContact = subscription.getBillingContact() == null ? new CoreContactModels.ContactModel()
			        : new CoreContactModels.ContactModel(subscription.getBillingContact());
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public String getCompanyId() {
			return companyId;
		}

		public String getStartDate() {
			return startDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public CoreContactModels.ContactModel getContact() {
			return contact;
		}

		public CoreContactModels.ContactModel getBillingContact() {
			return billingContact;
		}
	}

	public static class SubscriptionUpsert implements Serializable {
		private static final long serialVersionUID = 6267345084287916729L;

		private SubscriptionModel subscription;
		private List<CoreCompanyModels.CompanyOption> companyOptions;

		public SubscriptionUpsert(SubscriptionModels.SubscriptionModel subscription,
		        List<CoreCompanyModels.CompanyOption> companyOptions) {
			this.subscription = subscription;
			this.companyOptions = companyOptions;
		}

		public SubscriptionModel getSubscription() {
			return subscription;
		}

		public List<CoreCompanyModels.CompanyOption> getCompanyOptions() {
			return companyOptions;
		}
	}

	public static class ApplicationModel extends ApplicationModelAbstract<ApplicationModel> {
		private static final long serialVersionUID = 3711520145413935931L;

		@Override
		protected ApplicationModel self() {
			return this;
		}
	}

	public static class AccessKeyModel extends AccessKeyModelAbstract<AccessKeyModel> {
		private static final long serialVersionUID = 1416002011965520557L;

		public AccessKeyModel(AccessKey accessKey) {
			super(accessKey);
		}

		public AccessKeyModel() {
		}

		@Override
		protected AccessKeyModel self() {
			return this;
		}
	}
}
