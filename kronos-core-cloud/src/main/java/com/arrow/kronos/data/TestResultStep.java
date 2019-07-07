package com.arrow.kronos.data;

import java.io.Serializable;
import java.time.Instant;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TestResultStep implements Serializable {
	private static final long serialVersionUID = 1703853270489890061L;

	public enum Status {
		PENDING, INPROGRESS, SKIPPED, SUCCESS, FAIL
	}

	@NotNull
	private TestProcedureStep definition;
	@NotNull
	private Status status;
	private String error;
	private String comment;
	private Instant started;
	private Instant ended;

	@Transient
	@JsonIgnore
	private TestResult refTestResult;

	public TestResultStep() {
	}

	public TestResultStep(TestProcedureStep definition, Status status, String error, String comment, Instant started,
	        Instant ended) {
		this.definition = definition;
		this.status = status;
		this.error = error;
		this.comment = comment;
		this.started = started;
		this.ended = ended;
	}

	public TestProcedureStep getDefinition() {
		return definition;
	}

	public void setDefinition(TestProcedureStep definition) {
		this.definition = definition;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Instant getStarted() {
		return started;
	}

	public void setStarted(Instant started) {
		this.started = started;
	}

	public Instant getEnded() {
		return ended;
	}

	public void setEnded(Instant ended) {
		this.ended = ended;
	}

	public TestResult getRefTestResult() {
		return refTestResult;
	}

	public void setRefTestResult(TestResult refTestResult) {
		this.refTestResult = refTestResult;
	}
}
