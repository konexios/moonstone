package moonstone.selene.device.harting.rfid.tag;

import moonstone.selene.engine.DeviceModuleAbstract;

public abstract class TagModuleAbstract<Info extends TagInfoAbstract, Props extends TagPropertiesAbstract, States
		extends TagStatesAbstract, Data extends TagData>
		extends DeviceModuleAbstract<Info, Props, States, Data> {
}
