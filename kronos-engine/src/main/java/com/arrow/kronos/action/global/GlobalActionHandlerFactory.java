package com.arrow.kronos.action.global;

import com.arrow.kronos.GlobalActionTypeConstants;
import com.arrow.kronos.data.action.GlobalActionType;

import moonstone.acs.AcsLogicalException;

public class GlobalActionHandlerFactory {

	public static GlobalActionHandler create(GlobalActionType globalActionType) {
		if (globalActionType == null) {
			throw new AcsLogicalException("globalActionType is null");
		}
		switch (globalActionType.getSystemName()) {
		case GlobalActionTypeConstants.PostBackURL.SYSTEM_NAME: {
			return new PostBackURLHandler();
		}
		case GlobalActionTypeConstants.SendEmail.SYSTEM_NAME: {
			return new SendEmailHandler();
		}
		default: {
			throw new AcsLogicalException("Unsupported globalActionType " + globalActionType.getSystemName());
		}
		}
	}

}
