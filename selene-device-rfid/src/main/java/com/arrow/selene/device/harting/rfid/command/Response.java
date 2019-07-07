package com.arrow.selene.device.harting.rfid.command;

import java.io.Serializable;

public interface Response<T extends Response<T>> extends Serializable {
	T parse(int mode, byte... payload);
}
