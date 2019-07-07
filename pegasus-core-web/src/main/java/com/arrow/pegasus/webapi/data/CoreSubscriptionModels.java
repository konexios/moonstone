package com.arrow.pegasus.webapi.data;

import com.arrow.pegasus.data.profile.Subscription;

public class CoreSubscriptionModels {

	public static class SubscriptionOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -8953191142859420709L;

		public SubscriptionOption(Subscription subscription) {
			super(subscription.getId(), subscription.getHid(), subscription.getName());
		}
	}
}
