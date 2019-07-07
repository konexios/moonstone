package com.arrow.dashboard.runtime.model;

import java.io.Serializable;

public class GridsterGridOptions implements Serializable {
	private static final long serialVersionUID = 6460191246640672362L;

	// TODO future enhancements, currently minimal support for grid options

	private static final int DEFAULT_COLUMNS = 4;

	private int columns = DEFAULT_COLUMNS;

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
}
