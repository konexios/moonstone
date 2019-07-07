package com.arrow.kronos.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.arrow.acn.AcnEventNames;
import com.arrow.acs.JsonUtils;
import com.arrow.kronos.DeviceActionTypeConstants;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceEvent;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.service.GatewayCommandService;
import com.arrow.kronos.service.KronosCache;
import com.arrow.pegasus.client.api.ClientAccessKeyApi;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventBuilder;
import com.arrow.pegasus.data.event.EventParameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SendCommandHandler extends ActionHandlerAbstract {

    @Autowired
    private KronosCache kronosCache;
    @Autowired
    private ClientAccessKeyApi clientAccessKeyApi;
    @Autowired
    private GatewayCommandService gatewayCommandService;

    @Override
    public void handle(DeviceEvent deviceEvent, TelemetryWrapper wrapper, Device device, DeviceAction action) {
        String method = "handle";
        Map<String, String> parameters = action.getParameters();
        String command = parameters.get(DeviceActionTypeConstants.SendCommand.PARAMETER_COMMAND);
        if (StringUtils.isEmpty(command)) {
            command = parameters.get(DeviceActionTypeConstants.SendCommand.PARAMETER_COMMAND_OLD);
        }
        Assert.hasText(command, "command is not defined in action");
        String payload = parameters.get(DeviceActionTypeConstants.SendCommand.PARAMETER_PAYLOAD);
        if (StringUtils.isEmpty(payload)) {
            payload = parameters.get(DeviceActionTypeConstants.SendCommand.PARAMETER_PAYLOAD_OLD);
        }
        Assert.hasText(payload, "payload is not defined in action");
        payload = validateJson(payload);
        Assert.notNull(payload, "Invalid JSON");
        for (TelemetryItem item : wrapper.getItems()) {
            if (item.value() != null) {
                command = command.replace("{" + item.getName() + "}", item.value().toString());
                payload = payload.replace("{" + item.getName() + "}", item.value().toString());
            }
        }

        logInfo(method, "command: %s, payload: %s", command, payload);

        String applicationId = device.getApplicationId();

        EventBuilder builder = EventBuilder.create().applicationId(applicationId)
                .name(AcnEventNames.ServerToGateway.DEVICE_COMMAND)
                .parameter(EventParameter.InString("deviceHid", device.getHid()))
                .parameter(EventParameter.InString(DeviceActionTypeConstants.SendCommand.PARAMETER_COMMAND, command))
                .parameter(EventParameter.InString(DeviceActionTypeConstants.SendCommand.PARAMETER_PAYLOAD, payload));

        Gateway gateway = kronosCache.findGatewayById(device.getGatewayId());
        Assert.notNull(gateway, "gateway is not found");
        AccessKey gatewayOwnerKey = clientAccessKeyApi.findOwnerKey(gateway.getPri());
        Assert.notNull(gatewayOwnerKey, "gateway owner is not found");

        Event event = gatewayCommandService.sendEvent(builder.build(), gateway.getId(), gatewayOwnerKey);

        logInfo(method, "sent event: %s", event.getId());

        deviceEvent.addInformation(DeviceActionTypeConstants.SendCommand.PARAMETER_COMMAND, command);
        deviceEvent.addInformation(DeviceActionTypeConstants.SendCommand.PARAMETER_PAYLOAD, payload);
    }

    private String validateJson(String content) {
        try {
            ObjectMapper objectMapper = JsonUtils.getObjectMapper().copy();
            if (content != null) {
                objectMapper.readTree(content);
                return content;
            } else {
                return objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                        .writeValueAsString(new Object());
            }
        } catch (Exception e) {
            return null;
        }
    }
}