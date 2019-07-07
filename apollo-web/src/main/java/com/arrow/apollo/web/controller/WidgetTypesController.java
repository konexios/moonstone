package com.arrow.apollo.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.apollo.web.model.ApolloModelUtil;
import com.arrow.apollo.web.model.WidgetTypeModels;
import com.arrow.pegasus.dashboard.service.WidgetTypeService;

@RestController
@RequestMapping("/api/apollo/settings/widgettypes")
public class WidgetTypesController extends ApolloControllerAbstract {

	@Autowired
	private WidgetTypeService widgetTypeService;

	@RequestMapping("/all")
	public List<WidgetTypeModels.WidgetTypeModel> findAll(HttpSession session) {

		String method = "findAll";
		logDebug(method, "...");

		return ApolloModelUtil.toWidgetTypeModels(widgetTypeService.getWidgetTypeRepository().findAll());
	}
}