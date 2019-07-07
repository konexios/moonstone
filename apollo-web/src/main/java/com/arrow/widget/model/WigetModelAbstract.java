package com.arrow.widget.model;

import java.io.Serializable;

public abstract class WigetModelAbstract<T extends WigetModelAbstract<T>> implements Serializable {
	private static final long serialVersionUID = -4018956211420647813L;

	public final static String SELF_LINK = "self";
	
	protected abstract T self();
}
