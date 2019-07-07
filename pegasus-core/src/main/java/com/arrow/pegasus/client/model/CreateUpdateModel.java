package com.arrow.pegasus.client.model;

import java.io.Serializable;

public class CreateUpdateModel<M extends Serializable> implements Serializable {
	private static final long serialVersionUID = -304085957808851353L;

	private String who;
	private M model;

	public CreateUpdateModel() {
		super();
	}

	public CreateUpdateModel(String who, M model) {
		super();
		this.who = who;
		this.model = model;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public M getModel() {
		return model;
	}

	public void setModel(M model) {
		this.model = model;
	}

	public CreateUpdateModel<M> withModel(M model) {
		this.model = model;

		return this;
	}

	public CreateUpdateModel<M> withWho(String who) {
		this.who = who;

		return this;
	}
}
