package com.arrow.pegasus.core.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.arrow.pegasus.api.ApiAbstract;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventParameter;
import com.arrow.pegasus.service.EventService;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.StatusModel;

public abstract class EventApiAbstract extends ApiAbstract {

    @Autowired
    private EventService service;

    @RequestMapping(path = "/{hid}/received", method = RequestMethod.PUT)
    public StatusModel putReceived(@PathVariable String hid) {
        Event event = service.getEventRespository().doFindByHid(hid);
        if (event != null) {
            service.receive(event.getId());
            return StatusModel.OK;
        } else {
            return StatusModel.error("event not found for hid: " + hid);
        }
    }

    @RequestMapping(path = "/{hid}/failed", method = RequestMethod.PUT)
    public StatusModel putFailed(@PathVariable String hid, @RequestBody(required = false) Map<String, String> body) {
        TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
        };
        Map<String, String> parameters = JsonUtils.fromJson(getApiPayload(), typeRef);
        Event event = service.getEventRespository().doFindByHid(hid);
        if (event != null) {
            service.failed(event.getId(), parameters.get("error"));
            return StatusModel.OK;
        } else {
            return StatusModel.error("event not found for hid: " + hid);
        }
    }

    @RequestMapping(path = "/{hid}/succeeded", method = RequestMethod.PUT)
    public StatusModel putSucceeded(@PathVariable String hid, @RequestBody(required = false) Map<String, String> body) {
        TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
        };
        Map<String, String> parameters = JsonUtils.fromJson(getApiPayload(), typeRef);
        Event event = service.getEventRespository().doFindByHid(hid);
        if (event != null) {
            List<EventParameter> list = new ArrayList<>();
            if (parameters != null) {
                for (String key : parameters.keySet()) {
                    list.add(EventParameter.OutString(key, parameters.get(key)));
                }
            }
            service.succeeded(event.getId(), list);
            return StatusModel.OK;
        } else {
            return StatusModel.error("event not found for hid: " + hid);
        }
    }
}
