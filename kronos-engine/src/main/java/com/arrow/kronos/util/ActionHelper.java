package com.arrow.kronos.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.util.Assert;

import com.arrow.kronos.KronosEngineContext;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.service.DeviceActionWrapper;

public class ActionHelper {

    public static Iterable<DeviceAction> getDeviceActions(KronosEngineContext context, TelemetryWrapper wrapper,
            Device device) throws Exception {
        DeviceType deviceType = context.getKronosCache().findDeviceTypeById(device.getDeviceTypeId());
        Assert.notNull(deviceType, "deviceType not found for id: " + device.getDeviceTypeId());

        Stream.Builder<List<DeviceAction>> streamBuilder = Stream.builder();
        streamBuilder.add(device.getActions()).add(deviceType.getActions());

        if (device.getNodeId() != null) {
            Node node = context.getKronosCache().findNodeById(device.getNodeId());
            Assert.notNull(node, "node is not found for id: " + device.getNodeId());
            streamBuilder.add(node.getActions());
        }
        return streamBuilder.build().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static Iterable<DeviceActionWrapper> getDeviceActionWrappers(KronosEngineContext context,
            TelemetryWrapper wrapper, Device device) throws Exception {
        DeviceType deviceType = context.getKronosCache().findDeviceTypeById(device.getDeviceTypeId());
        Assert.notNull(deviceType, "deviceType not found for id: " + device.getDeviceTypeId());

        Stream.Builder<List<DeviceActionWrapper>> streamBuilder = Stream.builder();

        streamBuilder.add(wrapDeviceActions(device.getId(), device.getActions()))
                .add(wrapDeviceActions(deviceType.getId(), deviceType.getActions()));

        if (device.getNodeId() != null) {
            Node node = context.getKronosCache().findNodeById(device.getNodeId());
            Assert.notNull(node, "node is not found for id: " + device.getNodeId());
            streamBuilder.add(wrapDeviceActions(node.getId(), node.getActions()));
        }
        return streamBuilder.build().flatMap(Collection::stream).collect(Collectors.toList());
    }

    private static List<DeviceActionWrapper> wrapDeviceActions(String ownerId, List<DeviceAction> deviceActions) {
        return IntStream.range(0, deviceActions.size())
                .mapToObj(i -> new DeviceActionWrapper(i, ownerId, deviceActions.get(i))).collect(Collectors.toList());
    }
}
