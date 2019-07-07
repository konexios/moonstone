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
package com.arrow.acn.client.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.arrow.acn.client.AcnClientException;
import com.arrow.acn.client.ClientConstants;
import com.arrow.acn.client.ClientLogger;
import com.arrow.acs.AcsUtils;
import com.arrow.acs.Loggable;

public class Utils {
	private static final Loggable LOGGER = new ClientLogger(Utils.class.getName());

	public static Double trim2Decimals(Double value) {
		return Double.parseDouble(createDecimalFormatter("#.##").format(value));
	}

	public static Double trim2Decimals(float value) {
		return Double.parseDouble(createDecimalFormatter("#.##").format(value));
	}

	public static Double trim8Decimals(Double value) {
		return Double.parseDouble(createDecimalFormatter("#.########").format(value));
	}

	public static Double trim8Decimals(float value) {
		return Double.parseDouble(createDecimalFormatter("#.########").format(value));
	}

	private static DecimalFormat createDecimalFormatter(String pattern) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
		formatter.applyPattern(pattern);
		return formatter;
	}

	public static String toBinaryString(byte... bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(Integer.toBinaryString(256 + b));
		}
		return sb.toString();
	}

	public static void shutdownThread(Thread thread) {
		String method = "shutdownThread";
		if (thread != null && thread.isAlive()) {
			try {
				LOGGER.logInfo(method, "shutting down %s ...", thread.getName());
				thread.interrupt();
				LOGGER.logInfo(method, "joining thread ...");
			} catch (Throwable e) {
			}
		}
	}

	public static void shutdownProcess(Process process, String name) {
		String method = "shutdownProcess";
		if (process != null && process.isAlive()) {
			try {
				LOGGER.logInfo(method, "shutting down %s ...", name);
				process.destroy();
				if (!process.waitFor(ClientConstants.DEFAULT_SHUTDOWN_WAITING_MS, TimeUnit.MILLISECONDS)) {
					process.destroyForcibly();
				}
			} catch (Throwable t) {
			}
		}
		LOGGER.logInfo(method, "shutdown %s complete", name);
	}

	public static String getRequiredProperty(Properties props, String property) {
		String result = props.getProperty(property);
		if (AcsUtils.isEmpty(result)) {
			throw new AcnClientException("required property not found: " + property);
		}
		return result;
	}

	public static float celsiusToFahrenheit(float value) {
		return value * 9.0f / 5.0f + 32.0f;
	}

	public static double celsiusToFahrenheit(double value) {
		return value * 9.0 / 5.0 + 32.0;
	}

	public static void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
		}
	}
}
