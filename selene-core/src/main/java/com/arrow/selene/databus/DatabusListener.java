package com.arrow.selene.databus;

public interface DatabusListener {
	String getName();

	void receive(String queue, byte[] message);
}
