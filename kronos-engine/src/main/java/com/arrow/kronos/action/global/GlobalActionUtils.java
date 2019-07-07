package com.arrow.kronos.action.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.JsonUtils;
import com.arrow.kronos.data.action.GlobalAction;
import com.arrow.kronos.data.action.GlobalActionProperty;
import com.fasterxml.jackson.core.type.TypeReference;

public class GlobalActionUtils {

	private static final Pattern KEY_VALUE_PAIRS_PATTERN = Pattern.compile("(\\S+)\\s*=\\s*(.*?)");
	private static final String NEW_LINE_REGEX = "\\r?\\n";

	public static GlobalActionProperty getProperty(GlobalAction action, String propertyName) {

		// TODO check if required

		return action.getProperties().stream().filter(p -> p.getParameterName().equals(propertyName)).findFirst()
		        .orElse(null);
	}

	public static GlobalActionProperty getProperty(List<GlobalActionProperty> actionProperties, String propertyName) {
		// TODO check if required

		return actionProperties.stream().filter(p -> p.getParameterName().equals(propertyName)).findFirst()
		        .orElse(null);
	}
	
	public static Long parseLong(String longTypeValue) {
		try {
			return Long.valueOf(longTypeValue);
		} catch (Throwable t) {
			throw new AcsLogicalException("invalid longTypeValue " + longTypeValue + ". Exception: " + t.getMessage());
		}
	}

	public static List<String> parseList(String listTypeValue) {
		try {
			List<String> values = JsonUtils.fromJson(listTypeValue, new TypeReference<List<String>>() {
			});
			return values;
		} catch (Throwable t) {
			throw new AcsLogicalException("invalid listTypeValue " + listTypeValue + ". Exception: " + t.getMessage());
		}
	}

	public static Map<String, String> parseMap(String mapTypeValue) {
		try {
			Map<String, String> map = JsonUtils.fromJson(mapTypeValue, new TypeReference<Map<String, String>>() {
			});
			return map;
		} catch (Throwable t) {
			throw new AcsLogicalException("invalid mapTypeValue " + mapTypeValue + ". Exception: " + t.getMessage());
		}
	}

	public static Map<String, Object> parseKeyValuePairs(String pairs) {
		Map<String, Object> result = new HashMap<>();
		parseKeyValuePairs(pairs, (k, v) -> result.put(k, v));
		return result;
	}

	public static void parseKeyValuePairs(String pairs, BiConsumer<String, String> func) {
		if (StringUtils.hasText(pairs)) {
			for (String pair : pairs.split(NEW_LINE_REGEX)) {
				Matcher matcher = KEY_VALUE_PAIRS_PATTERN.matcher(pair);
				if (matcher.matches()) {
					func.accept(StringUtils.trimWhitespace(matcher.group(1)),
					        StringUtils.trimWhitespace(matcher.group(2)));
				}
			}
		}
	}

	public static List<String> parseMultilineString(String multiline) {
		List<String> result = new ArrayList<>();
		parseMultilineString(multiline, (s) -> result.add(s));
		return result;
	}

	public static void parseMultilineString(String multiline, Consumer<String> func) {
		if (StringUtils.hasText(multiline)) {
			for (String line : multiline.split(NEW_LINE_REGEX)) {
				func.accept(line);
			}
		}
	}
}
