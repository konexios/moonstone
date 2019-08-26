package moonstone.selene.device.harting.rfid.tag;

import java.io.IOException;

public class TagCommunicationException extends IOException {
	private static final long serialVersionUID = -2800302671187807341L;

	public TagCommunicationException(String message) {
		super(message);
	}

	public TagCommunicationException(Throwable cause) {
		super(cause);
	}
}
