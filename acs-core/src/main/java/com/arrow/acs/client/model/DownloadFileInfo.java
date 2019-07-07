/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acs.client.model;

import java.io.File;
import java.io.Serializable;

public class DownloadFileInfo implements Serializable {
	private static final long serialVersionUID = 4089147421358793617L;

	private String fileName;
	private long size;
	private File tempFile;

	public DownloadFileInfo withFileName(String fileName) {
		setFileName(fileName);
		return this;
	}

	public DownloadFileInfo withSize(long size) {
		setSize(size);
		return this;
	}

	public DownloadFileInfo withTempFile(File tempFile) {
		setTempFile(tempFile);
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public File getTempFile() {
		return tempFile;
	}

	public void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + (int) (size ^ (size >>> 32));
		result = prime * result + ((tempFile == null) ? 0 : tempFile.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DownloadFileInfo other = (DownloadFileInfo) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (size != other.size)
			return false;
		if (tempFile == null) {
			if (other.tempFile != null)
				return false;
		} else if (!tempFile.equals(other.tempFile))
			return false;
		return true;
	}
}
