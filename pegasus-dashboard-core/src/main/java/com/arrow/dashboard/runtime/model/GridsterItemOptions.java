package com.arrow.dashboard.runtime.model;

import java.io.Serializable;

public class GridsterItemOptions implements Serializable {
	private static final long serialVersionUID = 4922201942827083682L;

	private static final int DEFAULT_ROW = 0;
	private static final int DEFAULT_COL = 0;
	private static final int DEFAULT_SIZE_X = 1;
	private static final int DEFAULT_SIZE_Y = 1;

	private int row = DEFAULT_ROW;
	private int col = DEFAULT_COL;
	private int sizeX = DEFAULT_SIZE_X;
	private int sizeY = DEFAULT_SIZE_Y;

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
}