package com.arrow.widget.ootb;

import org.springframework.beans.factory.annotation.Autowired;

import com.arrow.dashboard.runtime.model.UserWrapper;
import com.arrow.dashboard.widget.WidgetDataProviderAbstract;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl.Retention;
import com.arrow.pegasus.client.api.ClientApplicationApi;
import com.arrow.pegasus.client.api.ClientCompanyApi;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;

@DataProviderImpl(dataProviders = UserDataProvider.class, retention = Retention.AUTOWIRED)
public class UserProviderImplementation extends WidgetDataProviderAbstract {

	@Autowired
	private ClientApplicationApi clientApplicationApi;
	@Autowired
	private ClientCompanyApi clientCompanyApi;

	public UserInfo getUserInfo(UserWrapper userWrapper) {

		String method = "getUserInfo";
		logDebug(method, "UserProviderImplementation got request for user info from user " + userWrapper.getLogin());

		Application app = clientApplicationApi.findById(userWrapper.getApplicationId());
		Company comp = clientCompanyApi.findAll().stream()
		        .filter(company -> userWrapper.getCompanyId().equals(company.getId())).findFirst().orElse(null);

		UserInfo userInfo = new UserInfo();
		userInfo.name = userWrapper.getLogin();
		userInfo.application = app.getName();
		userInfo.company = comp.getName();

		return userInfo;

	}

}
