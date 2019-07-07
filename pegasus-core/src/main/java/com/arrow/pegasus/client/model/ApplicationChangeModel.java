package com.arrow.pegasus.client.model;

import java.io.Serializable;

import com.arrow.pegasus.data.profile.Application;

public class ApplicationChangeModel implements Serializable {

	private static final long serialVersionUID = -4038044004058446416L;

	private Application application;
	private String who;

	public ApplicationChangeModel withApplication(Application application) {
		setApplication(application);
		return this;
	}

	public ApplicationChangeModel withWho(String who) {
		setWho(who);
		return this;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}
}
