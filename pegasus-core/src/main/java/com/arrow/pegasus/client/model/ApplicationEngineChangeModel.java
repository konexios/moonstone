package com.arrow.pegasus.client.model;

import java.io.Serializable;

import com.arrow.pegasus.data.ApplicationEngine;

public class ApplicationEngineChangeModel implements Serializable {

	private static final long serialVersionUID = -5312323163119264412L;

	private ApplicationEngine applicationEngine;
	private String who;

	public ApplicationEngineChangeModel withApplicationEngine(ApplicationEngine applicationEngine) {
		setApplicationEngine(applicationEngine);
		return this;
	}

	public ApplicationEngineChangeModel withWho(String who) {
		setWho(who);
		return this;
	}

	public ApplicationEngine getApplicationEngine() {
		return applicationEngine;
	}

	public void setApplicationEngine(ApplicationEngine applicationEngine) {
		this.applicationEngine = applicationEngine;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}
}
