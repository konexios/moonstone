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
package com.arrow.acs;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtils {

    private static ObjectMapper objectMapper;

    // static initializer
    static {
        objectMapper = new ObjectMapper();

        // support of Java 8 time
        objectMapper.registerModule(new JavaTimeModule());

        // ignore nanoseconds
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        // serialize date/time as String
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // ignore getters/setters
        objectMapper.getVisibilityChecker().withGetterVisibility(Visibility.NONE)
                .withIsGetterVisibility(Visibility.NONE).withSetterVisibility(Visibility.NONE);

        // ignore null properties
        objectMapper.setSerializationInclusion(Include.NON_NULL);

        // ignore unknown properties
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // turn off pretty-print
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String toJson(Object value) {
        if (value == null)
            return null;
        try {
            return getObjectMapper().writeValueAsString(value);
        } catch (Exception e) {
            throw new AcsSystemException("Error writing object to JSON format", e);
        }
    }

    public static byte[] toJsonBytes(Object value) {
        if (value == null)
            return null;
        try {
            return getObjectMapper().writeValueAsBytes(value);
        } catch (Exception e) {
            throw new AcsSystemException("Error writing object to JSON format", e);
        }
    }

    public static <T> T fromJsonBytes(byte[] value, Class<T> type) {
        if (value == null || value.length == 0)
            return null;
        try {
            return (T) getObjectMapper().readValue(value, type);
        } catch (Exception e) {
            e.printStackTrace(); // TODO: Remove this.
            throw new AcsSystemException("Error reading object from JSON format", e);
        }
    }

    public static <T> T fromJson(String value, Class<T> type) {
        if (AcsUtils.isEmpty(value))
            return null;
        try {
            return (T) getObjectMapper().readValue(value, type);
        } catch (Exception e) {
            e.printStackTrace(); // TODO: Remove this.
            throw new AcsSystemException("Error reading object from JSON format", e);
        }
    }

    public static <T> T fromJsonBytes(byte[] value, TypeReference<T> type) {
        if (value == null || value.length == 0)
            return null;
        try {
            return getObjectMapper().readValue(value, type);
        } catch (Exception e) {
            throw new AcsSystemException("Error reading object from JSON format", e);
        }
    }

    public static <T> T fromJson(String value, TypeReference<T> type) {
        if (AcsUtils.isEmpty(value))
            return null;
        try {
            return getObjectMapper().readValue(value, type);
        } catch (Exception e) {
            throw new AcsSystemException("Error reading object from JSON format", e);
        }
    }
}
