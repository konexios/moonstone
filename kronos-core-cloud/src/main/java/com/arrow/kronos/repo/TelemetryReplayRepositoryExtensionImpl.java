package com.arrow.kronos.repo;

import com.arrow.kronos.data.TelemetryReplay;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class TelemetryReplayRepositoryExtensionImpl extends RepositoryExtensionAbstract<TelemetryReplay>
        implements TelemetryReplayRepositoryExtension {

	public TelemetryReplayRepositoryExtensionImpl() {
		super(TelemetryReplay.class);
	}
}