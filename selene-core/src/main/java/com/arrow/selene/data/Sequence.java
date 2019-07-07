package com.arrow.selene.data;

public class Sequence extends EntityAbstract {
	private static final long serialVersionUID = -1439176447410188254L;

	private String name;
	private long count;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
}
