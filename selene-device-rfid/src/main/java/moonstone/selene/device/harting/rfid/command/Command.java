package moonstone.selene.device.harting.rfid.command;

import java.io.Serializable;

public interface Command extends Serializable {
	int IGNORED_MODE = -1;

	byte[] build(int comAddress);

	int getCommandMode();
}
