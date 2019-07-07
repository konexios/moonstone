package com.arrow.kronos;

import java.util.Collection;

public interface ContextListener {
	void applicationListChanged(Collection<String> applicationIds);

	void applicationSettingsChanged(String applicationId);
}
