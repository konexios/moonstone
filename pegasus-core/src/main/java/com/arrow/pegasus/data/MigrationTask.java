package com.arrow.pegasus.data;

import java.time.Instant;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;

@Document(collection = MigrationTask.COLLECTION_NAME)
public class MigrationTask extends TsDocumentAbstract {
	private static final long serialVersionUID = 2931906069251004889L;
	public static final String COLLECTION_NAME = "migration_task";

	@NotBlank
	private String name;
	private boolean complete = false;
	private String error;
	private Instant lastAttempt;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Instant getLastAttempt() {
		return lastAttempt;
	}

	public void setLastAttempt(Instant lastAttempt) {
		this.lastAttempt = lastAttempt;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.MIGRATION_TASK;
	}
}
