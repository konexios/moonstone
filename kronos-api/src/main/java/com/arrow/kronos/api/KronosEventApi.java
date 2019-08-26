package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventParameter;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acn.client.model.EventModel;
import moonstone.acn.client.model.EventParametersModel;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.StatusModel;

@RestController
@RequestMapping("/api/v1/core/events")
public class KronosEventApi extends BaseApiAbstract {

	@RequestMapping(path = "/{hid}/received", method = RequestMethod.PUT)
	public StatusModel putReceived(@PathVariable String hid, HttpServletRequest request) {
		String method = "putReceived";

		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);
		Event event = getEventService().getEventRespository().doFindByHid(hid);
		auditLog(method, accessKey.getApplicationId(), (event == null ? null : event.getId()), accessKey.getId(),
				request);

		if (event != null) {
			Assert.isTrue(accessKey.getApplicationId().equals(event.getApplicationId()), "applicationId mismatched!");
			getEventService().receive(event.getId());
			return StatusModel.OK;
		} else {
			return StatusModel.error("event not found for hid: " + hid);
		}
	}

	@RequestMapping(path = "/{hid}/failed", method = RequestMethod.PUT)
	public StatusModel putFailed(@PathVariable String hid, @RequestBody(required = false) Map<String, String> body,
			HttpServletRequest request) {
		String method = "putFailed";

		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);
		Event event = getEventService().getEventRespository().doFindByHid(hid);
		auditLog(method, accessKey.getApplicationId(), (event == null ? null : event.getId()), accessKey.getId(),
				request);

		TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
		};
		Map<String, String> parameters = JsonUtils.fromJson(getApiPayload(), typeRef);
		if (event != null) {
			Assert.isTrue(accessKey.getApplicationId().equals(event.getApplicationId()), "applicationId mismatched!");

			String error = "";
			if (parameters != null) {
				error = parameters.get("error");
			}
			getEventService().failed(event.getId(), error);
			return StatusModel.OK;
		} else {
			return StatusModel.error("event not found for hid: " + hid);
		}
	}

	@RequestMapping(path = "/{hid}/succeeded", method = RequestMethod.PUT)
	public StatusModel putSucceeded(@PathVariable String hid, @RequestBody(required = false) Map<String, String> body,
			HttpServletRequest request) {
		String method = "putSucceeded";

		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);
		Event event = getEventService().getEventRespository().doFindByHid(hid);
		auditLog(method, accessKey.getApplicationId(), (event == null ? null : event.getId()), accessKey.getId(),
				request);

		TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
		};
		Map<String, String> parameters = JsonUtils.fromJson(getApiPayload(), typeRef);
		if (event != null) {
			Assert.isTrue(accessKey.getApplicationId().equals(event.getApplicationId()), "applicationId mismatched!");
			List<EventParameter> list = new ArrayList<>();
			if (parameters != null) {
				for (String key : parameters.keySet()) {
					list.add(EventParameter.OutString(key, parameters.get(key)));
				}
			}
			getEventService().succeeded(event.getId(), list);
			return StatusModel.OK;
		} else {
			return StatusModel.error("event not found for hid: " + hid);
		}
	}

	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public EventModel findByHid(@PathVariable String hid, HttpServletRequest request) {
		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);
		Event event = getEventService().getEventRespository().doFindByHid(hid);
		Assert.notNull(event, "event not found for hid: " + hid);
		Assert.isTrue(accessKey.getApplicationId().equals(event.getApplicationId()), "applicationId mismatched!");
		return populateModel(new EventModel(), event);
	}

	protected EventModel populateModel(EventModel model, Event event) {
		model.setApplicationId(event.getApplicationId());
		model.setType(event.getType().toString());
		model.setName(event.getName());
		model.setStatus(event.getStatus().toString());
		List<EventParameter> parameters = event.getParameters();
		List<EventParametersModel> eventList = new ArrayList<>();
		for (EventParameter parameter : parameters) {
			eventList.add(new EventParametersModel().withName(parameter.getName())
					.withDataType(parameter.getDataType().toString()).withType(parameter.getType().toString())
					.withValue(parameter.getValue()));
		}
		model.setParameters(eventList);
		model.setHid(event.getHid());
		model.setCreatedDate(event.getCreatedDate());
		model.setCreatedBy(event.getCreatedBy());
		model.setLastModifiedDate(event.getLastModifiedDate());
		model.setLastModifiedBy(event.getLastModifiedBy());
		return model;
	}
}
