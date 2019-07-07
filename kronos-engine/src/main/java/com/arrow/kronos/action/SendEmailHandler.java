package com.arrow.kronos.action;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.arrow.kronos.DeviceActionTypeConstants;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceEvent;
import com.arrow.kronos.data.DeviceTelemetry;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.service.KronosCache;
import com.arrow.pegasus.util.EmailContentType;
import com.arrow.pegasus.util.SmtpEmailSender;

public class SendEmailHandler extends ActionHandlerAbstract {

    @Autowired
    private SmtpEmailSender sender;
    @Autowired
    private KronosCache kronosCache;

    public static final String CRITERIA = "{Criteria}";
    public static final String DEVICE_HID = "{DeviceHID}";
    public static final String DEVICE_NAME = "{DeviceName}";
    public static final String DEVICE_UID = "{DeviceUID}";
    public static final String DEVICE_TYPE = "{DeviceType}";
    public static final String TIMESTAMP = "{Timestamp}";
    public static final String FULL_TELEMETRY = "{FullTelemetry}";
    public static final String TELEMETRY_VALUE = "{TelemetryValue}";
    public static final String TELEMETRY_NAME = "{TelemetryName}";
    public static final String TELEMTRY_DESCRIPTION = "{TelemetryDescription}";
    public static final String THRESHOLD_VALUE = "{ThresholdValue}";

    private static final String NUMBER_PATTERN = "([-+]?\\d*[\\.\\,]?\\d+(?:[eE][-+]?\\d+)?)";
    private static final String QUOTED_STRING_PATTERN = "[\\'\\\"](.*?)[\\'\\\"]";
    private static final String COMPARISON_OPERATOR_PATTERN = "[><]=?|[=!]=";

    @Override
    public void handle(DeviceEvent deviceEvent, TelemetryWrapper wrapper, Device device, DeviceAction action) {
        String method = "handle";
        Assert.notNull(sender, "email sender is not initialized!");

        String email = getRequiredParameter(action, DeviceActionTypeConstants.SendEmail.PARAMETER_EMAIL);
        Assert.hasText(email, "email is not defined in action");

        // parsing FROM addresses
        List<String> fromAddresses = new ArrayList<>();
        email = email.replaceAll(",", ";").replaceAll(" ", ";");
        for (String token : email.split(";", -1)) {
            token = StringUtils.trimToEmpty(token);
            if (!token.isEmpty()) {
                fromAddresses.add(token);
                logInfo(method, "found email: %s", token);
            }
        }
        if (fromAddresses.isEmpty()) {
            logError(method, "ERROR: fromAddresses is EMPTY!");
            return;
        }

        Assert.notNull(device.getRefDeviceType(), "device.refDeviceType is not populated");
        String subject = getRequiredParameter(action, DeviceActionTypeConstants.SendEmail.PARAMETER_SUBJECT);
        Assert.hasText(subject, "subject is not defined in action");
        String body = getRequiredParameter(action, DeviceActionTypeConstants.SendEmail.PARAMETER_BODY);
        Assert.hasText(body, "body is not defined in action");
        String contentType = getRequiredParameter(action, DeviceActionTypeConstants.SendEmail.PARAMETER_CONTENT_TYPE);
        Assert.hasText(contentType, "contentType is not defined in action");

        subject = replaceKeyWordsWithValues(subject, wrapper, device, action);
        logInfo(method, "sending email to: %s with email subject: %s", email, subject);

        body = replaceKeyWordsWithValues(body, wrapper, device, action);
        logDebug(method, "email body:\n %s ", body);

        EmailContentType type = contentType.toLowerCase().contains("html") ? EmailContentType.HTML
                : EmailContentType.PLAIN_TEXT;
        logInfo(method, "contentType: %s, type: %s", contentType, type.toString());

        sender.send(fromAddresses.toArray(new String[fromAddresses.size()]), new String[0], subject, body, type);

        deviceEvent.addInformation(DeviceActionTypeConstants.SendEmail.PARAMETER_SUBJECT, subject);
        deviceEvent.addInformation(DeviceActionTypeConstants.SendEmail.PARAMETER_BODY, body);
    }

    private String replaceKeyWordsWithValues(String parameter, TelemetryWrapper wrapper, Device device,
            DeviceAction action) {
        String method = "replaceKeyWordsWithValues";
        for (TelemetryItem item : wrapper.getItems()) {
            if (item.value() != null) {
                parameter = parameter.replace("{" + item.getName() + "}", item.value().toString());
            }
        }

        parameter = parameter.replace(CRITERIA, action.getCriteria());
        parameter = parameter.replace(DEVICE_HID, device.getHid());
        parameter = parameter.replace(DEVICE_NAME, device.getName());
        parameter = parameter.replace(DEVICE_UID, device.getUid());
        parameter = parameter.replace(DEVICE_TYPE, device.getRefDeviceType().getName());
        parameter = parameter.replace(TIMESTAMP, Instant.ofEpochMilli(wrapper.getTimestamp()).toString());

        if (parameter.contains(FULL_TELEMETRY)) {
            StringBuilder sb = new StringBuilder();
            for (TelemetryItem item : wrapper.getItems()) {
                sb.append("<br>").append(item.getName()).append(": ").append(item.value());
            }
            parameter = parameter.replace(FULL_TELEMETRY, sb.toString());
        }

        if (parameter.contains(THRESHOLD_VALUE)) {
            String threshold = findThresholdValue(wrapper, action.getCriteria());
            parameter = parameter.replace(THRESHOLD_VALUE, threshold);
        }

        TelemetryItem item = null;
        boolean hasTelemetryName = parameter.contains(TELEMETRY_NAME);
        boolean hasTelemetryValue = parameter.contains(TELEMETRY_VALUE);
        boolean hasTelemetryDescription = parameter.contains(TELEMTRY_DESCRIPTION);
        if (hasTelemetryName || hasTelemetryDescription || hasTelemetryValue) {
            item = findFirstTelemetryInCriteria(wrapper, action.getCriteria());
            if (item != null) {
                logInfo(method, "found telemetry name: %s in criteria: %s", item.getName(), action.getCriteria());
                if (hasTelemetryName) {
                    parameter = parameter.replace(TELEMETRY_NAME, item.getName());
                }
                if (hasTelemetryValue) {
                    parameter = parameter.replace(TELEMETRY_VALUE, item.value().toString());
                }
                if (hasTelemetryDescription) {
                    DeviceType deviceType = kronosCache.findDeviceTypeById(device.getDeviceTypeId());
                    Assert.notNull(deviceType, "Device Type not found: " + device.getDeviceTypeId());
                    final String telemetryName = item.getName();
                    DeviceTelemetry deviceTelemetry = deviceType.getTelemetries().stream()
                            .filter(t -> t.getName().equals(telemetryName)).findFirst().orElse(null);
                    if (deviceTelemetry != null && !StringUtils.isEmpty(deviceTelemetry.getDescription())) {
                        parameter = parameter.replace(TELEMTRY_DESCRIPTION, deviceTelemetry.getDescription());
                    } else {
                        logError(method, "Telemetry Definition not found or empty: %s", telemetryName);
                    }
                }
            } else {
                logWarn(method, "no telemetry item name found in criteria!");
            }
        }

        return parameter;
    }

    private TelemetryItem findFirstTelemetryInCriteria(TelemetryWrapper wrapper, String criteria) {
        TelemetryItem match = null;
        for (TelemetryItem item : wrapper.getItems()) {
            if (criteria.contains(item.getName())) {
                match = item;
                break;
            }
        }
        return match;
    }

    private static String findThresholdValue(TelemetryWrapper wrapper, String criteria) {
        String threshold = "";

        for (TelemetryItem item : wrapper.getItems()) {
            String telemetryName = item.getName();
            Pattern pattern = Pattern.compile("(?:^" + telemetryName + "|" + "\\W" + telemetryName + ")\\s*(?:"
                    + COMPARISON_OPERATOR_PATTERN + ")\\s*(?:" + NUMBER_PATTERN + "|" + QUOTED_STRING_PATTERN + ")");
            List<String> thresholds = new ArrayList<>();
            Consumer<SortedSet<?>> consumer = new Consumer<SortedSet<?>>() {
                @Override
                public void accept(SortedSet<?> set) {
                    int size = set.size();
                    if (size > 0) {
                        Object[] array = set.toArray();
                        if (size > 1) {
                            thresholds.add("between " + array[0] + " and " + array[size - 1]);
                        } else {
                            thresholds.add(array[0].toString());
                        }
                    }
                }
            };
            String[] orParts = criteria.split("\\|\\|");
            for (String orCriteria : orParts) {
                TreeSet<Double> numbers = new TreeSet<>();
                TreeSet<String> strings = new TreeSet<>();
                String[] andParts = orCriteria.split("&&");
                for (String andCriteria : andParts) {
                    Matcher matcher = pattern.matcher(andCriteria);
                    while (matcher.find()) {
                        for (int i = 1; i <= matcher.groupCount(); i++) {
                            String group = matcher.group(i);
                            if (group != null) {
                                try {
                                    numbers.add(Double.parseDouble(group));
                                } catch (Exception e) {
                                    strings.add(group);
                                }
                            }
                        }
                    }
                }
                consumer.accept(numbers);
                consumer.accept(strings);
            }
            if (thresholds.size() > 0) {
                threshold = String.join(", ", thresholds);
                break;
            }
        }
        return threshold;
    }
}
