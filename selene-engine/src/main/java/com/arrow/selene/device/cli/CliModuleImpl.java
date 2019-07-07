package com.arrow.selene.device.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import com.arrow.acs.JsonUtils;
import com.arrow.selene.SysUtils;
import com.arrow.selene.engine.DeviceModuleAbstract;
import com.arrow.selene.engine.EngineConstants;
import com.arrow.selene.model.ProcessStatusModel;

public class CliModuleImpl extends DeviceModuleAbstract<CliInfo, CliProperties, CliStates, CliData>
		implements CliModule {

	private static final String DEFAULT_EMPTY_COMMAND = "EMPTY";

	private long currentCommandIntervalInSecs;
	private String currentCommand;
	private Timer timer;

	@Override
	protected void startDevice() {
		super.startDevice();

		currentCommand = getProperties().getCommand();
		currentCommandIntervalInSecs = getProperties().getCommandIntervalInSecs();
		startTimer();
	}

	@Override
	public void stop() {
		super.stop();
		stopTimer();
	}

	@Override
	public void notifyPropertiesChanged(Map<String, String> properties) {
		String method = "notifyPropertiesChanged";
		super.notifyPropertiesChanged(properties);
		try {
			String command = getProperties().getCommand();
			if (!StringUtils.equals(currentCommand, command)) {
				logInfo(method, "detected change of command: [%s] -> [%s]", currentCommand, command);
				currentCommand = command;
			}
			long commandIntervalInSecs = getProperties().getCommandIntervalInSecs();
			if (commandIntervalInSecs != currentCommandIntervalInSecs) {
				logInfo(method, "detected change of commandIntervalInSecs: %d -> %d, restarting timer ...",
						currentCommandIntervalInSecs, commandIntervalInSecs);
				currentCommandIntervalInSecs = commandIntervalInSecs;
				startTimer();
			}
		} catch (Throwable t) {
			logError(method, t);
		}
	}

	private void executeCommand() {
		String method = "executeCommand";

		System.out.println("CliModuleImpl.executeCommand() command = " + currentCommand + ".");

		try {
			if (StringUtils.isEmpty(currentCommand)
					|| StringUtils.equalsIgnoreCase(currentCommand, DEFAULT_EMPTY_COMMAND)) {
				logWarn(method, "ignoring EMPTY command");
			} else {
				CliDataImpl payload = new CliDataImpl();
				ProcessStatusModel status = SysUtils.executeCommandLine(currentCommand.split(" "));
				if (status.getExitCode() != 0) {
					logError(method, "command failed! exitCode: %d, error: %s, output: %s", status.getExitCode(),
							status.getError(), status.getOutput());
				} else {
					String data = StringUtils.trimToEmpty(status.getOutput());
					try {
						String dataType = getProperties().getDataType();
						logInfo(method, "dataType: %s, result data: %s", dataType, data);
						if (StringUtils.equalsIgnoreCase(dataType, CliProperties.DOUBLE_DATA_TYPE)) {
							payload.setDoubleData(Double.parseDouble(data));
						} else if (StringUtils.equalsIgnoreCase(dataType, CliProperties.LONG_DATA_TYPE)) {
							payload.setLongData(Long.parseLong(data));
						} else if (StringUtils.equalsIgnoreCase(dataType, CliProperties.STRING_ARRAY_DATA_TYPE)) {
							List<String> list = new ArrayList<>();
							Arrays.asList(data.split(getProperties().getArrayDelimiter(), -1))
									.forEach(token -> list.add(token.trim()));
							payload.setStrArrayData(list.toArray(new String[list.size()]));
						} else if (StringUtils.equalsIgnoreCase(dataType, CliProperties.DOUBLE_ARRAY_DATA_TYPE)) {
							List<Double> list = new ArrayList<>();
							Arrays.asList(data.split(getProperties().getArrayDelimiter(), -1))
									.forEach(token -> list.add(Double.parseDouble(token.trim())));
							payload.setDoubleArrayData(list.toArray(new Double[list.size()]));
						} else if (StringUtils.equalsIgnoreCase(dataType, CliProperties.LONG_ARRAY_DATA_TYPE)) {
							List<Long> list = new ArrayList<>();
							Arrays.asList(data.split(getProperties().getArrayDelimiter(), -1))
									.forEach(token -> list.add(Long.parseLong(token.trim())));
							payload.setLongArrayData(list.toArray(new Long[list.size()]));
						} else if (StringUtils.equalsIgnoreCase(dataType, CliProperties.RAW_DATA_TYPE)) {
							payload.setRawTelemetries(JsonUtils.fromJson(data, EngineConstants.MAP_TYPE_REF));
						} else {
							payload.setStrData(data);
						}
					} catch (Exception x) {
						x.printStackTrace();
						logWarn(method, "error parsing result, default to STRING", x);
						payload.setStrData(data);
					}
					queueDataForSending(payload);
				}
			}
		} catch (Throwable t) {
			logError(method, "unable to execute command", t);
		}
	}

	private void startTimer() {
		String method = "restartTimer";
		stopTimer();
		if (currentCommandIntervalInSecs > 0) {
			logInfo(method, "starting timer, currentCommandIntervalInSecs: %d", currentCommandIntervalInSecs);
			timer = new Timer();
			timer.scheduleAtFixedRate(new CommandTimerTask(), currentCommandIntervalInSecs * 1000L,
					currentCommandIntervalInSecs * 1000L);
		} else {
			logWarn(method, "timer is disabled because currentCommandIntervalInSecs is negative");
		}
	}

	private void stopTimer() {
		String method = "stopTimer";
		if (timer != null) {
			logInfo(method, "stopping timer ...");
			timer.cancel();
			timer = null;
		}
	}

	@Override
	protected CliProperties createProperties() {
		return new CliProperties();
	}

	@Override
	protected CliInfo createInfo() {
		return new CliInfo();
	}

	@Override
	protected CliStates createStates() {
		return new CliStates();
	}

	private class CommandTimerTask extends TimerTask {
		private AtomicBoolean running = new AtomicBoolean(false);

		@Override
		public void run() {
			if (!isShuttingDown()) {
				if (running.compareAndSet(false, true)) {
					executeCommand();
					running.set(false);
				}
			}
		}
	}
}
