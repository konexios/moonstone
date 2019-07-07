package com.arrow.pegasus;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.Loggable;
import com.arrow.pegasus.web.model.ResponseContext;

@RestController
public class PingController extends Loggable {

	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
	protected ResponseContext isAvailablePing() {
		String method = "isAvailablePing";
		logDebug(method, "...");

		return new ResponseContext().start().withStatus(HttpStatus.SC_OK).end();
	}
}