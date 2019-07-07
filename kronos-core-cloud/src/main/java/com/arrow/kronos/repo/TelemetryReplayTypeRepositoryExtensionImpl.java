package com.arrow.kronos.repo;

import com.arrow.kronos.data.TelemetryReplayType;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class TelemetryReplayTypeRepositoryExtensionImpl extends RepositoryExtensionAbstract<TelemetryReplayType>
        implements TelemetryReplayTypeRepositoryExtension {

	public TelemetryReplayTypeRepositoryExtensionImpl() {
		super(TelemetryReplayType.class);
	}
}