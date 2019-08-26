package moonstone.selene.model;

import java.io.Serializable;

public class ProcessStatusModel implements Serializable {
	private static final long serialVersionUID = -726278139450764818L;

	private int exitCode;
	private String output;
	private String error;

	public ProcessStatusModel withExitCode(int exitCode) {
		setExitCode(exitCode);
		return this;
	}

	public ProcessStatusModel withOutput(String output) {
		setOutput(output);
		return this;
	}

	public ProcessStatusModel withError(String error) {
		setError(error);
		return this;
	}

	public int getExitCode() {
		return exitCode;
	}

	public void setExitCode(int exitCode) {
		this.exitCode = exitCode;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
