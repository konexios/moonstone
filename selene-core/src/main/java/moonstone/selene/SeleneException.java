package moonstone.selene;

import moonstone.acs.AcsRuntimeException;

public class SeleneException extends AcsRuntimeException {
	private static final long serialVersionUID = 5844046206713878998L;

	public SeleneException(String message) {
		super(message);
	}

	public SeleneException(String message, Throwable cause) {
		super(message, cause);
	}
}
