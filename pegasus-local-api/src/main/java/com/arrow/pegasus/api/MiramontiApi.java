package com.arrow.pegasus.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.MiramontiTenant;
import com.arrow.pegasus.service.MiramontiService;

@RestController(value = "localMiramontiApi")
@RequestMapping("/api/v1/local/pegasus/miramonti")
public class MiramontiApi extends BaseApiAbstract {
	@Autowired
	private MiramontiService miramontiService;

	@RequestMapping(path = "/{number}/{zoneId}/{applicationEngineId}", method = RequestMethod.POST)
	public MiramontiTenant create(@PathVariable(name = "number", required = true) String number,
	        @PathVariable(name = "zoneId", required = true) String zoneId,
	        @PathVariable(name = "applicationEngineId", required = true) String applicationEngineId) {
		return miramontiService.createCompany(number, zoneId, applicationEngineId);
	}
}
