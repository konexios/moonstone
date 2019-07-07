package com.arrow.selene.device.conduit;

public class LoraDeviceModuleImpl
        extends LoraDeviceModuleAbstract<LoraDeviceInfo, LoraDeviceProperties, LoraDeviceStates, LoraDeviceData> {

	@Override
	protected LoraDeviceProperties createProperties() {
		return new LoraDeviceProperties();
	}

	@Override
	protected LoraDeviceInfo createInfo() {
		return new LoraDeviceInfo();
	}

	@Override
	protected LoraDeviceStates createStates() {
		return new LoraDeviceStates();
	}
}
