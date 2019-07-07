package com.arrow.kronos;

import java.util.List;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.arrow.pegasus.security.CoreApiKeyFilter;
import com.arrow.pegasus.security.CoreApiSecurity;

public class KronosApiSecurity extends CoreApiSecurity {

	private List<String> exceptionPaths;

	public KronosApiSecurity() {
	}

	public KronosApiSecurity(List<String> exceptionPaths) {
		super(true, exceptionPaths);
		this.exceptionPaths = exceptionPaths;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		KronosApiRequestFilter filter = new KronosApiRequestFilter(exceptionPaths);
		getApplicationContext().getAutowireCapableBeanFactory().autowireBean(filter);
		http.addFilterAfter(filter, CoreApiKeyFilter.class);
	}

}
