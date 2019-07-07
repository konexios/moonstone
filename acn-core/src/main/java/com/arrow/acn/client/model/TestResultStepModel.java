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
package com.arrow.acn.client.model;

import java.io.Serializable;

public class TestResultStepModel implements Serializable{
	private static final long serialVersionUID = 7560816543482740980L;

	private TestProcedureStepModel definition;
	private String comment;
	private String error;
	private String status;
	private String started;
	private String ended;
	
	public TestProcedureStepModel getDefinition() {
		return definition;
	}

	public void setDefinition(TestProcedureStepModel definition) {
		this.definition = definition;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStarted() {
		return started;
	}

	public void setStarted(String started) {
		this.started = started;
	}
	
	public String getEnded() {
		return ended;
	}

	public void setEnded(String ended) {
		this.ended = ended;
	}
}
