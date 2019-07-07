package com.arrow.selene.device.harting.rfid.tag;

import com.arrow.selene.engine.DeviceModuleAbstract;

public abstract class TagModuleAbstract<Info extends TagInfoAbstract, Props extends TagPropertiesAbstract, States
		extends TagStatesAbstract, Data extends TagData>
		extends DeviceModuleAbstract<Info, Props, States, Data> {
}
