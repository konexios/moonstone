package com.arrow.widget.socialevent;

import java.io.Serializable;

public class SocialEventStat implements Serializable {
	private static final long serialVersionUID = -2480640937835601104L;

	private long allRegisteredUsersCount;
	private long todayRegisteredUsersCount;

	public long getAllRegisteredUsersCount() {
		return allRegisteredUsersCount;
	}

	public void setAllRegisteredUsersCount(long allRegisteredUsersCount) {
		this.allRegisteredUsersCount = allRegisteredUsersCount;
	}

	public long getTodayRegisteredUsersCount() {
		return todayRegisteredUsersCount;
	}

	public void setTodayRegisteredUsersCount(long todayRegisteredUsersCount) {
		this.todayRegisteredUsersCount = todayRegisteredUsersCount;
	}

	@Override
	public String toString() {
		return "SocialEventStat [allRegisteredUsersCount=" + allRegisteredUsersCount + ", todayRegisteredUsersCount="
				+ todayRegisteredUsersCount + "]";
	}

}
