package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;

public class ApplicationModels {
	public static class ApplicationOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -1125437446518882638L;

		public ApplicationOption() {
			super(null, null, null);
		}

		public ApplicationOption(Application application) {
			super(application.getId(), application.getHid(), application.getName());
		}
	}

	public static class ApplicationModel extends Application {
		private static final long serialVersionUID = 684654810545070206L;

		private SubscriptionExpirationModel subscriptionExpiration;

		public ApplicationModel(Application application) {
			setId(application.getId());
			setHid(application.getHid());
			setName(application.getName());
			setEnabled(application.isEnabled());
			setDescription(application.getDescription());
			setLastModifiedDate(application.getLastModifiedDate());
			setLastModifiedBy(application.getLastModifiedBy());
			setCreatedDate(application.getCreatedDate());
			setCreatedBy(application.getCreatedBy());
			setZoneId(application.getZoneId());
			setCompanyId(application.getCompanyId());
			setProductId(application.getProductId());
			setSubscriptionId(application.getSubscriptionId());
			setVaultId(application.getVaultId());
			setApiSigningRequired(application.getApiSigningRequired());
			setApplicationEngineId(application.getApplicationEngineId());
			setDefaultSamlEntityId(application.getDefaultSamlEntityId());
			setCode(application.getCode());
			setConfigurations(application.getConfigurations());
			setProductExtensionIds(application.getProductExtensionIds());
			setProductFeatures(application.getProductFeatures());
		}

		public SubscriptionExpirationModel getSubscriptionExpiration() {
			return subscriptionExpiration;
		}

		public void setSubscriptionExpiration(SubscriptionExpirationModel subscriptionExpiration) {
			this.subscriptionExpiration = subscriptionExpiration;
		}
	}

	public static class SubscriptionExpirationModel implements Serializable {
		private static final long serialVersionUID = 369993842389798950L;

		private boolean expired = false;
		private boolean expiring = false;
		private Long endDate;

		public SubscriptionExpirationModel(Instant endDate) {
			this.endDate = endDate.toEpochMilli();
			this.expired = Instant.now().isAfter(endDate);
		}

		public SubscriptionExpirationModel(Instant endDate, int days) {
			this.endDate = endDate.toEpochMilli();
			Instant now = Instant.now();
			this.expired = now.isAfter(endDate);
			this.expiring = now.isAfter(endDate.minus(days, ChronoUnit.DAYS)) && now.isBefore(endDate);
		}

		public boolean isExpired() {
			return expired;
		}

		public boolean isExpiring() {
			return expiring;
		}

		public Long getEndDate() {
			return endDate;
		}
	}
}
