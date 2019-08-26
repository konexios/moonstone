package moonstone.selene.device.harting.rfid.tag;

import java.util.Locale;

public class GenericTagModule
		extends TagModuleAbstract<GenericTagInfo, GenericTagProperties, GenericTagStates, TagData> {
	public GenericTagModule(String id) {
		String name = String.format("generic-rfid-tag-%s", id);
		getInfo().setName(name);
		getInfo().setUid(name.toLowerCase(Locale.getDefault()));
	}

	@Override
	protected GenericTagProperties createProperties() {
		return new GenericTagProperties();
	}

	@Override
	protected GenericTagInfo createInfo() {
		return new GenericTagInfo();
	}

	@Override
	protected GenericTagStates createStates() {
		return new GenericTagStates();
	}
}
