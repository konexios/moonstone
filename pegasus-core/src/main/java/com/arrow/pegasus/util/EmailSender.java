package com.arrow.pegasus.util;

public interface EmailSender {
	void send(String[] to, String[] cc, String subject, String body, EmailContentType contentType);

	void send(String from, String[] to, String[] cc, String subject, String body, EmailContentType contentType);
}
