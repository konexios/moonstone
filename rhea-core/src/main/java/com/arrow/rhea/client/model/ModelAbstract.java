package com.arrow.rhea.client.model;

import java.io.Serializable;

public abstract class ModelAbstract<T extends Serializable, U extends ModelAbstract<T, U>> implements Serializable {

	private static final long serialVersionUID = 8744527679251707227L;

	private T model;
	private String who;

	public abstract U self();

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public U withWho(String who) {
		setWho(who);
		return self();
	}

	public U withModel(T model) {
		setModel(model);
		return self();
	}
}
