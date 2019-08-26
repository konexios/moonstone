package com.arrow.kronos.action;

import com.arrow.kronos.DeviceActionTypeConstants;
import com.arrow.kronos.data.DeviceActionType;

import moonstone.acs.AcsLogicalException;

public class ActionHandlerFactory {

    public static ActionHandler create(DeviceActionType type) {
        switch (type.getSystemName()) {
        case DeviceActionTypeConstants.SendEmail.SYSTEM_NAME:
            return new SendEmailHandler();
        case DeviceActionTypeConstants.PostBackURL.SYSTEM_NAME:
            return new PostBackURLHandler();
        case DeviceActionTypeConstants.SendCommand.SYSTEM_NAME:
            return new SendCommandHandler();
        default:
            throw new AcsLogicalException("UNSUPPORTED TYPE: " + type);
        }
    }
}
