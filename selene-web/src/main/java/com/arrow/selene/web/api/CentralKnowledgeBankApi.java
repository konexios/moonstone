package com.arrow.selene.web.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.JsonUtils;
import com.arrow.selene.SeleneException;
import com.arrow.selene.web.api.data.OsModel;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/selene/centralknowledgebank")
public class CentralKnowledgeBankApi extends BaseApi {

	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public ResponseEntity<Object> download(@RequestBody String data) {
		Assert.notNull(data, "Data is null");
		try {
			JsonNode payload = JsonUtils.fromJson(data, JsonNode.class);
			String protocol = payload.get("protocol").asText() + "/";
			String vendor = payload.get("vendor").asText() + "/";
			String model = payload.get("model").asText() + ".json";
			String fileName = getConfigService().getWebProperties().getCentralKnowledgeBankPath();
			if (StringUtils.isEmpty(fileName)) {
				fileName = OsModel.getCentralKnowledgeBankPath() + protocol + vendor + model;
			} else {
				fileName += protocol + vendor + model;
			}
			logInfo("download", "Downloading %s file", fileName);
			File file = new File(fileName);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", file.getName());
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/txt"))
			        .body(resource);
		} catch (FileNotFoundException e) {
			throw new SeleneException("Error in downloading file", e);
		}
	}
}
