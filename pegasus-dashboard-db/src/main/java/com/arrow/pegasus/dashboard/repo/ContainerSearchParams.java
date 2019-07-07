package com.arrow.pegasus.dashboard.repo;

import java.util.Set;

import com.arrow.pegasus.repo.params.DocumentSearchParams;

public class ContainerSearchParams extends DocumentSearchParams {
	private static final long serialVersionUID = -3703879574558166129L;

	private Set<String> boardIds;

	public Set<String> getBoardIds() {
		return super.getValues(boardIds);
	}

	public ContainerSearchParams addBoardIds(String... boardIds) {
		this.boardIds = super.addValues(this.boardIds, boardIds);

		return this;
	}
}
